package org.kravbank.resource

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.dao.CodeForm
import org.kravbank.resource.utils.KeycloakAccess


@QuarkusTest
class CodeResourceTest {

    val token = KeycloakAccess.getAccessToken("alice")

    @Test
    fun getCode() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1/codes/script1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listCode() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1/codes/")
            .then()
            .statusCode(200)
    }

    @Test
    fun createCode() {
        RestAssured.defaultParser = Parser.JSON
        val code = CodeForm()
        code.title = "CODE Integrasjonstest tittel"
        code.description = "CODE Integrasjonstest code desc"
        val codeMapper = CodeForm().toEntity(code)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(codeMapper)
            .header("Content-type", "application/json")
            .post("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1/codes")
            .then()
            .statusCode(201)
    }

    @Test
    fun deleteCode() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/prosjekt6-edb2-431f-855a-4368e2bcddd1/codelists/newlist33333db69-edb2-431f-855a-4368e2bcddd1/codes/script6b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun updateCode() {
        RestAssured.defaultParser = Parser.JSON
        val code = CodeForm()
        code.title = "CODE Integrasjonstest tittel"
        code.description = "CODE Integrasjonstest code desc"
        val codeMapper = CodeForm().toEntity(code)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(codeMapper)
            .header("Content-type", "application/json")
            .put("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1/codes/script1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}