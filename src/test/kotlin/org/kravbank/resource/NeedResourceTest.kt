package org.kravbank.resource

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.dao.NeedForm
import org.kravbank.resource.utils.KeycloakAccess

@QuarkusTest
class NeedResourceTest {

    val token = KeycloakAccess.getAccessToken("alice")

    @Test
    fun getNeed() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/needs/need1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listNeed() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/needs/")
            .then()
            .statusCode(200)
    }

    @Test
    fun createNeed() {
        RestAssured.defaultParser = Parser.JSON
        val form = NeedForm()
        form.title = "POST Integrasjonstest need - tittel 1"
        form.description = "POST Integrasjonstest need - beskrivelse 1"
        val need = NeedForm().toEntity(form)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(need)
            .header("Content-type", "application/json")
            .post("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/needs/")
            .then()
            .statusCode(201)
    }

    @Test
    fun updateNeed() {
        RestAssured.defaultParser = Parser.JSON
        val form = NeedForm()
        form.title = "PUT Integrasjonstest need - tittel 1"
        form.description = "PUT Integrasjonstest need - beskrivelse 1"
        val need = NeedForm().toEntity(form)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(need)
            .header("Content-type", "application/json")
            .put("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/needs/need1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun deleteNeed() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/needs/need2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}