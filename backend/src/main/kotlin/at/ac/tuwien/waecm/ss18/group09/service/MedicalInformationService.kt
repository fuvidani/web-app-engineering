package at.ac.tuwien.waecm.ss18.group09.service

import at.ac.tuwien.waecm.ss18.group09.dto.MedicalInformation
import at.ac.tuwien.waecm.ss18.group09.repository.MedicalInformationRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IMedicalInformationService {

    @Throws(ValidationException::class)
    fun create(medicalInfo: MedicalInformation): Mono<MedicalInformation>

    fun findById(id: String): Mono<MedicalInformation>

    fun findByUser(user: String): Flux<MedicalInformation>
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

    override fun findByUser(user: String): Flux<MedicalInformation> {
        return repository.findByUser(user)
    }

    @Throws(ValidationException::class)
    private fun validate(medicalInformation: MedicalInformation) {

        if (medicalInformation.user.trim().isEmpty())
            throw ValidationException("no user reference was provided")

        if (medicalInformation.title.trim().isEmpty())
            throw ValidationException("the title of the medical information is mandatory and can not be empty or null")

        if (medicalInformation.description.trim().isEmpty() && medicalInformation.image.trim().isEmpty())
            throw ValidationException("either the description or image must be provided - both must not be null")

        if (medicalInformation.tags.isEmpty())
            throw ValidationException("there must be at least one tag provided")
    }
}