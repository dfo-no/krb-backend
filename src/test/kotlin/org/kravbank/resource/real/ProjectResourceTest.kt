package org.kravbank.resource.real

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.kravbank.dao.ProjectForm
import org.kravbank.domain.DeletedRecord
import org.kravbank.domain.Project
import org.kravbank.utils.KeycloakAccess
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
class ProjectResourceTest {


    @Inject
    lateinit var em: EntityManager


    lateinit var token: String

    lateinit var newProjectRef: String

    lateinit var urlLocationFromHeaderToUse: String

    var classTypeToUse = Project::class.java

    val formTypeToUse = ProjectForm()


    @BeforeAll
    fun setUp() {

        token = KeycloakAccess.getAccessToken("alice")

        urlLocationFromHeaderToUse = "projects"
    }


    @Test
    @Order(1)
    fun `create then return 201 and verify ref has value`() {

        val create = ProjectForm().apply {
            title = "Nytt prosjekt"
            description = "Beskrivelse prosjekt"
        }

        val response =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .body(formTypeToUse.toEntity(create))
                .header("Content-type", "application/json")
                .post("/api/v1/projects")

        assertEquals(201, response.statusCode)
        assertTrue(response.headers.hasHeaderWithName("Location"))


        val newUrlLocation = response.headers.getValue("Location")
        val newRefToUse = newUrlLocation.split("$urlLocationFromHeaderToUse/")[1]

        assertTrue(newRefToUse.isNotEmpty())

        newProjectRef = newRefToUse
    }


    @Order(2)
    @Test
    fun `get then return 200 and verify ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$newProjectRef")

        assertEquals(200, response.statusCode())

        val entity = response.body.jsonPath().getObject("", classTypeToUse)

        assertEquals(newProjectRef, entity.ref)
    }


    @Test
    @Order(3)
    fun `list then return 200 and verify ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects")

        assertEquals(200, response.statusCode)


        val list = response.body.jsonPath().getList("", classTypeToUse)

        val lastEntityInList = list.last()


        assertEquals(newProjectRef, lastEntityInList.ref)
    }


    @Test
    @Order(4)
    fun `update then return 200 and verify updated attributes`() {

        RestAssured.defaultParser = Parser.JSON

        val update = formTypeToUse.apply {
            title = "Oppdatere integrasjonstest project - tittel 2"
            description = "Oppdatere integrasjonstest project - beskrivelse 2"
        }


        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(formTypeToUse.toEntity(update))
            .header("Content-type", "application/json")
            .put("/api/v1/projects/$newProjectRef")

        assertEquals(200, response.statusCode)

        val entity = response.body.jsonPath().getObject("", classTypeToUse)

        assertEquals(update.title, entity.title)
        assertEquals(update.description, entity.description)
    }


    @Test
    @Order(5)
    fun `delete project and verify delete record`() {

        //list project
        var listProjectResponse =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects")


        assertEquals(200, listProjectResponse.statusCode())

        val projectList = listProjectResponse.body.jsonPath().getList("", classTypeToUse)
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
                .get("/api/v1/projects")

        assertEquals(200, listProjectResponse.statusCode())


        val newProjectListLength = listProjectResponse.body.jsonPath().getInt("data.size()")

        assertEquals(oldProjectListLength - 1, newProjectListLength)


        // Verify the deleted project can be deserialized to a representation of the orginal project
        val mapper = jacksonObjectMapper()
        val deserializeThisProject = listDeletedRecordsAfterTest.last()
        val productEntity = mapper.readValue(deserializeThisProject.data, classTypeToUse)

        assertEquals(projectToDelete.ref, productEntity.ref)
        assertEquals(projectToDelete.title, productEntity.title)
        assertEquals(projectToDelete.description, productEntity.description)
    }
}