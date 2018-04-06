package at.ac.tuwien.waecm.ss18.group09.dto

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

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

data class AuthResponse(val token: String)

@Document(collection = "counters")
data class Counter(@Id val id: String, var value: Int)

enum class Gender {
    MALE, FEMALE
}

@Document(collection = "users")
data class User(@Id var id: String?, var email: String, var password: String, var name: String, var gender: Gender, var birthday: String)