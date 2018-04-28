package at.ac.tuwien.waecm.ss18.group09.service

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.dto.*
import at.ac.tuwien.waecm.ss18.group09.repository.MedicalQueryRepository
import at.ac.tuwien.waecm.ss18.group09.repository.SharingPermissionRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.Period
import java.util.*

interface IMedicalQueryService {

    @Throws(ValidationException::class)
    fun create(medicalQuery: MedicalQuery): Mono<MedicalQuery>

    fun findByResearchFacilityId(id: String): Flux<MedicalQuery>

    fun findAll(): Flux<MedicalQuery>

    fun findMatchingQueries(userId: String): Flux<RelevantQueryData>

    fun createSharingPermission(sharingPermission: SharingPermission): Mono<SharingPermission>

    fun createSharingPermission(sharingPermissions: List<SharingPermission>): Mono<List<SharingPermission>>

    fun findAllSharedInformationOfResearchFacility(researchId: String): Flux<AnonymizedUserInformation>

    fun findSharedInformationForQuery(queryId: String): Flux<AnonymizedUserInformation>
}

@Component("medicalQueryService")
class MedicalQueryService(
    private val queryRepository: MedicalQueryRepository,
    private val sharingPermissionRepository: SharingPermissionRepository,
    private val medicalInformationService: IMedicalInformationService,
    private val userService: UserService
) : IMedicalQueryService {

    @Throws(ValidationException::class)
    override fun create(medicalQuery: MedicalQuery): Mono<MedicalQuery> {
        validate(medicalQuery)
        return queryRepository.save(medicalQuery)
    }

    override fun findByResearchFacilityId(id: String): Flux<MedicalQuery> {
        return queryRepository.findByResearchFacilityId(id)
    }

    override fun findAll(): Flux<MedicalQuery> {
        return queryRepository.findAll()
    }

    override fun findMatchingQueries(userId: String): Flux<RelevantQueryData> {

        val infos = medicalInformationService.findByUserId(userId)
        val user = userService.findById(userId).cast(User::class.java)

        return infos.zipWith(user)
            .map { tuple ->
                queryRepository.findByGenderAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(
                    tuple.t2.gender,
                    calcAge(tuple.t2.birthday),
                    calcAge(tuple.t2.birthday)
                )
                    .filter { query -> query.tags.any { qTag -> tuple.t1.tags.contains(qTag) } }
                    .map { q ->
                        val data = medicalInformationService
                            .findInfoForQuery(q, userId)
                            .map { info -> Pair(info.id!!, info.title) }
                            .collectList()
                            .map { info ->
                                RelevantQueryData(
                                    q.id!!, q.name, q.description, q.researchFacilityId, q.financialOffering,
                                    info
                                )
                            }
                        userService.findById(q.researchFacilityId)
                            .zipWith(data)
                            .map { tuple ->
                                tuple.t2.queryInstituteName = tuple.t1.username
                                tuple.t2
                            }
                    }.flatMap { it }
            }.flatMap { it }
    }

    override fun createSharingPermission(sharingPermission: SharingPermission): Mono<SharingPermission> {
        return sharingPermissionRepository.save(sharingPermission)
    }

    override fun createSharingPermission(sharingPermissions: List<SharingPermission>): Mono<List<SharingPermission>> {
        sharingPermissions.forEach { p -> createSharingPermission(p) }
        return Mono.just(sharingPermissions)
    }

    override fun findAllSharedInformationOfResearchFacility(researchId: String): Flux<AnonymizedUserInformation> {
        val queries = findByResearchFacilityId(researchId)
        return queries.map { q -> findSharedInformationForQuery(q.id!!) }.flatMap { it }
    }

    override fun findSharedInformationForQuery(queryId: String): Flux<AnonymizedUserInformation> {
        val permissions = sharingPermissionRepository.findByQueryId(queryId)

        return permissions
            .flatMap { p -> medicalInformationService.findById(p.information) }
            .groupBy { info -> info.userId }
            .flatMap { groupedFlux ->
                groupedFlux
                    .map { medicalInformation ->
                        AnonymizedUserInformation(
                            "",
                            mutableListOf(medicalInformation),
                            groupedFlux.key()!!,
                            null,
                            null
                        )
                    }
                    .log()
                    .reduce { a1: AnonymizedUserInformation, a2: AnonymizedUserInformation ->
                        AnonymizedUserInformation(
                            "",
                            a1.medicalInformation.union(a2.medicalInformation).toMutableList(),
                            a1.userId,
                            null,
                            null
                        )
                    }
                    .log()
            }
            .log("before user mapping")
            .map { an ->
                println(an.userId)
                userService.findById(an.userId)
                    .log("from user service 1")
                    .cast(User::class.java)
                    .log("from user service 1")
                    .map { user ->
                        println(user)
                        an.birthday = user.birthday
                        an.gender = user.gender
                        an.id = UUID.randomUUID().toString()
                        an.userId = UUID.randomUUID().toString()
                    }
                    .log("end of user service")
                an
            }.log("end of user service 2")

//        val permissions = sharingPermissionRepository.findByQueryId(queryId)
//        val anonymizer = PseduoAnonymizer(userService)
//
//        return permissions
//            .flatMap { p -> medicalInformationService.findById(p.information) }
//            .
//            .map { q -> anonymizer.anonymize(q) }
    }

    @Throws(ValidationException::class)
    private fun validate(medicalQuery: MedicalQuery) {

        if (
            medicalQuery.minAge == 0 &&
            medicalQuery.maxAge == 0 &&
            medicalQuery.gender == null &&
            medicalQuery.tags.isEmpty()
        )
            throw ValidationException("there must be at least one criterion met, min/max age, gender or tags")
    }

    private fun calcAge(birthDate: LocalDate): Int {
        return Period.between(birthDate, LocalDate.now()).years
    }
}