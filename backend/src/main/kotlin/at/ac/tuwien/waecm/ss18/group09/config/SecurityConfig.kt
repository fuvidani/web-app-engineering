package at.ac.tuwien.waecm.ss18.group09.config

import at.ac.tuwien.waecm.ss18.group09.auth.IJwtService
import at.ac.tuwien.waecm.ss18.group09.auth.JwtAuthWebFilter
import at.ac.tuwien.waecm.ss18.group09.auth.JwtReactiveAuthenticationManager
import at.ac.tuwien.waecm.ss18.group09.auth.JwtService
import at.ac.tuwien.waecm.ss18.group09.service.IUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.util.pattern.PathPatternParser
import java.security.SecureRandom

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
    fun userDetailsRepositoryReactiveAuthenticationManager(userService: IUserService): UserDetailsRepositoryReactiveAuthenticationManager {
        val manager = UserDetailsRepositoryReactiveAuthenticationManager(userService)
        manager.setPasswordEncoder(passwordEncoder())
        return manager
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

        http.authorizeExchange().pathMatchers("/auth", "/user/register").permitAll()
                .and()
                .authorizeExchange()
                .pathMatchers("/swagger")
                .permitAll()
        http.authorizeExchange().anyExchange().authenticated()
                .and().addFilterAt(jwtAuthWebFilter, SecurityWebFiltersOrder.HTTP_BASIC)
                .addFilterAt(corsFilter(), SecurityWebFiltersOrder.HTTP_BASIC)

        return http.build()
    }

    fun corsFilter(): CorsWebFilter {
        val config = CorsConfiguration()

        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")

        val source = UrlBasedCorsConfigurationSource(PathPatternParser())
        source.registerCorsConfiguration("/**", config)

        return CorsWebFilter(source)
    }

    @Bean(name = ["passwordEncoder"])
    fun passwordEncoder(): BCryptPasswordEncoder {
        //15 rounds in BCrypt - uses already a salt internally
        return BCryptPasswordEncoder(15, SecureRandom())
    }
}