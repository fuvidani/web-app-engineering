package at.ac.tuwien.waecm.ss18.group09

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.config.EnableWebFlux


/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Daniel Fuevesi
 * @version 1.0.0
 * @since 1.0.0
 */
@SpringBootApplication
@EnableWebFlux
@Configuration
@EnableWebFluxSecurity
class BackendApplication {

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      SpringApplication.run(BackendApplication::class.java, *args)
    }
  }

  @Bean
  fun userDetailsRepository(@Value("\${spring.security.user.name}") username: String,
                            @Value("\${spring.security.user.password}") password: String)
      : MapReactiveUserDetailsService {
    val user = User
        .withUsername(username)
        .password("{noop}$password")
        .roles("USER")
        .build()
    return MapReactiveUserDetailsService(user)
  }

  @Bean
  fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    http
        .authorizeExchange()
        .anyExchange().authenticated()
        .and()
        .httpBasic().and()
        .formLogin()
        .and().csrf().disable()
    return http.build()
  }
}