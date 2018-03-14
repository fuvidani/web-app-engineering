package at.ac.tuwien.waecm.ss18.group09.web

import org.junit.Test
import org.springframework.http.MediaType
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
class BackendControllerTest {

  private val client = WebTestClient.bindToController(BackendController()).build()

  @Test
  fun testGetCounter() {
    client.get().uri("/counter")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk
        .expectBody()
        .json("{\"value\":0}")
  }

  @Test
  fun testPostCounter() {
    client.post().uri("/counter")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk

    client.get().uri("/counter")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk
        .expectBody()
        .json("{\"value\":1}")
  }
}