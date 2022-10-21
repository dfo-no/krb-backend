package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.utils.project.dto.ProjectForm
import org.kravbank.utils.project.dto.ProjectFormUpdate
import org.kravbank.utils.project.mapper.ProjectMapper
import org.kravbank.utils.project.mapper.ProjectUpdateMapper


@QuarkusTest
@QuarkusIntegrationTest
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
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/api/v1/";

        val project = ProjectForm()
        project.title = "Oppdatert integrasjonstest - Tittel 1"
        project.description = "Oppdatert integrasjonstest - Beskrivelse 1"
        val projectMappedForm = ProjectMapper().toEntity(project)

        given()
            .`when`()
            .body(projectMappedForm)
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
        RestAssured.basePath = "/api/v1/projects";

        val project = ProjectFormUpdate()
        project.title = "Oppdatert integrasjonstest - Tittel 1"
        project.description = "Oppdatert integrasjonstest - Beskrivelse 1"

        val projectMappedForm = ProjectUpdateMapper().toEntity(project)
        given()
            .`when`()
            .body(projectMappedForm)
            .header("Content-type", "application/json")
            .put("/bbb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}