package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalInformation
import at.ac.tuwien.waecm.ss18.group09.dto.ResearchFacility
import at.ac.tuwien.waecm.ss18.group09.dto.User
import at.ac.tuwien.waecm.ss18.group09.service.IMedicalInformationService
import at.ac.tuwien.waecm.ss18.group09.service.IUserService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType
import org.springframework.http.MediaType.TEXT_EVENT_STREAM
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
@SpringBootTest(value = ["application.yml"], classes = [BackendTestApplication::class])
@AutoConfigureWebTestClient(timeout = "15000")
class MedicalInformationControllerTest {

    private val testDataProvider = TestDataProvider()

    @Autowired
    private lateinit var client: WebTestClient

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var medicalInformationService: IMedicalInformationService

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    private lateinit var user: AbstractUser
    private lateinit var firstObject: MedicalInformation
    private lateinit var secondObject: MedicalInformation

    @Before
    fun setUp() {
        mongoTemplate.dropCollection(AbstractUser::class.java)
        mongoTemplate.dropCollection(User::class.java)
        mongoTemplate.dropCollection(ResearchFacility::class.java)
        mongoTemplate.dropCollection(MedicalInformation::class.java)

        user = userService.create(testDataProvider.getDummyUser()).block()!!
    }

    private fun getMedicalInformationWithUserReference(): MedicalInformation {
        val medicalInformation = testDataProvider.getValidMedicalInformation()
        medicalInformation.userId = user.id
        return medicalInformation
    }

    @Test
    fun create_validCreate_shouldReturn() {

        val medicalInformation = getMedicalInformationWithUserReference()

        client.post().uri("/user/${user.id}/medicalInformation")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(medicalInformation), MedicalInformation::class.java)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isNotEmpty
    }

    @Test
    fun findAll_getRequestToRetrieveAllMedicalInformationForTheUser_shouldReturnAllInfos() {

        createTestDummyData()

        val result = client.get().uri("/user/${user.id}/medicalInformation")
                .accept(TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(TEXT_EVENT_STREAM)
                .returnResult(MedicalInformation::class.java)

        StepVerifier.create(result.responseBody)
                .expectNextCount(2)
                .expectNext(firstObject)
                .expectNext(secondObject)
    }

    private fun createTestDummyData() {

        val secondUser = testDataProvider.getDummyUser()
        secondUser.email = "other@mail.com"
        userService.create(secondUser).block()

        firstObject = testDataProvider.getValidMedicalInformation()
        firstObject.userId = user.id
        medicalInformationService.create(firstObject).block()

        val unrelevantInformation = testDataProvider.getValidMedicalInformation()
        unrelevantInformation.userId = secondUser.id
        medicalInformationService.create(unrelevantInformation).block()

        secondObject = testDataProvider.getValidMedicalInformation()
        secondObject.title = "other title"
        secondObject.description = "my rash"
        secondObject.userId = user.id

        medicalInformationService.create(firstObject).block()
        medicalInformationService.create(secondObject).block()
    }
}