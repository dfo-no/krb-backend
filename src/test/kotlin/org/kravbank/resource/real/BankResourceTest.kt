package org.kravbank.resource.real

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.kravbank.frontend.Bank
import org.kravbank.utils.KeycloakAccess


@QuarkusTest
class BankResourceTest {

    private val token = KeycloakAccess.getAccessToken("bob")

    @Nested
    inner class GetBanks {

        @Test
        fun `get banks then return ok`() {

            RestAssured.defaultParser = Parser.JSON

            RestAssured.given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/banks/")
                .then()
                .statusCode(200)
                .extract().asString().isNotEmpty()
        }


        @Test
        fun `get banks based on query params then return ok`() {

            RestAssured.defaultParser = Parser.JSON

            RestAssured.given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/banks?pagesize=2&page0&fieldname=id&order=DESC")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("", Bank::class.java)
                .let {
                    assertEquals(it[0].title, "ScriptProsjekt6")
                    assertEquals(it[1].title, "ScriptProsjekt5")
                }
        }


        @Test
        fun `get banks based on query params then fail`() {

            RestAssured.defaultParser = Parser.JSON

            RestAssured.given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/banks?pagesize=2&page0&fieldname=id&order=ASC")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("", Bank::class.java)
                .let {
                    assertFalse(it[0].title == "ScriptProsjekt6")
                    assertFalse(it[1].title == "ScriptProsjekt5")
                }
        }
    }
}