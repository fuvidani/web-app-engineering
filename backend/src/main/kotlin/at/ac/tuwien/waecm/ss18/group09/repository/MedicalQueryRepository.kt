package at.ac.tuwien.waecm.ss18.group09.repository

import at.ac.tuwien.waecm.ss18.group09.dto.Gender
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalQuery
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface MedicalQueryRepository : ReactiveMongoRepository<MedicalQuery, String> {

    fun findByResearchFacilityId(researchFacilityId: String): Flux<MedicalQuery>

    @Query(
        "SELECT q " +
                "FROM MedicalQuery q " +
                "WHERE " +
                "(gender IS NULL OR gender = ?0)" +
                "AND" +
                "(minAge IS NULL OR minAge <= ?1)" +
                "AND" +
                "(maxAge IS NULL OR maxAge >= ?1)" +
                ""
    )
    fun findMatchingQuery(gender: Gender, age: Int): Flux<MedicalQuery>
}