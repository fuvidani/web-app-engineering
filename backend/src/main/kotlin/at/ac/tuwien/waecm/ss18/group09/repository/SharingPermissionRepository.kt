package at.ac.tuwien.waecm.ss18.group09.repository

import at.ac.tuwien.waecm.ss18.group09.dto.SharingPermission
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface SharingPermissionRepository : ReactiveMongoRepository<SharingPermission, String> {

    fun findByQuery(query: String): Flux<SharingPermission>
}
