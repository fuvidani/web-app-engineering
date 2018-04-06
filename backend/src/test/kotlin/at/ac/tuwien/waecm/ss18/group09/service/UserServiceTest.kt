package at.ac.tuwien.waecm.ss18.group09.service

import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import at.ac.tuwien.waecm.ss18.group09.dto.Gender
import at.ac.tuwien.waecm.ss18.group09.dto.User
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.After
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

    @Autowired
    private lateinit var userService: IUserService

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @After
    fun tearDown() {
        mongoTemplate.dropCollection(User::class.java)
    }


    @Test
    fun create_creatingValidUser_shouldPersistUserAndReturn() {
        val user = getDummyUser()
        val plainTextPassword = user.password
        assertNull("the id of the user must be null", user.id)
        userService.create(user).block()
        assertNotNull("the created user must be persisted and returned", user)
        assertNotNull("the id of the user must be set by the database", user.id)
        assertNotEquals("the clear text input password must be hashed after creation", plainTextPassword, user.password)
    }

    @Test
    fun findById_creatingAndFindingUserById_shouldPersistAndReturnSameUser() {
        val toCreate = getDummyUser()
        userService.create(toCreate).block()
        assertNotNull("the id of the user must be set by the database", toCreate.id)
        val id: String = toCreate?.id ?: ""
        val foundUser = userService.findById(id).block()
        assertEquals("the created and found user must be equal", toCreate, foundUser)
    }

    @Test
    fun findByEmail_creatingAndFindingUserByEmail_shouldPersistAndReturnSameUser() {
        val toCreate = getDummyUser()
        userService.create(toCreate).block()
        val foundUser = userService.findByEMail(toCreate.email).block()
        assertEquals("the created and found user must be equal", toCreate, foundUser)
    }

    @Test
    fun checkIfEmailExists_creatingUserAndCheckingIfEmailExists_shouldFindEmail() {
        val toCreate = getDummyUser()
        val email = toCreate.email
        val foundEmailBeforeCreation = userService.checkIfEMailExists(email).block()
        assertFalse("the email address must not be found in the empty database", foundEmailBeforeCreation)
        userService.create(toCreate).block()
        val foundEmailAfterCreation = userService.checkIfEMailExists(email).block()
        assertTrue("the email address must be found after a user has been created with it", foundEmailAfterCreation)
    }

    private fun getDummyUser(): User {

        return User(null, "kalureg@gmx.at", "needstobehashed", "dummy", Gender.MALE, "02.02.99")
    }
}

