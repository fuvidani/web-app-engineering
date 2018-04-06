package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import at.ac.tuwien.waecm.ss18.group09.dto.Gender
import at.ac.tuwien.waecm.ss18.group09.dto.User
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@RunWith(SpringRunner::class)
@SpringBootTest(value = ["application.yml"], classes = [BackendTestApplication::class])
@AutoConfigureWebTestClient
class UserControllerTest {

    @Autowired
    private lateinit var client: WebTestClient

    @Test
    fun register_registerWithValidUser_shouldReturnPersistedUser() {
        val user = getDummyUser()
        client.post().uri("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(user), User::class.java)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isNotEmpty
    }

    @Test
    fun register_registerWithInvalidUser_shouldReturnServerErrorBecauseOfInvalidUser() {
        val user = getDummyUser()
        user.password = ""
        client.post().uri("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(user), User::class.java)
                .exchange()
                .expectStatus().is4xxClientError
    }

    private fun getDummyUser(): User {
        return User(null, "kalu@gmail.com", "tobehashed", "dummy", Gender.MALE, "02.02.99")
    }
}