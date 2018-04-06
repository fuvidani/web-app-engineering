package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.dto.User
import at.ac.tuwien.waecm.ss18.group09.service.IUserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid

@CrossOrigin
@RestController
@RequestMapping("/user")
class UserController(private val userService: IUserService) {

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody user: User): Mono<ResponseEntity<User>> {
        return userService.create(user)
                .map { user -> ResponseEntity<User>(user, HttpStatus.OK) }
                .defaultIfEmpty(ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR))
    }
}