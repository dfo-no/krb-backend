package org.kravbank.resource.real

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kravbank.utils.KeycloakAccess

@QuarkusTest
class LiquibaseResourceTest {

    private val token = KeycloakAccess.getAccessToken("alice")

    @Test
    fun `assert liquibase changelogs is ok`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/liquibase")

        assertEquals(200, response.statusCode)
    }
}