package org.kravbank.api

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.domain.Project

@QuarkusTest
@QuarkusIntegrationTest

class ProjectResourceTest {

    val baseUri = "http://localhost:8080"
    val basePath = "/api/v1/projects"
    val useProjectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"


    @Test
    fun getProjectByRef() {
        given()
            //.pathParam("uuid", uuid)
            .`when`().get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
        // .body(`is`("hello $uuid"))
    }

    @Test
    fun listProjects() {
        given()
            .`when`().get("http://localhost:8080/api/v1/projects")
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))
    }

    @Test
    fun createProject() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080";
        //RestAssured.port = 8080;
        RestAssured.basePath = "/api/v1/";

        val project = Project()
        project.title = "Integrasjonstest prosjektittel"
        project.description = "Integrasjonstest prosjektbeskrivelse"
        //project.projectId = UUID.randomUUID().toString()
        project.version = "1.0"
        project.publishedDate = "Not published"
        project.deletedDate = "11-11-11"

        given()
            .`when`()
            .body(project)
            .header("Content-type", "application/json")
            .post("/projects")
            .then()
            .statusCode(201) //envt 200

        // RestAssured.reset()
        //body(both(startsWith("")).and(not(endsWith("null"))))
    }

    @Test
    fun deleteProjectByRef() {
        given()
            .`when`().delete("http://localhost:8080/api/v1/projects/ccc4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(204)
    }

    @Test
    fun updateProjectByRef() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        RestAssured.basePath = basePath;

        val project = Project()
        project.title = "Oppdatert integrasjonstest - Tittel 1"
        project.description = "Oppdatert integrasjonstest - Beskrivelse 1"

        given()
            .`when`()
            .body(project)
            .header("Content-type", "application/json")
            .put(useProjectRef)
            .then()
            .statusCode(200)
    }
}