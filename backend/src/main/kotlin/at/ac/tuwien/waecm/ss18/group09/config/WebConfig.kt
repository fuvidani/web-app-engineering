package at.ac.tuwien.waecm.ss18.group09.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.reactive.config.EnableWebFlux

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Michael Sober
 * @version 1.0.0
 * @since 1.0.0
 */
@EnableWebFlux
@Configuration
class WebConfig {

    @Bean(name = ["validator"])
    fun validator(): LocalValidatorFactoryBean {
        val bean = LocalValidatorFactoryBean()
        return bean
    }

}