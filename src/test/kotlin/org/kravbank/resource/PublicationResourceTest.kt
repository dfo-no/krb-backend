package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.utils.publication.dto.PublicationForm
import org.kravbank.utils.publication.dto.PublicationFormUpdate


@QuarkusTest
@QuarkusIntegrationTest
class PublicationResourceTest {

    @Test
    fun getPublication() {
        given()
            .`when`()
            .get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications/xxx4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listPublication() {
        given()
            .`when`()
            .get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications")
            .then()
            .statusCode(200)
    }

    @Test
    fun createPublication() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects";

        val publication = PublicationForm()
        publication.comment = "Integrasjonstest publication - comment 1"
        publication.version = 3

        given()
            .`when`()
            .body(publication)
            .header("Content-type", "application/json")
            .post("/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications")
            .then()
            .statusCode(201)
    }

    @Test
    fun updatePublication() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects";

        val publication = PublicationFormUpdate()
        publication.comment = "Oppdatert Integrasjonstest publication - comment 1"

        given()
            .`when`()
            .body(publication)
            .header("Content-type", "application/json")
            .put("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications/zzz4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun deletePublication() {
        given()
            .`when`()
            .delete("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications/xxx4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}