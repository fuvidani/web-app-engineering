package at.ac.tuwien.waecm.ss18.group09.repository

import at.ac.tuwien.waecm.ss18.group09.dto.MedicalQuery
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface MedicalQueryRepository : ReactiveMongoRepository<MedicalQuery, String> {

    fun findByResearchFacilityId(researchFacilityId: String): Flux<MedicalQuery>
}