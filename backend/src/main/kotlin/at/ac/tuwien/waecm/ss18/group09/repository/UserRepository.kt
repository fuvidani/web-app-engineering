package at.ac.tuwien.waecm.ss18.group09.repository

import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : ReactiveMongoRepository<AbstractUser, String> {
    fun findByEmailIgnoringCase(email: String): Mono<AbstractUser>
}
