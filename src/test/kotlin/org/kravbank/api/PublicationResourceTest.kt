package org.kravbank.api

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.domain.Publication


@QuarkusIntegrationTest
class PublicationResourceTest {
    private final val baseUri = "http://localhost:8080"
    private final val basePath = "/api/v1/projects"
    private final val useProjectRef = "/bbb4db69-edb2-431f-855a-4368e2bcddd1"
    private final val useResourceFolder = "/publications"
    private final val usePublicationRef = "/xxx4db69-edb2-431f-855a-4368e2bcddd1"
    private final val usePublicaionRefPut = "/zzz4db69-edb2-431f-855a-4368e2bcddd1"
    private final val resourceUrl = "$baseUri$basePath$useProjectRef$useResourceFolder"
    private final val fullUrl = "$resourceUrl$usePublicaionRefPut"
    @Test
    fun getPublication() {
        RestAssured.given()
            //.pathParam("uuid", uuid)
            .`when`()
            .get("$resourceUrl$usePublicationRef")
            .then()
            .statusCode(200)
        // .body(`is`("hello $uuid"))
    }

    @Test
    fun listPublication() {
        RestAssured.given()
            .`when`().get(resourceUrl)
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))

    }

    @Test
    fun createPublication() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val publication = Publication ()
        publication.comment = "Integrasjonstest publication - comment 1"
        publication.deletedDate = "Integrasjonstest publication - del  1"

        RestAssured.given()
            .`when`()
            .body(publication)
            .header("Content-type", "application/json")
            .post("$useProjectRef$useResourceFolder")
            .then()
            .statusCode(201) //envt 200
    }

    @Test
    fun updatePublication() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val publication = Publication()
        publication.comment = "Oppdatert integrasjonstest pub - comment 1"
        publication.deletedDate = "Oppdatert integrasjonstest pub - del 1"

        RestAssured.given()
            .`when`()
            .body(publication)
            .header("Content-type", "application/json")
            .put(fullUrl)
            .then()
            .statusCode(200) //envt 200
    }


    @Test
    fun deletePublication() {
        RestAssured.given()
            .`when`()
            .delete("$resourceUrl$usePublicationRef")
            .then()
            .statusCode(204)
        //.body(`is`("Hello RESTEasy"))
    }

}