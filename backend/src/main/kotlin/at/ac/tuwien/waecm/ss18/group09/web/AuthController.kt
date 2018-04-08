package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.auth.IJwtService
import at.ac.tuwien.waecm.ss18.group09.dto.AuthRequest
import at.ac.tuwien.waecm.ss18.group09.dto.AuthResponse
import at.ac.tuwien.waecm.ss18.group09.service.IUserService
import com.google.gson.Gson
import org.springframework.http.MediaType
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
/* ktlint-disable no-wildcard-imports */
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Michael Sober
 * @version 1.0.0
 * @since 1.0.0
 */
@CrossOrigin
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthController(
    private val gson: Gson,
    private val jwtService: IJwtService,
    private val userService: IUserService,
    private val userDetailsRepositoryReactiveAuthenticationManager: ReactiveAuthenticationManager
) {
    @CrossOrigin
    @PostMapping("/auth")
    fun getAuthenticationToken(@RequestBody authRequest: AuthRequest): Mono<AuthResponse> {
        val authenticationToken = UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
        val auth = userDetailsRepositoryReactiveAuthenticationManager.authenticate(authenticationToken)
        return auth
                .map { authentication -> parseAuthenticationToUser(authentication) }
                .map { user -> AuthResponse(jwtService.generateJwt(user), getUserFromPrincipal(user.username)) }
    }

    fun parseAuthenticationToUser(authentication: Authentication): User {
        val user = authentication.principal as at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
        return User(user.email, user.password, user.authorities)
    }

    fun getUserFromPrincipal(email: String): String {
        return gson.toJson(userService.findByEMail(email).block())
    }
}