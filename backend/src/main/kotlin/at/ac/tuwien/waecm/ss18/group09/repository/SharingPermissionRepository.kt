package at.ac.tuwien.waecm.ss18.group09.repository

import at.ac.tuwien.waecm.ss18.group09.dto.SharingPermission
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface SharingPermissionRepository : ReactiveMongoRepository<SharingPermission, String> {

    fun findByQueryId(queryId: String): Flux<SharingPermission>

    fun findByQueryIdAndInformation(queryId: String, information: String): Mono<SharingPermission>
}