package at.ac.tuwien.waecm.ss18.group09.config

import at.ac.tuwien.waecm.ss18.group09.auth.IJwtService
import at.ac.tuwien.waecm.ss18.group09.auth.JwtAuthWebFilter
import at.ac.tuwien.waecm.ss18.group09.auth.JwtReactiveAuthenticationManager
import at.ac.tuwien.waecm.ss18.group09.auth.JwtService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Michael Sober
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun userDetailsRepository(
        @Value("\${spring.security.user.name}") username: String,
        @Value("\${spring.security.user.password}") password: String
    ): MapReactiveUserDetailsService {
        val user = User
            .withUsername(username)
            .password("{noop}$password")
            .roles("USER")
            .build()
        return MapReactiveUserDetailsService(user)
    }

    @Bean
    fun userDetailsRepositoryReactiveAuthenticationManager(userDetailsRepository: MapReactiveUserDetailsService): UserDetailsRepositoryReactiveAuthenticationManager {
        return UserDetailsRepositoryReactiveAuthenticationManager(userDetailsRepository)
    }

    @Bean
    fun jwtService(): IJwtService {
        return JwtService()
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.httpBasic().disable()
        http.formLogin().disable()
        http.csrf().disable()
        http.logout().disable()

        val jwtReactiveAuthenticationManager = JwtReactiveAuthenticationManager(jwtService())
        val jwtAuthWebFilter = JwtAuthWebFilter(jwtReactiveAuthenticationManager)

        http.authorizeExchange().pathMatchers("/auth").permitAll()
        http.authorizeExchange().anyExchange().authenticated()
            .and().addFilterAt(jwtAuthWebFilter, SecurityWebFiltersOrder.HTTP_BASIC)

        return http.build()
    }

    /*@Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .httpBasic().and()
                .formLogin()
                .and().csrf().disable()
        return http.build()
    }*/
}