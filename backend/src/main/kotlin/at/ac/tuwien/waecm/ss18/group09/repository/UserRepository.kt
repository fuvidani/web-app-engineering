package at.ac.tuwien.waecm.ss18.group09.repository

import at.ac.tuwien.waecm.ss18.group09.dto.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepository : ReactiveMongoRepository<User, String> {

    fun findByEmailIgnoringCase(email: String): Mono<User>
}