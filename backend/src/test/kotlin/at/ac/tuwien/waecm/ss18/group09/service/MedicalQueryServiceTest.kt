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
        mongoTemplate.dropCollection(AbstractUser::class.java)
        mongoTemplate.dropCollection(User::class.java)
        mongoTemplate.dropCollection(ResearchFacility::class.java)
    }

    private fun getMedicalQueryWithResearchReference(): MedicalQuery {
        val researchFacility = userService.create(testDataProvider.getDummyResearcher()).block()
        val medicalQuery = testDataProvider.getValidMedicalQuery()
        medicalQuery.researchFacility = researchFacility.id
        return medicalQuery
    }

    @Test
    fun create_validCreateOfMedicalQuery_shouldPersistAndReturn() {
        val medicalQuery = getMedicalQueryWithResearchReference()
        assertNull("the id of the query must be null before creation", medicalQuery.id)
        medicalQueryService.create(medicalQuery).block()
        assertNotNull("the returned object must not be null", medicalQuery)
        assertNotNull("the id of the stored object must not be null anymore", medicalQuery.id)
    }

    @Test(expected = ValidationException::class)
    fun crate_invalidCreateWithNoCriteria_shouldThrowException() {
        val medicalQuery = getMedicalQueryWithResearchReference()
        medicalQuery.minAge = Integer(0)
        medicalQuery.maxAge = Integer(0)
        medicalQuery.gender = null
        medicalQuery.tags = emptyArray()
        medicalQueryService.create(medicalQuery)
    }

    @Test
    fun findMatchingQueries_shouldReturn() {
        val user = testDataProvider.getDummyUser()
        userService.create(user).block()

        val info = testDataProvider.getValidMedicalInformation()
        info.user = user.id
        medicalInformationService.create(info).block()

        val medicalQuery = getMedicalQueryWithResearchReference()
        medicalQueryService.create(medicalQuery).block()

        val list = medicalQueryService.findMatchingQueries(userId = user.id).collectList().block()

        assertNotNull("the returned object must not be null", list)
        assertEquals("List should have size of 1", 1, list.size)
        assertEquals("The list should contain the inserted info", info, list.get(0))
    }

    @Test
    fun findSharedInformationForQuery_shouldReturn() {
        val user = testDataProvider.getDummyUser()
        userService.create(user).block()

        var info1 = testDataProvider.getValidMedicalInformation()
        var info2 = testDataProvider.getValidMedicalInformation()
        info1.user = user.id
        info2.user = user.id
        info1 = medicalInformationService.create(info1).block()!!
        info2 = medicalInformationService.create(info2).block()!!

        var medicalQuery = getMedicalQueryWithResearchReference()
        medicalQuery = medicalQueryService.create(medicalQuery).block()!!

        val list = medicalQueryService.findSharedInformationForQuery(medicalQuery.id!!).collectList().block()

        assertNotNull("the returned object must not be null", list)
        assertEquals("List should have size of 0 (no permissions are granted)", 0, list!!.size)

        val matches = medicalQueryService.findMatchingQueries(user.id).collectList().block()

//        assert(matches.size == 2)
//        assert(matches.contains(info1))
//        assert(matches.contains(info2))

        var permission = SharingPermission(null, info1.id!!, medicalQuery.id!!)
        permission = medicalQueryService.createSharingPermission(permission).block()!!

        var permission2 = SharingPermission(null, info2.id!!, medicalQuery.id!!)
        permission2 = medicalQueryService.createSharingPermission(permission2).block()!!

        val shared = medicalQueryService.findSharedInformationForQuery(medicalQuery.id!!).collectList().block()

        assertNotNull("the returned object must not be null", shared)
        assertEquals("List should have size of 1", 1, shared!!.size)

        val anonInfo = shared[0]

        assertEquals("The anonymized info should contain the inserted & shared info: gender", user.gender, anonInfo.gender)
        assertEquals("The anonymized info should contain the inserted & shared info: birthday", user.birthday, anonInfo.birthday)
        assertEquals("The anonymized info should contain the inserted & shared medical info", 2, anonInfo.medicalInformation.size)
        assertEquals("The anonymized info should contain the inserted & shared info: title", info1.title, anonInfo.medicalInformation[0].title)
        assertEquals("The anonymized info should contain the inserted & shared info:description", info1.description, anonInfo.medicalInformation[0].description)
        assertEquals("The anonymized info should contain the inserted & shared info: tags", info1.tags, anonInfo.medicalInformation[0].tags)

        //anonyimzed part
        assertEquals("The anonymized info should have the same userid than the attached medical information userid", anonInfo.userId, anonInfo.medicalInformation[0].user)
        assertEquals("The anonymized info should have the same userid than the attached medical information userid", anonInfo.userId, anonInfo.medicalInformation[1].user)

        assertNotSame("The anonymized info should have a different userid then the original user\"", user.id, anonInfo.medicalInformation[0].user)
        assertNotSame("The anonymized info should have a different userid then the original user\"", user.id, anonInfo.medicalInformation[1].user)
        assertNotSame("The anonymized info should have a different userid then the original user", user.id, anonInfo.userId)


    }
}