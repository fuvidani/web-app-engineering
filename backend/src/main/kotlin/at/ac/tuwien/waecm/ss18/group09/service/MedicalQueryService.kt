package at.ac.tuwien.waecm.ss18.group09.service

import at.ac.tuwien.waecm.ss18.group09.dto.AnonymizedUserInformation
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalQuery
import at.ac.tuwien.waecm.ss18.group09.dto.SharingPermission
import at.ac.tuwien.waecm.ss18.group09.dto.User
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

    fun findByResearchFacility(id: String): Flux<MedicalQuery>

    fun findAll(): Flux<MedicalQuery>

    fun findMatchingQueries(userId: String): Flux<MedicalQuery>

    fun createSharingPermission(sharingPermission: SharingPermission): Mono<SharingPermission>

    fun createSharingPermission(sharingPermissions: List<SharingPermission>): Mono<List<SharingPermission>>

    fun findAllSharedInformation(researchId: String): Flux<AnonymizedUserInformation>

    fun findSharedInformationForQuery(id: String): Flux<AnonymizedUserInformation>
}

@Component("medicalQueryService")
class MedicalQueryService(private val repository: MedicalQueryRepository,
                          private val sharingPermissionRepository: SharingPermissionRepository,
                          private val medicalInformationService: IMedicalInformationService,
                          private val userService: UserService) : IMedicalQueryService {

    @Throws(ValidationException::class)
    override fun create(medicalQuery: MedicalQuery): Mono<MedicalQuery> {
        validate(medicalQuery)
        return repository.save(medicalQuery)
    }

    override fun findByResearchFacility(id: String): Flux<MedicalQuery> {
        return repository.findByResearchFacility(id)
    }

    override fun findAll(): Flux<MedicalQuery> {
        return repository.findAll()
    }

    override fun findMatchingQueries(userId: String): Flux<MedicalQuery> {
        val user = userService.findById(userId).block() as User
        val infos = medicalInformationService.findByUser(user = userId)
        val tags = infos.map { i -> i.tags }.flatMap { list -> Flux.fromArray(list) }.distinct().collectList().block()
        val queries = repository.findByGenderAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(user.gender, calcAge(user.birthday), calcAge(user.birthday))

//        TODO("handle null filter")
        return queries.filter { query -> query.tags.any { qTag -> tags.contains(qTag) } }
    }

    override fun createSharingPermission(sharingPermission: SharingPermission): Mono<SharingPermission> {
        return sharingPermissionRepository.save(sharingPermission)
    }

    override fun createSharingPermission(sharingPermissions: List<SharingPermission>): Mono<List<SharingPermission>> {
        sharingPermissions.forEach { p -> createSharingPermission(p) }
        return Mono.just(sharingPermissions)
    }

    override fun findAllSharedInformation(researchId: String): Flux<AnonymizedUserInformation> {
        val queries = findByResearchFacility(researchId)
        return queries.map { q -> findSharedInformationForQuery(q.id!!) }.flatMap { it }
    }

    override fun findSharedInformationForQuery(id: String): Flux<AnonymizedUserInformation> {
        val permissions = sharingPermissionRepository.findByQuery(id)

        return permissions
                .flatMap { p -> medicalInformationService.findById(p.information) }
                .sort { medicalInformation, medicalInformation2 ->
                    Integer.compare(
                            medicalInformation.user.hashCode(),
                            medicalInformation2.user.hashCode())
                }
                .groupBy { info -> info.user }
                .flatMap { groupedFlux ->
                    groupedFlux
                            .map { medicalInformation ->
                                AnonymizedUserInformation(
                                        "",
                                        mutableListOf(medicalInformation),
                                        groupedFlux.key()!!,
                                        null,
                                        null)
                            }
                            .reduce { a1: AnonymizedUserInformation, a2: AnonymizedUserInformation ->
                                AnonymizedUserInformation(
                                        "",
                                        a1.medicalInformation.union(a2.medicalInformation).toMutableList(),
                                        a1.userId,
                                        null,
                                        null)
                            }
                }
                .map { an ->
                    userService.findById(an.userId)
                            .map { user ->
                                user as User
                                println(user)
                                an.birthday = user.birthday
                                an.gender = user.gender
                                an.id = UUID.randomUUID().toString()
                                an.userId = UUID.randomUUID().toString()
                            }
                    an
                }
    }

    @Throws(ValidationException::class)
    private fun validate(medicalQuery: MedicalQuery) {

        if (
                medicalQuery.minAge == Integer(0) &&
                medicalQuery.maxAge == Integer(0) &&
                medicalQuery.gender == null &&
                medicalQuery.tags.isEmpty()
        )
            throw ValidationException("there must be at least one criterion met, min/max age, gender or tags")
    }

    private fun calcAge(birthDate: LocalDate): Int {
        return Period.between(birthDate, LocalDate.now()).years
    }
}