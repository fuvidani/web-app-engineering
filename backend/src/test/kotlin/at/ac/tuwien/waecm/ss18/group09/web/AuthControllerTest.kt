package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.AuthRequest
import at.ac.tuwien.waecm.ss18.group09.dto.ResearchFacility
import at.ac.tuwien.waecm.ss18.group09.dto.User
import at.ac.tuwien.waecm.ss18.group09.service.IUserService
import com.google.gson.Gson
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
    private val gson = Gson()
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
        val authRequest = getAuthRequestForUser(user)
        userService.create(user).block()
        makeValidAuthRequest(authRequest, user)
    }

    @Test
    fun auth_loginWithUserAndResearch_shouldSuccessfullyAuthorize() {
        val user = testDataProvider.getDummyUser()
        val researchFacility = testDataProvider.getDummyResearcher()
        val userAuthRequest = getAuthRequestForUser(user)
        val rfAuthRequest = getAuthRequestForUser(researchFacility)

        userService.create(user).block()
        userService.create(researchFacility).block()

        makeValidAuthRequest(userAuthRequest, user)
        makeValidAuthRequest(rfAuthRequest, researchFacility)
    }

    private fun makeValidAuthRequest(authRequest: AuthRequest, user: AbstractUser) {
        client.post().uri("/auth")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(authRequest), AuthRequest::class.java)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.token").isNotEmpty
                .jsonPath("$.user").isEqualTo(getUserAsJson(user))
    }

    @Test
    fun auth_loginWithNonExistingUser_shouldReturnError() {
        val user = testDataProvider.getDummyUser()
        val userAuthRequest = getAuthRequestForUser(user)
        client.post().uri("/auth")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(userAuthRequest), AuthRequest::class.java)
                .exchange()
                .expectStatus().is5xxServerError
    }

    private fun getUserAsJson(user: AbstractUser): String {
        return gson.toJson(user)
    }

    private fun getAuthRequestForUser(user: AbstractUser): AuthRequest {
        return AuthRequest(username = user.email, password = user.password)
    }
}