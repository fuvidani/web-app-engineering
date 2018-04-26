package at.ac.tuwien.waecm.ss18.group09.service

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.*
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(value = ["application.yml"], classes = [BackendTestApplication::class])
class MedicalQueryServiceTest {

    val testDataProvider = TestDataProvider()

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var medicalQueryService: IMedicalQueryService

    @Autowired
    lateinit var medicalInformationService: MedicalInformationService

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Before
    fun setUp() {
        mongoTemplate.dropCollection(MedicalQuery::class.java)
        mongoTemplate.dropCollection(MedicalInformation::class.java)
        mongoTemplate.dropCollection(SharingPermission::class.java)
        mongoTemplate.dropCollection(AbstractUser::class.java)
        mongoTemplate.dropCollection(User::class.java)
        mongoTemplate.dropCollection(ResearchFacility::class.java)
    }

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

        val list = medicalQueryService.findMatchingQueries(userId = user.id).collectList().block()!!

        assertNotNull("the returned object must not be null", list)
        assertEquals("List should have size of 1", 1, list.size)
        assertEquals("The list should contain the inserted info", medicalQuery, list[0])
    }

    //    @Test
    fun findAllSharedInformation_shouldReturn() {
        val user = testDataProvider.getDummyUser()
        userService.create(user).block()

        val info1 = createInfo("info 1", user)
        val info2 = createInfo("info 2", user)
        val info3 = createInfo("info 3", user)

        var medicalQuery = getMedicalQueryWithResearchReference()
        medicalQuery = medicalQueryService.create(medicalQuery).block()!!

        createPermission(info1.id, medicalQuery.id)
        createPermission(info2.id, medicalQuery.id)
        createPermission(info3.id, medicalQuery.id)

        val shared = medicalQueryService.findAllSharedInformationOfResearchFacility(medicalQuery.researchFacilityId).collectList().block()

        assertNotNull("the returned object must not be null", shared)
        assertEquals("List should have size of 1", 1, shared!!.size)

        assertAnonInfo(user, shared[0], info1, 0, 3)
        assertAnonInfo(user, shared[0], info2, 1, 3)
        assertAnonInfo(user, shared[0], info3, 2, 3)
    }

    //    @Test
    fun findSharedInformationForQuery_shouldReturn() {
        val user = testDataProvider.getDummyUser()
        userService.create(user).block()

        val info1 = createInfo("info 1", user)
        val info2 = createInfo("info 2", user)
        val info3 = createInfo("info 3", user)

        var medicalQuery = getMedicalQueryWithResearchReference()
        medicalQuery = medicalQueryService.create(medicalQuery).block()!!

        createPermission(info1.id, medicalQuery.id)
        createPermission(info2.id, medicalQuery.id)
        createPermission(info3.id, medicalQuery.id)

        val shared = medicalQueryService.findSharedInformationForQuery(medicalQuery.id!!).collectList().block()!!

        assertNotNull("the returned object must not be null", shared)
        assertEquals("List should have size of 1 (only 1 user)", 1, shared.size)
        assertEquals("the AnonymizedInfo should have 3 MedicalInfos", 3, shared[0].medicalInformation.size)

        println(shared[0])

        assertAnonInfo(user, shared[0], info1, 0, 3)
        assertAnonInfo(user, shared[0], info2, 1, 3)
//        assertAnonInfo(user, shared[0], info3,2,3)
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

    private fun assertAnonInfo(user: User, anonInfo: AnonymizedUserInformation, info: MedicalInformation, index: Int, expectedListSize: Int) {

        assertEquals("The anonymized info should contain the inserted & shared info: gender", user.gender, anonInfo.gender)
        assertEquals("The anonymized info should contain the inserted & shared info: birthday", user.birthday, anonInfo.birthday)
        assertEquals("The anonymized info should contain the inserted & shared medical info", expectedListSize, anonInfo.medicalInformation.size)
        assertEquals("The anonymized info should contain the inserted & shared info: title", info.title, anonInfo.medicalInformation[index].title)
        assertEquals("The anonymized info should contain the inserted & shared info:description", info.description, anonInfo.medicalInformation[index].description)
        assertTrue("The anonymized info should contain the inserted & shared info: tags", info.tags contentDeepEquals anonInfo.medicalInformation[index].tags)

        //anonyimzed part
        assertEquals("The anonymized info should have the same userid than the attached medical information userid", anonInfo.userId, anonInfo.medicalInformation[index].userId)

        assertNotSame("The anonymized info should have a different userid then the original user\"", user.id, anonInfo.medicalInformation[index].userId)
        assertNotSame("The anonymized info should have a different userid then the original user", user.id, anonInfo.userId)

    }
}