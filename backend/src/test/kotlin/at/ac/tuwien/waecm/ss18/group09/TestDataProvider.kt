package at.ac.tuwien.waecm.ss18.group09

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.dto.*
import java.time.LocalDate

class TestDataProvider {

    fun getDummyUser(): User {
        val user = User()
        user.email = "kalu@gmx.at"
        user.password = "abc"
        user.name = "lk"
        user.birthday = LocalDate.of(1994, 6, 6)
        user.gender = Gender.MALE
        return user
    }

    fun getDummyResearcher(): ResearchFacility {
        val researcher = ResearchFacility()
        researcher.email = "research@who.com"
        researcher.password = "password"
        return researcher
    }

    fun getResearchFacilities(): List<ResearchFacility> {
        return listOf(getDummyResearcher(), getAdditionalResearcher())
    }

    fun getValidMedicalInformation(): MedicalInformation {
        return MedicalInformation(title = "my disease",
                description = "some description",
                image = "image",
                tags = arrayOf("disease"))
    }

    private fun getAdditionalResearcher(): ResearchFacility {
        val researcher = ResearchFacility()
        researcher.email = "research@bayer.com"
        researcher.password = "password"
        return researcher
    }

    fun getValidMedicalQuery(): MedicalQuery {
        return MedicalQuery(name = "my_query",
                description = "some description",
                financialOffering = 5.05,
                minAge = Integer(18),
                maxAge = Integer(35),
                gender = Gender.MALE,
                tags = arrayOf("disease"))
    }
}
