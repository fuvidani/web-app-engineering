package at.ac.tuwien.waecm.ss18.group09.auth

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.web.server.ServerWebExchange
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
class JwtServerAuthenticationEntryPoint : ServerAuthenticationEntryPoint {

    private val authenticateHeader = "WWW-Authenticate"
    private val headerValue = "Bearer"

    override fun commence(exchange: ServerWebExchange, e: AuthenticationException): Mono<Void> {
        return Mono.fromRunnable {
            val response = exchange.response
            response.statusCode = HttpStatus.UNAUTHORIZED
            response.headers.set(authenticateHeader, headerValue)
        }
    }
}