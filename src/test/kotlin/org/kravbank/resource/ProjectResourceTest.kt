package org.kravbank.resource

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProjectForm
import org.kravbank.utils.KeycloakAccess

@QuarkusTest
class ProjectResourceTest {

    val token = KeycloakAccess.getAccessToken("alice")


    @Test
    fun getProjectByRef() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listProjects() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects")
            .then()
            .statusCode(200)
    }

    @Test
    fun createProject() {
        RestAssured.defaultParser = Parser.JSON
        val form = ProjectForm()
        form.title = "POST integrasjonstest - Tittel 1"
        form.description = "POST integrasjonstest - Beskrivelse 1"

        val project = ProjectForm().toEntity(form)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(project)
            .header("Content-type", "application/json")
            .post("/api/v1/projects")
            .then()
            .statusCode(201)
    }

    @Test
    fun deleteProjectByRef() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/ccc4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun updateProject() {
        RestAssured.defaultParser = Parser.JSON
        val form = ProjectForm()
        form.title = "Oppdatert integrasjonstest - Tittel 1"
        form.description = "Oppdatert integrasjonstest - Beskrivelse 1"
        val project = ProjectForm().toEntity(form)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(project)
            .header("Content-type", "application/json")
            .put("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}