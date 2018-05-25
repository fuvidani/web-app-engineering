package at.ac.tuwien.waecm.ss18.group09.repository

import at.ac.tuwien.waecm.ss18.group09.dto.NotificationSubscriptionRequest
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface NotificationSubscriptionRepository : ReactiveMongoRepository<NotificationSubscriptionRequest, String> {

    fun findByEmailIgnoringCase(email: String): Mono<NotificationSubscriptionRequest>
}