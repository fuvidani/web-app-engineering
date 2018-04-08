package at.ac.tuwien.waecm.ss18.group09.repository

import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalInformation
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface MedicalInformationRepository : ReactiveMongoRepository<MedicalInformation, String> {

    fun findByUser(user: AbstractUser): Flux<MedicalInformation>
}