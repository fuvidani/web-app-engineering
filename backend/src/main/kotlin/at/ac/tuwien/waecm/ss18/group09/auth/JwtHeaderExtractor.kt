package at.ac.tuwien.waecm.ss18.group09.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.function.Function

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Michael Sober
 * @version 1.0.0
 * @since 1.0.0
 */
class JwtHeaderExtractor : Function<ServerWebExchange, Mono<Authentication>> {

    private val tokenType = "Bearer"
    private val header = "Authorization"

    override fun apply(exchange: ServerWebExchange): Mono<Authentication> {

        val request = exchange.request
        val authorizationHeader = request.headers.getFirst(header)

        if (authorizationHeader == null || !authorizationHeader.startsWith(tokenType)) {
            return Mono.empty()
        }

        val jwt = authorizationHeader.substring(tokenType.length + 1)
        val authentication = UsernamePasswordAuthenticationToken(null, jwt)
        return Mono.just(authentication)
    }
}