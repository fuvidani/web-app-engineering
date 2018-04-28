package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.AbstractTest
import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalQuery
import at.ac.tuwien.waecm.ss18.group09.service.IMedicalQueryService
import at.ac.tuwien.waecm.ss18.group09.service.IUserService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
internal class MedicalQueryControllerTest : AbstractTest() {

    override fun init() {
        user = userService.create(testDataProvider.getDummyUser()).block()!!
    }

    val testDataProvider = TestDataProvider()

    @Autowired
    private lateinit var client: WebTestClient

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var medicalQueryService: IMedicalQueryService

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    private lateinit var user: AbstractUser
    private lateinit var firstObject: MedicalQuery
    private lateinit var secondObject: MedicalQuery

    private fun getMedicalQueryWithResearchReference(): MedicalQuery {
        val researchFacility = userService.create(testDataProvider.getDummyResearcher()).block()!!
        val medicalQuery = testDataProvider.getValidMedicalQuery()
        medicalQuery.researchFacilityId = researchFacility.id
        return medicalQuery
    }

    @Test
    fun create_validCreate_shouldReturn() {
        val medicalQuery = getMedicalQueryWithResearchReference()

        client.post().uri("/user/${user.id}/medicalQuery")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(medicalQuery), MedicalQuery::class.java)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isNotEmpty
    }

    @Test
    fun findAll_getRequestToRetrieveAllMedicalQueryForTheUser_shouldReturnAllInfos() {

        createTestDummyData()

        val result = client.get().uri("/user/${user.id}/medicalQuery")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM)
            .returnResult(MedicalQuery::class.java)

        StepVerifier.create(result.responseBody)
            .expectNextCount(2)
            .expectNext(firstObject)
            .expectNext(secondObject)
    }

    @Test
    fun getAllMatchingQueries_shouldReturn() {
        //TODO
    }

    @Test
    fun createSharingPermission_valid_shouldReturn() {
        //TODO
    }

    @Test
    fun createSharingPermissionList_valid__shouldReturn() {
        //TODO
    }

    @Test
    fun getSharedInformationForQuery_valid__shouldReturn() {
        //TODO
    }

    private fun createTestDummyData() {

        val secondUser = testDataProvider.getDummyUser()
        secondUser.email = "other@mail.com"
        userService.create(secondUser).block()

        firstObject = testDataProvider.getValidMedicalQuery()
        firstObject.researchFacilityId = user.id
        medicalQueryService.create(firstObject).block()

        val unrelevantInformation = testDataProvider.getValidMedicalQuery()
        unrelevantInformation.researchFacilityId = secondUser.id
        medicalQueryService.create(unrelevantInformation).block()

        secondObject = testDataProvider.getValidMedicalQuery()
        secondObject.name = "other title"
        secondObject.description = "my rash"

        medicalQueryService.create(firstObject).block()
        medicalQueryService.create(secondObject).block()
    }
}