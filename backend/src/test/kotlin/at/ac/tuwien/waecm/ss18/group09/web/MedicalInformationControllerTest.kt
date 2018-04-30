package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.AbstractTest
import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalInformation
import at.ac.tuwien.waecm.ss18.group09.service.IMedicalInformationService
import at.ac.tuwien.waecm.ss18.group09.service.IUserService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.MediaType.TEXT_EVENT_STREAM
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
class MedicalInformationControllerTest : AbstractTest() {

    override fun init() {
        user = userService.create(testDataProvider.getDummyUser()).block()!!
    }

    private val testDataProvider = TestDataProvider()

    @Autowired
    private lateinit var client: WebTestClient

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var medicalInformationService: IMedicalInformationService

    private lateinit var user: AbstractUser
    private lateinit var firstObject: MedicalInformation
    private lateinit var secondObject: MedicalInformation

    private fun getMedicalInformationWithUserReference(): MedicalInformation {
        val medicalInformation = testDataProvider.getValidMedicalInformation()
        medicalInformation.userId = user.id
        return medicalInformation
    }

    @Test
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
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
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
    fun create_invalidCreate_wrongUser_shouldFail() {

        val medicalInformation = getMedicalInformationWithUserReference()
        medicalInformation.userId = "99999"

        client.post().uri("/user/${user.id}/medicalInformation")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(medicalInformation), MedicalInformation::class.java)
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
    fun create_invalidCreate_noUser_shouldFail() {

        val medicalInformation = getMedicalInformationWithUserReference()
        medicalInformation.userId = ""

        client.post().uri("/user/${user.id}/medicalInformation")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(medicalInformation), MedicalInformation::class.java)
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
    fun create_invalidCreate_noTitle_shouldFail() {

        val medicalInformation = getMedicalInformationWithUserReference()
        medicalInformation.title = ""

        client.post().uri("/user/${user.id}/medicalInformation")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(medicalInformation), MedicalInformation::class.java)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
    fun create_validCreate_noDescr_shouldReturn() {

        val medicalInformation = getMedicalInformationWithUserReference()
        medicalInformation.description = ""

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
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
    fun create_validCreate_noImg_shouldReturn() {

        val medicalInformation = getMedicalInformationWithUserReference()
        medicalInformation.image = ""

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
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
    fun create_invalidCreate_noImgAndNoDescr_shouldFail() {

        val medicalInformation = getMedicalInformationWithUserReference()
        medicalInformation.image = ""
        medicalInformation.description = ""

        client.post().uri("/user/${user.id}/medicalInformation")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(medicalInformation), MedicalInformation::class.java)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
    fun create_invalidCreate_noTags_shouldFail() {

        val medicalInformation = getMedicalInformationWithUserReference()
        medicalInformation.tags = emptyArray()

        client.post().uri("/user/${user.id}/medicalInformation")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(medicalInformation), MedicalInformation::class.java)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
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