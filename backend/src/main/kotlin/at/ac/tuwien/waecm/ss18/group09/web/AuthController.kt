package at.ac.tuwien.waecm.ss18.group09.web

import at.ac.tuwien.waecm.ss18.group09.auth.IJwtService
import at.ac.tuwien.waecm.ss18.group09.auth.JwtService
import at.ac.tuwien.waecm.ss18.group09.dto.AuthRequest
import at.ac.tuwien.waecm.ss18.group09.dto.AuthResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*
import kotlin.collections.ArrayList

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Michael Sober
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthController {

    @Autowired
    private lateinit var jwtService: IJwtService

    @Autowired
    private lateinit var userDetailsRepositoryReactiveAuthenticationManager : ReactiveAuthenticationManager

    @PostMapping("/auth")
    fun getAuthenticationToken(@RequestBody authRequest: AuthRequest): Mono<AuthResponse> {

        val authentication = UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
        var auth = userDetailsRepositoryReactiveAuthenticationManager.authenticate(authentication)

        return auth
                .map { a -> a.principal as User }
                .map { u -> jwtService.generateJwt(u) }
                .map { j -> AuthResponse(j) }

    }

}