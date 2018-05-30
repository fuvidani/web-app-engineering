package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.AbstractTest
import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.MedicalQuery
import at.ac.tuwien.waecm.ss18.group09.dto.RelevantQueryData
import at.ac.tuwien.waecm.ss18.group09.dto.SharingPermission
import at.ac.tuwien.waecm.ss18.group09.service.IMedicalInformationService
import at.ac.tuwien.waecm.ss18.group09.service.IMedicalQueryService
import at.ac.tuwien.waecm.ss18.group09.service.IUserService
import com.google.gson.Gson
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
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
    lateinit var medicalInformationService: IMedicalInformationService

    var gson: Gson = Gson()

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
    @WithMockUser(username = "research@who.com", roles = arrayOf("RESEARCH"))
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
    @WithMockUser(username = "research@who.com", roles = arrayOf("RESEARCH"))
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
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
    fun createSharingPermission_valid_shouldReturn() {
        createTestDummyData()
        var info = testDataProvider.getValidMedicalInformation()
        info.userId = user.id
        info = medicalInformationService.create(info).block()!!

        val permission = SharingPermission(null, info.id!!, firstObject.id!!)
        val result = client.post().uri("/user/${user.id}/medicalQuery/permission")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(permission), SharingPermission::class.java)
            .exchange().expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isNotEmpty
    }

    @Test
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
    fun validGetAllMatchingQueries_withoutShared_shouldReturn() {
        val info = testDataProvider.getValidMedicalInformation()
        info.userId = user.id
        medicalInformationService.create(info).block()

        val medicalQuery = getMedicalQueryWithResearchReference()
        medicalQueryService.create(medicalQuery).block()

        createPermission(info.id, medicalQuery.id)

        client.get()
            .uri("/user/${user.id}/medicalQuery/matching")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange().expectStatus().isOk
            .expectBody().json("[]")
    }

    @Test
    @WithMockUser(username = "kalu@gmx.at", roles = arrayOf("END_USER"))
    fun validGetAllMatchingQueries_withShared_shouldReturn() {
        val info = testDataProvider.getValidMedicalInformation()
        info.userId = user.id
        medicalInformationService.create(info).block()

        val medicalQuery = getMedicalQueryWithResearchReference()
        medicalQueryService.create(medicalQuery).block()

        createPermission(info.id, medicalQuery.id)

        val relevantQueryData = RelevantQueryData(
            medicalQuery.id!!,
            medicalQuery.name,
            medicalQuery.description,
            testDataProvider.getDummyResearcher().username,
            medicalQuery.financialOffering,
            listOf(Pair(info.id!!, info.title))
        )

        val result = client.get()
            .uri("/user/${user.id}/medicalQuery/matching?includeAlreadyShared=true")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange().expectStatus().isOk
            .expectBody().json(gson.toJson(listOf(relevantQueryData)))
    }

    private fun createPermission(infoId: String?, queryId: String?): SharingPermission {
        val permission = SharingPermission(null, infoId!!, queryId!!)
        return medicalQueryService.createSharingPermission(permission).block()!!
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