package at.ac.tuwien.waecm.ss18.group09.web

/* ktlint-disable no-wildcard-imports */
import at.ac.tuwien.waecm.ss18.group09.auth.IJwtService
import at.ac.tuwien.waecm.ss18.group09.dto.AbstractUser
import at.ac.tuwien.waecm.ss18.group09.dto.AuthRequest
import at.ac.tuwien.waecm.ss18.group09.dto.AuthResponse
import at.ac.tuwien.waecm.ss18.group09.service.ISecurityService
import org.springframework.http.MediaType
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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
    private val jwtService: IJwtService,
    private val userDetailsRepositoryReactiveAuthenticationManager: ReactiveAuthenticationManager,
    private val securityService: ISecurityService
) {
    @CrossOrigin
    @PostMapping("/auth")
    fun getAuthenticationToken(@RequestBody authRequest: AuthRequest): Mono<AuthResponse> {
        val authenticationToken = UsernamePasswordAuthenticationToken(authRequest.email, authRequest.password)
        val auth = userDetailsRepositoryReactiveAuthenticationManager.authenticate(authenticationToken)
        return auth
                .map { authentication -> authentication.principal as AbstractUser }
                .map { user -> securityService.loginUser(user) }
                .map { user ->
                    AuthResponse(jwtService.generateJwt(user), user.id)
                }
    }
}