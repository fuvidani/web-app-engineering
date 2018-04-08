package at.ac.tuwien.waecm.ss18.group09

import at.ac.tuwien.waecm.ss18.group09.dto.Gender
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalInformation
import at.ac.tuwien.waecm.ss18.group09.dto.ResearchFacility
import at.ac.tuwien.waecm.ss18.group09.dto.User

class TestDataProvider {

    fun getDummyUser(): User {
        val user = User()
        user.email = "kalu@gmx.at"
        user.password = "abc"
        user.name = "lk"
        user.birthday = "02.02"
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
        return MedicalInformation(title = "my disease", description = "some description", image = "image", tags = listOf("disease"))
    }

    private fun getAdditionalResearcher(): ResearchFacility {
        val researcher = ResearchFacility()
        researcher.email = "research@bayer.com"
        researcher.password = "password"
        return researcher
    }
}
