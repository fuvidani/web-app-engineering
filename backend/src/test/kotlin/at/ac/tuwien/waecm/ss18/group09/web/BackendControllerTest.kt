package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.BackendTestApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Daniel Fuevesi
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringRunner::class)
@SpringBootTest(value = ["application.yml"], classes = [BackendTestApplication::class])
@AutoConfigureWebTestClient
class BackendControllerTest {

  @Autowired
  private lateinit var client: WebTestClient

  @Before
  fun setUp() {
    resetCounter()
  }

  @Test
  fun testGetCounter() {
    assertGetCounter(0)
  }

  @Test
  fun testPostCounter() {
    postIncrement()
    postIncrement()
    assertGetCounter(2)
  }

  @Test
  fun testResetPostAndGetCombination() {
    assertGetCounter(0)
    postIncrement()
    postIncrement()
    postIncrement()
    assertGetCounter(3)
    resetCounter()
    assertGetCounter(0)
  }

  private fun postIncrement() {
    client.post().uri("/counter")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk
  }

  private fun assertGetCounter(expectedCount: Int) {
    client.get().uri("/counter")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk
        .expectBody()
        .json("$expectedCount")
  }

  private fun resetCounter() {
    client.post().uri("/reset")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk
  }
}