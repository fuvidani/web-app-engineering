package at.ac.tuwien.waecm.ss18.group09.service

import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.ResearchFacility
import at.ac.tuwien.waecm.ss18.group09.dto.User
/* ktlint-disable no-wildcard-imports */
import junit.framework.TestCase.*
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(value = ["application.yml"], classes = [BackendTestApplication::class])
class UserServiceTest {

    private val testDataProvider = TestDataProvider()

    @Autowired
    private lateinit var userService: IUserService

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Before
    fun setUp() {
        mongoTemplate.dropCollection(AbstractUser::class.java)
        mongoTemplate.dropCollection(User::class.java)
        mongoTemplate.dropCollection(ResearchFacility::class.java)
    }

    @Test
    fun create_creatingValidUser_shouldPersistUserAndReturn() {
        val user = testDataProvider.getDummyUser()
        val plainTextPassword = user.password
        assertNull("the id of the user must be null", user.id)
        userService.create(user).block()
        assertNotNull("the created user must be persisted and returned", user)
        assertNotNull("the id of the user must be set by the database", user.id)
        assertNotEquals(
            "the clear text input password must be hashed after creation",
            plainTextPassword,
            user.password
        )
    }

    @Test
    fun create_createValidResearchFacility_shouldPersistAndReturn() {
        val researchFacility = testDataProvider.getDummyResearcher()
        val plainTextPassword = researchFacility.password
        assertNull("the id of the user must be null", researchFacility.id)
        userService.create(researchFacility).block()
        assertNotNull("the created user must be persisted and returned", researchFacility)
        assertNotNull("the id of the user must be set by the database", researchFacility.id)
        assertNotEquals(
            "the clear text input password must be hashed after creation",
            plainTextPassword,
            researchFacility.password
        )
        assertEquals("research@who.com", researchFacility.username)
        assertTrue(researchFacility.isAccountNonExpired)
        assertTrue(researchFacility.isAccountNonLocked)
        assertTrue(researchFacility.isCredentialsNonExpired)
        assertTrue(researchFacility.isEnabled)
        assertTrue(researchFacility.authorities.toMutableList()[0].authority == "ROLE_RESEARCH")
        assertNotNull(researchFacility.hashCode())
        assertEquals(researchFacility, researchFacility)
        assertNotEquals(researchFacility, null)
    }

    @Test
    fun findById_creatingAndFindingUserById_shouldPersistAndReturnSameUser() {
        val toCreate = testDataProvider.getDummyUser()
        userService.create(toCreate).block()
        assertNotNull("the id of the user must be set by the database", toCreate.id)
        val id: String = toCreate?.id ?: ""
        val foundUser = userService.findById(id).block()
        assertEquals("the created and found user must be equal", toCreate, foundUser)
        assertEquals(toCreate.name, (foundUser as User).name)
        assertEquals(toCreate.birthday, foundUser.birthday)
        assertEquals(toCreate.gender, foundUser.gender)
        assertNotNull(toCreate.hashCode())
        assertNotNull(toCreate.toString())
        assertNotNull(foundUser.toString())
        assertNotEquals(toCreate, testDataProvider.getResearchFacilities())
        assertEquals(toCreate, toCreate)
    }

    @Test
    fun findByEmail_creatingAndFindingUserByEmail_shouldPersistAndReturnSameUser() {
        val toCreate = testDataProvider.getDummyUser()
        userService.create(toCreate).block()
        val foundUser = userService.findByEMail(toCreate.email).block()
        assertEquals("the created and found user must be equal", toCreate, foundUser)
        assertNotNull(toCreate.toString())
    }

    @Test
    fun checkIfEmailExists_creatingUserAndCheckingIfEmailExists_shouldFindEmail() {
        val toCreate = testDataProvider.getDummyUser()
        val email = toCreate.email
        val foundEmailBeforeCreation = userService.checkIfEMailExists(email).block()
        assertFalse(
            "the email address must not be found in the empty database",
            foundEmailBeforeCreation!!
        )
        userService.create(toCreate).block()
        val foundEmailAfterCreation = userService.checkIfEMailExists(email).block()
        assertTrue(
            "the email address must be found after a user has been created with it",
            foundEmailAfterCreation!!
        )
    }

    @Test(expected = DuplicatedEmailException::class)
    fun create_invalidCreationWithTwoIdenticalEmails_shouldThrowException() {
        val user = testDataProvider.getDummyUser()
        val researchFacility = testDataProvider.getDummyResearcher()
        researchFacility.email = user.email
        userService.create(user).block()
        userService.create(researchFacility).block()
    }

    @Test
    fun findByEmail_creatingUserAndResearcherFindingByEmail_shouldReturnObjects() {
        val user = testDataProvider.getDummyUser()
        val researchFacility = testDataProvider.getDummyResearcher()
        userService.create(user).block()
        userService.create(researchFacility).block()

        val foundUser = userService.findByEMail(user.email).block()
        val foundResearcher = userService.findByEMail(researchFacility.email).block()

        assertEquals("the found user must be equal to the created one", user, foundUser)
        assertEquals(
            "the found researcher must be equal to the created one",
            researchFacility,
            foundResearcher
        )
    }
}
