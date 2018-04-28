package at.ac.tuwien.waecm.ss18.group09.service

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.AbstractTest
import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.*
import junit.framework.TestCase.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import reactor.test.StepVerifier
import java.time.Duration

@RunWith(SpringRunner::class)
@SpringBootTest(value = ["application.yml"], classes = [BackendTestApplication::class])
class MedicalQueryServiceTest : AbstractTest() {

    override fun init() {}

    val testDataProvider = TestDataProvider()

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var medicalQueryService: IMedicalQueryService

    @Autowired
    lateinit var medicalInformationService: MedicalInformationService

    private fun getMedicalQueryWithResearchReference(): MedicalQuery {
        val researchFacility = userService.create(testDataProvider.getDummyResearcher()).block()!!
        val medicalQuery = testDataProvider.getValidMedicalQuery()
        medicalQuery.researchFacilityId = researchFacility.id
        return medicalQuery
    }

    @Test
    fun create_validCreateOfMedicalQuery_shouldPersistAndReturn() {
        val medicalQuery = getMedicalQueryWithResearchReference()
        assertNull("the id of the queryId must be null before creation", medicalQuery.id)
        medicalQueryService.create(medicalQuery).block()
        assertNotNull("the returned object must not be null", medicalQuery)
        assertNotNull("the id of the stored object must not be null anymore", medicalQuery.id)
    }

    @Test(expected = ValidationException::class)
    fun crate_invalidCreateWithNoCriteria_shouldThrowException() {
        val medicalQuery = getMedicalQueryWithResearchReference()
        medicalQuery.minAge = 0
        medicalQuery.maxAge = 0
        medicalQuery.gender = null
        medicalQuery.tags = emptyArray()
        medicalQueryService.create(medicalQuery)
    }

    @Test
    fun findMatchingQueries_shouldReturn() {
        val user = testDataProvider.getDummyUser()
        userService.create(user).block()

        val info = testDataProvider.getValidMedicalInformation()
        info.userId = user.id
        medicalInformationService.create(info).block()

        val medicalQuery = getMedicalQueryWithResearchReference()
        medicalQueryService.create(medicalQuery).block()

        val list = medicalQueryService.findMatchingQueries(userId = user.id)

        val relevantQueryData = RelevantQueryData(
            medicalQuery.id!!,
            medicalQuery.name,
            medicalQuery.description,
            testDataProvider.getDummyResearcher().username,
            medicalQuery.financialOffering,
            listOf(Pair(info.id!!, info.title))

        )

        StepVerifier.create(list)
            .expectNextCount(1)
            .expectNext(relevantQueryData)
            .expectComplete()
            .verify(Duration.ofSeconds(10))
    }

    //    @Test
    fun findAllSharedInformation_shouldReturn() {

        println("create user")

        val user = testDataProvider.getDummyUser()
        userService.create(user).block()

        println("create infos")

        val info1 = createInfo("info 1", user)
        val info2 = createInfo("info 2", user)
        val info3 = createInfo("info 3", user)

        println("create query")

        var medicalQuery = getMedicalQueryWithResearchReference()
        medicalQuery = medicalQueryService.create(medicalQuery).block()!!

        println("create permissions")

        createPermission(info1.id, medicalQuery.id)
        createPermission(info2.id, medicalQuery.id)
        createPermission(info3.id, medicalQuery.id)

        println("findAllSharedInformationOfResearchFacility")

        val shared = medicalQueryService
            .findAllSharedInformationOfResearchFacility(medicalQuery.researchFacilityId)

        StepVerifier.create(shared)
            .expectNextCount(3)
            .assertNext { anon -> assertAnonInfo(user, anon, info1) }
            .assertNext { anon -> assertAnonInfo(user, anon, info2) }
            .assertNext { anon -> assertAnonInfo(user, anon, info3) }
            .expectComplete()
            .verify(Duration.ofSeconds(10))
    }

    //    @Test
    fun findSharedInformationForQuery_shouldReturn() {

        println("create user")

        val user = testDataProvider.getDummyUser()
        userService.create(user).block()

        println("create infos")

        val info1 = createInfo("info 1", user)
        val info2 = createInfo("info 2", user)
        val info3 = createInfo("info 3", user)

        println("create query")

        var medicalQuery = getMedicalQueryWithResearchReference()
        medicalQuery = medicalQueryService.create(medicalQuery).block()!!

        println("create permissions")

        createPermission(info1.id, medicalQuery.id)
        createPermission(info2.id, medicalQuery.id)
        createPermission(info3.id, medicalQuery.id)

        println("findSharedInformationForQuery")

        val shared = medicalQueryService
            .findSharedInformationForQuery(medicalQuery.id!!)

        println("start comparing")

        StepVerifier.create(shared)
            .expectNextCount(3)
            .assertNext { anon -> assertAnonInfo(user, anon, info1) }
            .assertNext { anon -> assertAnonInfo(user, anon, info2) }
            .assertNext { anon -> assertAnonInfo(user, anon, info3) }
            .expectComplete()
            .verify(Duration.ofSeconds(10))
    }

    private fun createPermission(infoId: String?, queryId: String?): SharingPermission {
        val permission = SharingPermission(null, infoId!!, queryId!!)
        return medicalQueryService.createSharingPermission(permission).block()!!
    }

    private fun createInfo(title: String, user: User): MedicalInformation {
        val info = testDataProvider.getValidMedicalInformation()
        info.title = title
        info.userId = user.id
        return medicalInformationService.create(info).block()!!
    }

    private fun assertAnonInfo(
        user: User,
        anonInfo: AnonymizedUserInformation,
        info: MedicalInformation
    ) {

        assertEquals(
            "The anonymized info should contain the inserted & shared info: gender",
            user.gender,
            anonInfo.gender
        )
        assertEquals(
            "The anonymized info should contain the inserted & shared info: birthday",
            user.birthday,
            anonInfo.birthday
        )
        assertEquals(
            "The anonymized info should contain the inserted & shared info: title",
            info.title,
            anonInfo.medicalInformation.title
        )
        assertEquals(
            "The anonymized info should contain the inserted & shared info:description",
            info.description,
            anonInfo.medicalInformation.description
        )
        assertTrue(
            "The anonymized info should contain the inserted & shared info: tags",
            info.tags contentDeepEquals anonInfo.medicalInformation.tags
        )

        //anonyimzed part
        assertEquals(
            "The anonymized info should have the same userid than the attached medical information userid",
            anonInfo.userId,
            anonInfo.medicalInformation.userId
        )

        assertNotSame(
            "The anonymized info should have a different userid then the original user\"",
            user.id,
            anonInfo.medicalInformation.userId
        )
        assertNotSame(
            "The anonymized info should have a different userid then the original user",
            user.id,
            anonInfo.userId
        )
    }
}