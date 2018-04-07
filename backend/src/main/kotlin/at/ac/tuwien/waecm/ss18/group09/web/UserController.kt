package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.User
import at.ac.tuwien.waecm.ss18.group09.service.IUserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
/* ktlint-disable no-wildcard-imports */
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import javax.validation.Valid

@CrossOrigin
@RestController
@RequestMapping("/user")
class UserController(private val userService: IUserService) {

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody user: User): Mono<ResponseEntity<AbstractUser>> {

        return userService.create(user)
                .map { user -> ResponseEntity<AbstractUser>(user, HttpStatus.OK) }
                .defaultIfEmpty(ResponseEntity<AbstractUser>(HttpStatus.BAD_REQUEST))
    }
}