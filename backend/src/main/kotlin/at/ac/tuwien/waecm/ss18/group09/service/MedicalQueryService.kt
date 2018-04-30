package at.ac.tuwien.waecm.ss18.group09.service

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.dto.*
import at.ac.tuwien.waecm.ss18.group09.repository.MedicalQueryRepository
import at.ac.tuwien.waecm.ss18.group09.repository.SharingPermissionRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
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

    fun createSharingPermission(sharingPermissions: List<SharingPermission>): Flux<SharingPermission>

    fun findAllSharedInformationOfResearchFacility(researchId: String): Flux<AnonymizedUserInformation>

    fun findSharedInformationForQuery(queryId: String): Flux<AnonymizedUserInformation>
}

@Component("medicalQueryService")
class MedicalQueryService(
    private val queryRepository: MedicalQueryRepository,
    private val sharingPermissionRepository: SharingPermissionRepository,
    private val medicalInformationService: IMedicalInformationService,
    private val userService: UserService,
    private val mongoTemplate: ReactiveMongoTemplate
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
        val user = userService.findById(userId).cast(User::class.java).repeat()

        return infos.zipWith(user)
            .map { tuple ->
                // TODO check only not null criteria

                val age = calcAge(tuple.t2.birthday)
                val mongoQuery = Query()
                mongoQuery.addCriteria(
                    Criteria().andOperator(
                        Criteria().orOperator(
                            Criteria.where("gender").`is`(null),
                            Criteria.where("gender").`is`(tuple.t2.gender)
                        ),
                        Criteria().orOperator(
                            Criteria.where("gender").`is`(null),
                            Criteria.where("gender").`is`(tuple.t2.gender)
                        ),
                        Criteria().orOperator(
                            Criteria.where("minAge").`is`(null),
                            Criteria.where("minAge").lte(age)
                        ),
                        Criteria().orOperator(
                            Criteria.where("maxAge").`is`(null),
                            Criteria.where("maxAge").gte(age)
                        )
                    )
                )
                mongoTemplate.find(mongoQuery, MedicalQuery::class.java)
                    .filter { query ->
                        query.tags.isEmpty()
                                || query.tags.any { qTag -> tuple.t1.tags.contains(qTag) }
                    }
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
            }.flatMap { it }.distinct()
    }

    override fun createSharingPermission(sharingPermission: SharingPermission): Mono<SharingPermission> {
        return sharingPermissionRepository.save(sharingPermission)
    }

    override fun createSharingPermission(sharingPermissions: List<SharingPermission>): Flux<SharingPermission> {
        return Flux.fromIterable(sharingPermissions)
            .map { p -> createSharingPermission(p) }
            .flatMap { it }
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
                    .reduce { a1: AnonymizedUserInformation, a2: AnonymizedUserInformation ->
                        AnonymizedUserInformation(
                            "",
                            a1.medicalInformation.union(a2.medicalInformation).toMutableList(),
                            a1.userId,
                            null,
                            null
                        )
                    }
            }
            .map { an ->
                println(an.userId)
                userService.findById(an.userId)
                    .cast(User::class.java)
                    .map { user ->
                        println(user)
                        an.birthday = user.birthday
                        an.gender = user.gender
                        an.id = UUID.randomUUID().toString()
                        an.userId = UUID.randomUUID().toString()
                        an.medicalInformation.forEach { info -> info.userId = an.userId }
                        an
                    }
            }.flatMap { it }
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