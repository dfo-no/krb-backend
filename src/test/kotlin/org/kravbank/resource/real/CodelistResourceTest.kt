package org.kravbank.resource.real

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.dao.CodelistForm
import org.kravbank.utils.KeycloakAccess

@QuarkusTest
class CodelistResourceTest {

    private val token = KeycloakAccess.getAccessToken("bob")

    @Test
    fun `get one codelist`() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun `list all codelist`() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/")
            .then()
            .statusCode(200)
    }

    @Test
    fun `create codelist`() {
        RestAssured.defaultParser = Parser.JSON

        val codelist = CodelistForm().apply {
            title = "CODELIST Integrasjonstest - Tittel 1"
            description = "CODELIST Integrasjonstest - Beskrivelse 1"
        }

        val codelistMapper = CodelistForm().toEntity(codelist)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(codelistMapper)
            .header("Content-type", "application/json")
            .post("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists")
            .then()
            .statusCode(201)
    }

    @Test
    fun `delete codelist`() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/prosjekt5-edb2-431f-855a-4368e2bcddd1/codelists/newlist2222db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }


    @Test
    fun `update codelist`() {
        RestAssured.defaultParser = Parser.JSON

        val codelist = CodelistForm().apply {
            title = "CODELIST Oppdatert integrasjonstest - Tittel 1"
            description = "CODELIST Oppdatert integrasjonstest - Beskrivelse 1"
        }

        val codelistMapper = CodelistForm().toEntity(codelist)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(codelistMapper)
            .header("Content-type", "application/json")
            .put("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}