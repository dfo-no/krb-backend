package org.kravbank.api

import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.kravbank.domain.Codelist
import org.kravbank.service.CodelistService

@QuarkusTest
@QuarkusIntegrationTest
internal class CodelistResourceTestMock() {


    @InjectMock
    private lateinit var codelistService: CodelistService

/*
    @Test
    fun `should respond test`() {

        val projectRef= "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"

        every { codelistService.get(projectRef, codelistRef) } returns Codelist()
        //assertThat(firstService.greet()).isEqualTo("test")
    }

    @Test
    fun `should respond second`() {
        every { secondService.greet() } returns "second"
        assertThat(firstService.greet()).isEqualTo("second")
        verify { secondService.greet() }
    }

 */


    @Test
    fun getCodelist() {



    }
}
