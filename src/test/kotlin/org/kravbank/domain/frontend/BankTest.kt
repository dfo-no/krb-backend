package org.kravbank.domain.frontend

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.io.InputStream

class BankTest {

    private lateinit var banksStream: InputStream
    private lateinit var projectsStream: InputStream
    private val mapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        banksStream = javaClass.getResourceAsStream("/banks.json") as InputStream
        projectsStream = javaClass.getResourceAsStream("/projects.json") as InputStream
    }

    @Test
    fun whenDeserializeBanks_thenSuccess() {

        if (this::banksStream.isInitialized) { // Probably overkill : )
            val banks = mapper.readValue<Banks>(banksStream)
            assertEquals(113, banks.size)
        } else {
            fail { "Could not load test data from resources/banks.json." }
        }
    }

    @Test
    fun whenDeserializeProjects_thenSuccess() {
        if (this::banksStream.isInitialized) {
            val projects = mapper.readValue<Banks>(projectsStream)
            assertEquals(34, projects.size)
        } else {
            fail { "Could not load test data from resources/projects.json." }
        }
    }
}