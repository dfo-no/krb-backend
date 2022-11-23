package org.kravbank.resource

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProjectForm


@QuarkusTest
@TestSecurity(authorizationEnabled = false)
class ProjectResourceTest {

    @Test
    fun getProjectByRef() {
        given()
            .`when`().get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listProjects() {
        given()
            .`when`().get("http://localhost:8080/api/v1/projects")
            .then()
            .statusCode(200)
    }

    @Test
    fun createProject() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/"

        val form = ProjectForm()
        form.title = "POST integrasjonstest - Tittel 1"
        form.description = "POST integrasjonstest - Beskrivelse 1"

        val project = ProjectForm().toEntity(form)

        given()
            .`when`()
            .body(project)
            .header("Content-type", "application/json")
            .post("/projects")
            .then()
            .statusCode(201)
    }

    @Test
    fun deleteProjectByRef() {
        given()
            .`when`().delete("http://localhost:8080/api/v1/projects/ccc4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun updateProject() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects"

        val form = ProjectForm()
        form.title = "Oppdatert integrasjonstest - Tittel 1"
        form.description = "Oppdatert integrasjonstest - Beskrivelse 1"

        val project = ProjectForm().toEntity(form)

        given()
            .`when`()
            .body(project)
            .header("Content-type", "application/json")
            .put("/bbb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}