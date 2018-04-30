package at.ac.tuwien.waecm.ss18.group09.service

import at.ac.tuwien.waecm.ss18.group09.dto.MedicalInformation
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalQuery
import at.ac.tuwien.waecm.ss18.group09.repository.MedicalInformationRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IMedicalInformationService {

    @Throws(ValidationException::class)
    fun create(medicalInfo: MedicalInformation): Mono<MedicalInformation>

    fun findById(id: String): Mono<MedicalInformation>

    fun findByUserId(userId: String): Flux<MedicalInformation>

    fun findInfoForQuery(query: MedicalQuery, userId: String): Flux<MedicalInformation>
}

@Component("medicalInformationService")
class MedicalInformationService(private val repository: MedicalInformationRepository) : IMedicalInformationService {

    @Throws(ValidationException::class)
    override fun create(medicalInfo: MedicalInformation): Mono<MedicalInformation> {
        validate(medicalInformation = medicalInfo)
        return repository.save(medicalInfo)
    }

    override fun findById(id: String): Mono<MedicalInformation> {
        return repository.findById(id)
    }

    override fun findByUserId(userId: String): Flux<MedicalInformation> {
        return repository.findByUserId(userId)
    }

    override fun findInfoForQuery(query: MedicalQuery, userId: String): Flux<MedicalInformation> {
        return findByUserId(userId)
            .filter { info -> query.tags.isEmpty() || info.tags.any { iTag -> query.tags.contains(iTag) } }
    }

    @Throws(ValidationException::class)
    private fun validate(medicalInformation: MedicalInformation) {

        if (medicalInformation.userId.trim().isEmpty())
            throw ValidationException("no user reference was provided")

        if (medicalInformation.title.trim().isEmpty())
            throw ValidationException("the title of the medical information is mandatory and can not be empty or null")

        if (medicalInformation.description.trim().isEmpty() && medicalInformation.image.trim().isEmpty())
            throw ValidationException("either the description or image must be provided - both must not be null")

        if (medicalInformation.tags.isEmpty())
            throw ValidationException("there must be at least one tag provided")
    }
}