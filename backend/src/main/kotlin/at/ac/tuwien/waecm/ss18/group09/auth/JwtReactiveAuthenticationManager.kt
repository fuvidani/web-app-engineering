package at.ac.tuwien.waecm.ss18.group09.auth

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
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
class JwtReactiveAuthenticationManager(private val jwtService: IJwtService) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {

        try {

            val jwt = authentication.credentials as String
            val user = jwtService.parseJwt(jwt)
            val authResult = JwtAuthenticationToken(user.username, jwt, user.authorities)

            return Mono.just(authResult)

        } catch (e: JwtServiceException) {
            return Mono.error(BadCredentialsException("Token not valid."))
        }

    }

}