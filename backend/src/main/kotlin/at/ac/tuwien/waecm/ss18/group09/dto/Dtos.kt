package at.ac.tuwien.waecm.ss18.group09.dto

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotBlank

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

@Document(collection = "medicalInformation")
data class MedicalInformation(
    @Id
    var id: String? = null,
    @DBRef
    var user: AbstractUser? = null,
    @get: NotBlank
    var title: String = "",
    var description: String = "",
    var image: String = "",
    var tags: List<String> = listOf()
)