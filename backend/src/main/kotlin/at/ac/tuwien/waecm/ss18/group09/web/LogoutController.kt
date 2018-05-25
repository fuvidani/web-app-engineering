package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.service.ISecurityService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@CrossOrigin
@RestController
class LogoutController(private val securityService: ISecurityService) {

    @PostMapping("/logoutUser")
    fun logUserOut(@RequestBody data: String): Mono<HttpStatus> {
        securityService.logoutUser(data)
        return Mono.just(HttpStatus.OK)
    }
}
