package org.kravbank.api

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.domain.Requirement

@QuarkusTest
class RequirementResourceTest {
    private final val baseUri = "http://localhost:8080"
    private final val basePath = "/api/v1/projects"
    private final val useProjectRef = "/aaa4db69-edb2-431f-855a-4368e2bcddd1"
    private final val useResourceFolder = "/requirements"
    private final val useRequirementRefPut = "/req1b69-edb2-431f-855a-4368e2bcddd1"
    private final val useRequirementRef = "/reqd2b69-edb2-431f-855a-4368e2bcddd1"
    private final val resourceUrl = "$baseUri$basePath$useProjectRef$useResourceFolder"
    private final val fullUrl = "$resourceUrl$useRequirementRefPut"

    @Test
    fun getRequirement() {
        RestAssured.given()
            //.pathParam("uuid", uuid)
            .`when`()
            .get("$resourceUrl$useRequirementRef")
            .then()
            .statusCode(200)
        // .body(`is`("hello $uuid"))
    }

    @Test
    fun listRequirements() {
        RestAssured.given()
            .`when`().get(resourceUrl)
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))
    }

    @Test
    fun createRequirement() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val requirement = Requirement ()
        requirement.title = "Integrasjonstest requirement - tittel 1"
        requirement.description = "Integrasjonstest requirement - beskrivelse 1"

        RestAssured.given()
            .`when`()
            .body(requirement)
            .header("Content-type", "application/json")
            .post("$useProjectRef$useResourceFolder")
            .then()
            .statusCode(201) //envt 200
    }

    @Test
    fun updateRequirement() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val requirement = Requirement ()
        requirement.title = "Oppdatert Integrasjonstest requirement - tittel 1"
        requirement.description = "Oppdatert Integrasjonstest requirement - beskrivelse 1"

        RestAssured.given()
            .`when`()
            .body(requirement)
            .header("Content-type", "application/json")
            .put(fullUrl)
            .then()
            .statusCode(200) //envt 200
    }

    @Test
    fun deleteRequirement() {
        RestAssured.given()
            .`when`()
            .delete("$resourceUrl$useRequirementRef")
            .then()
            .statusCode(204)
        //.body(`is`("Hello RESTEasy"))
    }
}