package at.ac.tuwien.waecm.ss18.group09.dto

/* ktlint-disable no-wildcard-imports */
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Daniel Fuevesi
 * @version 1.0.0
 * @since 1.0.0
 */
data class AuthRequest(val email: String, val password: String)

data class AuthResponse(val token: String)

enum class Gender {
    MALE, FEMALE
}

@Document(collection = "medicalInformation")
data class MedicalInformation(
    @Id
    var id: String? = null,
    var userId: String = "",
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
        if (userId != other.userId) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (image != other.image) return false
        if (!Arrays.equals(tags, other.tags)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + userId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + Arrays.hashCode(tags)
        return result
    }
}

@Document(collection = "medicalQuery")
data class MedicalQuery(
    @Id
    var id: String? = null,
    var researchFacilityId: String = "",
    @get: NotBlank
    var name: String = "",
    @get: NotBlank
    var description: String = "",
    @get: NotNull
    @get: Min(0)
    var financialOffering: Double = 0.0,
    var minAge: Int = 0,
    var maxAge: Int = 0,
    var gender: Gender?,
    var tags: Array<String> = emptyArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicalQuery

        if (id != other.id) return false
        if (researchFacilityId != other.researchFacilityId) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (financialOffering != other.financialOffering) return false
        if (minAge != other.minAge) return false
        if (maxAge != other.maxAge) return false
        if (!Arrays.equals(tags, other.tags)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + researchFacilityId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + financialOffering.hashCode()
        result = 31 * result + minAge.hashCode()
        result = 31 * result + maxAge.hashCode()
        result = 31 * result + Arrays.hashCode(tags)
        return result
    }
}

data class RelevantQueryData(
    var queryId: String,
    var queryName: String,
    var queryDescription: String,
    var queryInstituteName: String,
    var queryPrice: Double,
    var medicalInfo: List<Pair<String, String>>
)

@Document(collection = "sharingPermission")
data class SharingPermission(
    @Id
    var id: String? = null,
    @get: NotBlank
    var information: String,
    @get: NotBlank
    var queryId: String
)

data class AnonymizedUserInformation(
    var id: String,
    var medicalInformation: List<MedicalInformation>,
    var userId: String,
    var birthday: LocalDate?,
    var gender: Gender?
)