package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.auth.IJwtService
import at.ac.tuwien.waecm.ss18.group09.dto.AuthRequest
import at.ac.tuwien.waecm.ss18.group09.dto.AuthResponse
import org.springframework.http.MediaType
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
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
    private val jwtService: IJwtService,
    private val userDetailsRepositoryReactiveAuthenticationManager: ReactiveAuthenticationManager
) {

    @PostMapping("/auth")
    @CrossOrigin
    fun getAuthenticationToken(@RequestBody authRequest: AuthRequest): Mono<AuthResponse> {

        val authenticationToken = UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
        val auth = userDetailsRepositoryReactiveAuthenticationManager.authenticate(authenticationToken)

        return auth
                .map { authentication -> parseAuthenticationToUser(authentication) }
                .map { user -> jwtService.generateJwt(user) }
                .map { token -> AuthResponse(token) }
    }

    fun parseAuthenticationToUser(authentication: Authentication): User {
        return User(authentication.principal.toString(), authentication.credentials as String, authentication.authorities)
    }
}