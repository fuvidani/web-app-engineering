package at.ac.tuwien.waecm.ss18.group09.helper

import at.ac.tuwien.waecm.ss18.group09.TestDataProvider
import at.ac.tuwien.waecm.ss18.group09.dto.ResearchFacility
import org.junit.Before
import org.junit.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.SecureRandom

class PasswordGenerator {

    private val testDataProvider = TestDataProvider()
    private val passwordEncoder = BCryptPasswordEncoder(15, SecureRandom())
    private lateinit var researchFacilities: List<ResearchFacility>

    @Before
    fun setUp() {
        researchFacilities = testDataProvider.getResearchFacilities()
    }

    @Test
    fun password_generatePasswordHashesForFacilities_shouldPrintPasswordHash() {
        researchFacilities.map {
            println("email:${it.email} \t password: ${passwordEncoder.encode(it.password)}")
        }
    }
}