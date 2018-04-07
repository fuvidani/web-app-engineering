package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.AuthRequest
import at.ac.tuwien.waecm.ss18.group09.dto.ResearchFacility
import at.ac.tuwien.waecm.ss18.group09.dto.User
import at.ac.tuwien.waecm.ss18.group09.service.IUserService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@RunWith(SpringRunner::class)
@SpringBootTest(value = ["application.yml"], classes = [BackendTestApplication::class])
@AutoConfigureWebTestClient
class AuthControllerTest {

    private val testDataProvider = TestDataProvider()

    @Autowired
    private lateinit var client: WebTestClient

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Autowired
    private lateinit var userService: IUserService

    @Before
    fun setUp() {
        mongoTemplate.dropCollection(AbstractUser::class.java)
        mongoTemplate.dropCollection(User::class.java)
        mongoTemplate.dropCollection(ResearchFacility::class.java)
    }

    @Test
    fun auth_tryToLoginWithCreatedUser_shouldSuccessfullyAuthorize() {
        val user = testDataProvider.getDummyUser()
        val plainTextPw = user.password
        val userName = user.email

        userService.create(user).block()

        val authRequest = AuthRequest(userName, password = plainTextPw)

        client.post().uri("/auth")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(authRequest), AuthRequest::class.java)
                .exchange()
                .expectStatus().isOk
    }
}