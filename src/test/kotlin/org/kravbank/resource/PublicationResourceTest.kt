package org.kravbank.resource

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.dao.PublicationForm
import org.kravbank.resource.utils.KeycloakAccess

@QuarkusTest
class PublicationResourceTest {

    val token = KeycloakAccess.getAccessToken("alice")


    @Test
    fun getPublication() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications/xxx4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listPublication() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications")
            .then()
            .statusCode(200)
    }

    @Test
    fun createPublication() {
        RestAssured.defaultParser = Parser.JSON
        val form = PublicationForm()
        form.comment = "Integrasjonstest publication - comment 1"
        form.version = 3

        val publication = PublicationForm().toEntity(form)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(publication)
            .header("Content-type", "application/json")
            .post("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications")
            .then()
            .statusCode(201)
    }

    @Test
    fun updatePublication() {
        RestAssured.defaultParser = Parser.JSON

        val form = PublicationForm()
        form.comment = "Oppdaterer --->>>> Integrasjonstest publication - comment 1"
        form.version = 111

        val publication = PublicationForm().toEntity(form)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(publication)
            .header("Content-type", "application/json")
            .put("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications/zzz4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun deletePublication() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications/xxx4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}