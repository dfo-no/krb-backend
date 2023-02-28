package org.kravbank.resource.real

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.kravbank.dao.ProjectForm
import org.kravbank.domain.DeletedRecord
import org.kravbank.domain.Project
import org.kravbank.utils.KeycloakAccess
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class
)
@QuarkusTest
class ProjectResourceTest {

    private val token = KeycloakAccess.getAccessToken("alice")

    @Inject
    lateinit var em: EntityManager


    @Test
    @Order(1)
    fun `get one project`() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }


    @Test
    @Order(2)
    fun `list all projects`() {
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
    // @TestTransaction
    @Order(4)
    fun `delete project and verify delete record`() {

        //list project
        var listProjectResponse =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects/")


        assertEquals(200, listProjectResponse.statusCode())

        val projectList = listProjectResponse.body.jsonPath().getList("", Project::class.java)
        val oldProjectListLength = projectList.size

        val projectToDelete = projectList.last()


        // list existing soft-deleted records
        val deletedRecordQuery: TypedQuery<DeletedRecord> =
            em.createNamedQuery("selectDeletedRecords", DeletedRecord::class.java)

        val numberOfDeletedRecordsBeforeTest = deletedRecordQuery.resultList.size

        // Delete action...
        val delete = given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/${projectToDelete.ref}")

        assertEquals(204, delete.statusCode)

        // Verify we have one more soft-deleted record
        val listDeletedRecordsAfterTest = deletedRecordQuery.resultList


        assertEquals(numberOfDeletedRecordsBeforeTest + 1, listDeletedRecordsAfterTest.size)


        // Verify the deleted product is gone
        listProjectResponse =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects/")

        assertEquals(200, listProjectResponse.statusCode())


        val newProjectListLength = listProjectResponse.body.jsonPath().getInt("data.size()")

        assertEquals(oldProjectListLength - 1, newProjectListLength)


        // Verify the deleted project can be deserialized to a representation of the orginal project
        val mapper = jacksonObjectMapper()
        val deserializeThisProject = listDeletedRecordsAfterTest.last()
        val productEntity = mapper.readValue(deserializeThisProject.data, Project::class.java)

        assertEquals(projectToDelete.ref, productEntity.ref)
        assertEquals(projectToDelete.title, productEntity.title)
        assertEquals(projectToDelete.description, productEntity.description)
    }


    @Test
    @Order(5)
    fun `update existing product`() {
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