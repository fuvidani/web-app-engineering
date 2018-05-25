package at.ac.tuwien.waecm.ss18.group09.service

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import org.springframework.stereotype.Service
import java.util.*

interface ISecurityService {

    fun getAllLoggedInUsers(): Set<AbstractUser>

    fun loginUser(user: AbstractUser): AbstractUser

    fun logoutUser(email: String)
}

@Service
class SecurityService : ISecurityService {

    private val loggedInUser = TreeSet<AbstractUser>()

    override fun getAllLoggedInUsers(): Set<AbstractUser> {
        return loggedInUser
    }

    override fun loginUser(user: AbstractUser): AbstractUser {
        loggedInUser.add(user)
        return user
    }

    override fun logoutUser(email: String) {
        val user = loggedInUser.find { it.email == email }
        if (user != null) {
            this.loggedInUser.remove(user)
        }
    }
}