package at.ac.tuwien.waecm.ss18.group09.web

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.io.InputStreamReader

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Daniel Fuevesi
 * @version 1.0.0
 * @since 1.0.0
 */
@CrossOrigin
@RestController
class BackendController(private val gson: Gson) {

    @GetMapping("/swagger", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun swaggerDocs(): Mono<String> {
        val stream = BackendController::class.java.classLoader.getResourceAsStream("swagger.json")
        val reader = JsonReader(InputStreamReader(stream))
        val result = gson.fromJson<JsonObject>(reader, JsonObject::class.java).toString()
        return Mono.just(result)
    }
}