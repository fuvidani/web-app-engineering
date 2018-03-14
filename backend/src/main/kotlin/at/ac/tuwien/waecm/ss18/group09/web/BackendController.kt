package at.ac.tuwien.waecm.ss18.group09.web

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Daniel Fuevesi
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class BackendController {

  private var dummyCounter = Counter(0)

  @GetMapping("/counter")
  fun getCounter(): Mono<Counter> {
    return Mono.just(dummyCounter)
  }

  @PostMapping("/counter")
  fun incrementCounter(): Mono<Void> {
    dummyCounter = Counter(dummyCounter.value + 1)
    return Mono.empty()
  }
}

data class Counter(val value: Int)