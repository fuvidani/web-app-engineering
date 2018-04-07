package at.ac.tuwien.waecm.ss18.group09.service

import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.User
import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.ResearchFacility
import com.google.gson.Gson
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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
        assertNotEquals("the clear text input password must be hashed after creation", plainTextPassword, user.password)
    }

    @Test
    fun findById_creatingAndFindingUserById_shouldPersistAndReturnSameUser() {
        val toCreate = testDataProvider.getDummyUser()
        userService.create(toCreate).block()
        assertNotNull("the id of the user must be set by the database", toCreate.id)
        val id: String = toCreate?.id ?: ""
        val foundUser = userService.findById(id).block()
        assertEquals("the created and found user must be equal", toCreate, foundUser)
    }

    @Test
    fun findByEmail_creatingAndFindingUserByEmail_shouldPersistAndReturnSameUser() {
        val toCreate = testDataProvider.getDummyUser()
        userService.create(toCreate).block()
        val foundUser = userService.findByEMail(toCreate.email).block()
        assertEquals("the created and found user must be equal", toCreate, foundUser)
    }

    @Test
    fun checkIfEmailExists_creatingUserAndCheckingIfEmailExists_shouldFindEmail() {
        val toCreate = testDataProvider.getDummyUser()
        val email = toCreate.email
        val foundEmailBeforeCreation = userService.checkIfEMailExists(email).block()
        assertFalse("the email address must not be found in the empty database", foundEmailBeforeCreation)
        userService.create(toCreate).block()
        val foundEmailAfterCreation = userService.checkIfEMailExists(email).block()
        assertTrue("the email address must be found after a user has been created with it", foundEmailAfterCreation)
    }

    @Test(expected = DuplicatedEmailException::class)
    fun create_invalidCreationWithTwoIdenticalEmails_shouldThrowException() {
        val user = testDataProvider.getDummyUser()
        val second = testDataProvider.getDummyResearcher()
        second.email = user.email
        userService.create(user).block()
        userService.create(second).block()
    }

    @Test
    fun x() {
        val user = testDataProvider.getDummyUser()
        val gson = Gson()
        println(gson.toJson(user))
    }
}
