package org.kravbank.api

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.domain.Project
import org.kravbank.utils.form.project.ProjectForm
import org.kravbank.utils.form.project.ProjectFormUpdate
import org.kravbank.utils.mapper.project.ProjectMapper
import org.kravbank.utils.mapper.project.ProjectUpdateMapper
import java.time.LocalDateTime


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

        val project = ProjectForm()
        project.title = "Oppdatert integrasjonstest - Tittel 1"
        project.description = "Oppdatert integrasjonstest - Beskrivelse 1"
        project.version = 11
        //project.deletedDate = "sadsfdsa"

        val projectMappedForm = ProjectMapper().toEntity(project)

        //project.publishedDate = LocalDateTime.now()

        given()
            .`when`()
            .body(projectMappedForm)
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

        val project = ProjectFormUpdate()
        project.title = "Oppdatert integrasjonstest - Tittel 1"
        project.description = "Oppdatert integrasjonstest - Beskrivelse 1"
        project.version = 11
        //project.deletedDate = "sadsfdsa"

        val projectMappedForm = ProjectUpdateMapper().toEntity(project)




      //  project.publishedDate = LocalDateTime.now()

        // project.publishedDate = LocalDateTime.now()

        given()
            .`when`()
            .body(projectMappedForm)
            .header("Content-type", "application/json")
            .put(useProjectRef)
            .then()
            .statusCode(200)
    }


}