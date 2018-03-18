package at.ac.tuwien.waecm.ss18.group09

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


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
class BackendApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(BackendApplication::class.java, *args)
        }
    }

}