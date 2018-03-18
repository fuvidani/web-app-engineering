package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.repository.CounterRepository
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
class BackendController(private val repository: CounterRepository) {

    private val id = "counter"

    @GetMapping("/counter")
    fun getCounter(): Mono<Int> {
        return repository.findByIdIgnoringCase(id).map { it.value }
    }

    @PostMapping("/counter")
    fun incrementCounter(): Mono<Void> {
        return repository.findByIdIgnoringCase(id)
            .flatMap {
                it.value = it.value + 1
                repository.save(it)
            }.then()
    }

    @PostMapping("/reset")
    fun resetCounter(): Mono<Void> {
        return repository.save(Counter(id, 0)).then()
    }
}

@Document(collection = "counters")
data class Counter(@Id val id: String, var value: Int)