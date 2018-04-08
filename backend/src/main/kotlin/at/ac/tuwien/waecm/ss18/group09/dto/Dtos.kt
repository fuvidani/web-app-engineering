package at.ac.tuwien.waecm.ss18.group09.dto

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Daniel Fuevesi
 * @version 1.0.0
 * @since 1.0.0
 */
data class AuthRequest(val username: String, val password: String)

data class AuthResponse(val token: String, val user: String)

enum class Gender {
    MALE, FEMALE
}
