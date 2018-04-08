package at.ac.tuwien.waecm.ss18.group09.dto

/* ktlint-disable no-wildcard-imports */
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
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
    var user: String = "",
    @get: NotBlank
    var title: String = "",
    var description: String = "",
    var image: String = "",
    var tags: Array<String> = emptyArray()

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicalInformation

        if (id != other.id) return false
        if (user != other.user) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (image != other.image) return false
        if (!Arrays.equals(tags, other.tags)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + user.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + Arrays.hashCode(tags)
        return result
    }
}