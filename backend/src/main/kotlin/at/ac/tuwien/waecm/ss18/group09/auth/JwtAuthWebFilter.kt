package at.ac.tuwien.waecm.ss18.group09.auth

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.core.publisher.onErrorResume

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Michael Sober
 * @version 1.0.0
 * @since 1.0.0
 */
class JwtAuthWebFilter(private val reactiveJwtAuthenticationManager: ReactiveAuthenticationManager) : WebFilter {

    private var authenticationSuccessHandler: ServerAuthenticationSuccessHandler =
            WebFilterChainServerAuthenticationSuccessHandler()

    private var authenticationFailureHandler: ServerAuthenticationFailureHandler =
            ServerAuthenticationEntryPointFailureHandler(JwtServerAuthenticationEntryPoint())

    private val requiresAuthenticationMatcher =
            ServerWebExchangeMatchers.anyExchange()

    private val jwtHeaderExtractor: JwtHeaderExtractor = JwtHeaderExtractor()

    private val securityContextRepository = NoOpServerSecurityContextRepository.getInstance()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return this.requiresAuthenticationMatcher.matches(exchange)
                .filter { matchResult -> matchResult.isMatch }
                .flatMap<Authentication> { matchResult -> this.jwtHeaderExtractor.apply(exchange) }
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty<Authentication>()))
                .flatMap { token -> authenticate(exchange, chain, token) }
    }

    private fun authenticate(exchange: ServerWebExchange,
                             chain: WebFilterChain, token: Authentication): Mono<Void> {
        val webFilterExchange = WebFilterExchange(exchange, chain)
        return this.reactiveJwtAuthenticationManager.authenticate(token)
                .flatMap({ authentication -> onAuthenticationSuccess(authentication, webFilterExchange) })
                .onErrorResume(AuthenticationException::class, { e ->
                    this.authenticationFailureHandler.onAuthenticationFailure(webFilterExchange, e)
                })
    }

    private fun onAuthenticationSuccess(authentication: Authentication, webFilterExchange: WebFilterExchange): Mono<Void> {
        val exchange = webFilterExchange.exchange
        val securityContext = SecurityContextImpl()
        securityContext.authentication = authentication
        return this.securityContextRepository.save(exchange, securityContext)
                .then<Void>(this.authenticationSuccessHandler
                        .onAuthenticationSuccess(webFilterExchange, authentication))
                .subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
    }

}