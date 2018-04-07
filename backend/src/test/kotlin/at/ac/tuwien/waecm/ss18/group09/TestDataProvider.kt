package at.ac.tuwien.waecm.ss18.group09

import at.ac.tuwien.waecm.ss18.group09.dto.Gender
import at.ac.tuwien.waecm.ss18.group09.dto.ResearchFacility
import at.ac.tuwien.waecm.ss18.group09.dto.User

class TestDataProvider() {

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
        researcher.email = "research@ixo.com"
        researcher.password = "hashthis"
        return researcher
    }
}