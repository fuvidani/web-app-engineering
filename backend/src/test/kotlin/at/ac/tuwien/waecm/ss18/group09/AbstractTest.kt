package at.ac.tuwien.waecm.ss18.group09

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.dto.*
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest(value = ["application.yml"], classes = [BackendTestApplication::class])
@AutoConfigureWebTestClient(timeout = "15000")
abstract class AbstractTest {

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Before
    fun setUp() {
        mongoTemplate.dropCollection(AbstractUser::class.java)
        mongoTemplate.dropCollection(User::class.java)
        mongoTemplate.dropCollection(ResearchFacility::class.java)
        mongoTemplate.dropCollection(MedicalInformation::class.java)
        mongoTemplate.dropCollection(SharingPermission::class.java)
        mongoTemplate.dropCollection(MedicalQuery::class.java)

        init()
    }

    abstract fun init()
}