package at.ac.tuwien.waecm.ss18.group09.service

import at.ac.tuwien.waecm.ss18.group09.dto.MedicalQuery
import at.ac.tuwien.waecm.ss18.group09.repository.MedicalQueryRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IMedicalQueryService {

    @Throws(ValidationException::class)
    fun create(medicalQuery: MedicalQuery): Mono<MedicalQuery>

    fun findByResearchFacility(id: String): Flux<MedicalQuery>

    fun findAll(): Flux<MedicalQuery>
}

@Component("medicalQueryService")
class MedicalQueryService(private val repository: MedicalQueryRepository) : IMedicalQueryService {

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
}