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

    private val TOKEN_TYPE = "Bearer"
    private val HEADER_NAME = "Authorization"

    override fun apply(exchange: ServerWebExchange): Mono<Authentication> {

        val request = exchange.request
        val authorizationHeader = request.headers.getFirst(HEADER_NAME)

        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_TYPE)) {
            return Mono.empty()
        }

        val jwt = authorizationHeader.substring(TOKEN_TYPE.length + 1)
        val authentication = UsernamePasswordAuthenticationToken(null, jwt)
        return Mono.just(authentication)

    }

}