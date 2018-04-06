package at.ac.tuwien.waecm.ss18.group09.service

import at.ac.tuwien.waecm.ss18.group09.dto.User
import at.ac.tuwien.waecm.ss18.group09.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

interface IUserService {

    fun create(user: User): Mono<User>

    fun findById(id: String): Mono<User>

    fun checkIfEMailExists(email: String): Mono<Boolean>

    fun findByEMail(email: String): Mono<User>
}

@Component("userService")
class UserService(private val repository: UserRepository, private val passwordEncoder: PasswordEncoder) : IUserService {

    override fun create(user: User): Mono<User> {
        user.password = passwordEncoder.encode(user.password)
        return repository.save(user)
    }

    override fun findById(id: String): Mono<User> {
        return repository.findById(id)
    }

    override fun checkIfEMailExists(email: String): Mono<Boolean> {
        return findByEMail(email).hasElement()
    }

    override fun findByEMail(email: String): Mono<User> {
        return repository.findByEmailIgnoringCase(email)
    }
}