package at.ac.tuwien.waecm.ss18.group09.service

import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.repository.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

interface IUserService : ReactiveUserDetailsService {

    fun create(user: AbstractUser): Mono<AbstractUser>

    fun findById(id: String): Mono<AbstractUser>

    @Throws(DuplicatedEmailException::class)
    fun checkIfEMailExists(email: String): Mono<Boolean>

    fun findByEMail(email: String): Mono<AbstractUser>
}

@Component("userService")
class UserService(private val repository: UserRepository, private val passwordEncoder: PasswordEncoder) : IUserService {

    override fun findByUsername(username: String): Mono<UserDetails> {
        @Suppress("UNCHECKED_CAST")
        return this.findByEMail(username) as Mono<UserDetails>
    }

    @Throws(DuplicatedEmailException::class)
    override fun create(user: AbstractUser): Mono<AbstractUser> {
        val email = user.email
        val exists = checkIfEMailExists(email).block()

//        return checkIfEMailExists(email).filter { it -> it == true }
//                .switchIfEmpty(Mono.defer {
//                    Mono.error<DuplicatedEmailException>(DuplicatedEmailException("the email exists already in the database")
//                }).thenReturn (repository -> {})

        if (exists) {
            throw DuplicatedEmailException("the email exists already in the database")
        } else {
            user.password = passwordEncoder.encode(user.password)
            return repository.save(user)
        }
    }

    override fun findById(id: String): Mono<AbstractUser> {
        return repository.findById(id)
    }

    override fun checkIfEMailExists(email: String): Mono<Boolean> {
        return findByEMail(email).hasElement()
    }

    override fun findByEMail(email: String): Mono<AbstractUser> {
        return repository.findByEmailIgnoringCase(email)
    }
}