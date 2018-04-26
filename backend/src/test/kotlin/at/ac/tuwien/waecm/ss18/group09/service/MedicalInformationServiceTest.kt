package at.ac.tuwien.waecm.ss18.group09.service

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalInformation
import at.ac.tuwien.waecm.ss18.group09.dto.ResearchFacility
import at.ac.tuwien.waecm.ss18.group09.dto.User
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.junit4.SpringRunner
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
@SpringBootTest(value = ["application.yml"], classes = [BackendTestApplication::class])
class MedicalInformationServiceTest {

    val testDataProvider = TestDataProvider()

    @Autowired
    lateinit var medicalInformationService: IMedicalInformationService

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Before
    fun setUp() {
        mongoTemplate.dropCollection(MedicalInformation::class.java)
        mongoTemplate.dropCollection(AbstractUser::class.java)
        mongoTemplate.dropCollection(User::class.java)
        mongoTemplate.dropCollection(ResearchFacility::class.java)
    }

    private fun getMedicalInformationWithUserReference(): MedicalInformation {
        val user = userService.create(testDataProvider.getDummyUser()).block()
        val medicalInformation = testDataProvider.getValidMedicalInformation()
        medicalInformation.user = user.id
        return medicalInformation
    }

    @Test
    fun create_validCreate_shouldPersistAndReturn() {
        val medicalInformation = getMedicalInformationWithUserReference()
        assertNull("the id must be null before storing the document", medicalInformation.id)
        medicalInformationService.create(medicalInformation).block()
        assertNotNull("the created medical information must not be null", medicalInformation)
        assertNotNull("the id of the object must be set by the database", medicalInformation.id)
    }

    @Test(expected = ValidationException::class)
    fun create_invalidCreateWithMissingTitle_shouldThrowValidationException() {
        val medicalInformation = getMedicalInformationWithUserReference()
        medicalInformation.title = ""
        medicalInformationService.create(medicalInformation).block()
    }

    @Test(expected = ValidationException::class)
    fun create_invalidCreateWithNoDescriptionOrImage_shouldThrowValidationException() {
        val medicalInformation = getMedicalInformationWithUserReference()
        medicalInformation.description = ""
        medicalInformation.image = ""
        medicalInformationService.create(medicalInformation).block()
    }

    @Test(expected = ValidationException::class)
    fun create_invalidCreateWithNoTags_shouldThrowValidationException() {
        val medicalInformation = getMedicalInformationWithUserReference()
        medicalInformation.tags = emptyArray()
        medicalInformationService.create(medicalInformation).block()
    }

    @Test(expected = ValidationException::class)
    fun create_invalidCreateWithNoUser_shouldThrowValidationException() {
        val medicalInformation = testDataProvider.getValidMedicalInformation()
        medicalInformationService.create(medicalInformation).block()
    }

    @Test
    fun findById_creatingAndSearchingObject_shouldPersistAndReturn() {
        val medicalInformation = getMedicalInformationWithUserReference()
        medicalInformationService.create(medicalInformation).block()
        val id: String = medicalInformation?.id ?: ""
        val found = medicalInformationService.findById(id).block()
        assertEquals("the created and found object must be equal", medicalInformation, found)
    }

    @Test
    fun findByUser_creatingAndSearchingObjects_shouldPersistAndReturn() {
        val firstUser = userService.create(testDataProvider.getDummyUser()).block()

        val secondUser = testDataProvider.getDummyUser()
        secondUser.email = "other@mail.com"
        userService.create(secondUser).block()

        val firstObject = testDataProvider.getValidMedicalInformation()
        firstObject.user = firstUser.id
        medicalInformationService.create(firstObject).block()

        val unrelevantInformation = testDataProvider.getValidMedicalInformation()
        unrelevantInformation.user = secondUser.id
        medicalInformationService.create(unrelevantInformation).block()

        val secondObject = testDataProvider.getValidMedicalInformation()
        secondObject.title = "other title"
        secondObject.description = "my rash"
        secondObject.user = firstUser.id

        medicalInformationService.create(firstObject).block()
        medicalInformationService.create(secondObject).block()

        StepVerifier.create(medicalInformationService.findByUser(firstUser.id))
                .expectNext(firstObject)
                .expectNext(secondObject)
                .verifyComplete()
    }
}