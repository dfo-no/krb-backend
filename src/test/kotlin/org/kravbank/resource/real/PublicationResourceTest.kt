package org.kravbank.resource.real

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
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
import org.kravbank.dao.PublicationForm
import org.kravbank.domain.DeletedRecord
import org.kravbank.domain.Publication
import org.kravbank.utils.KeycloakAccess
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class
)
@QuarkusTest
class PublicationResourceTest {

    private val token = KeycloakAccess.getAccessToken("alice")

    @Inject
    lateinit var em: EntityManager


    @Test
    @Order(1)
    fun `get one publication`() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications/xxx4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }


    @Test
    @Order(2)
    fun `list all publications`() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications")
            .then()
            .statusCode(200)
    }


    @Test
    @Order(3)
    fun `create a new publication`() {
        RestAssured.defaultParser = Parser.JSON
        val form = PublicationForm()
        form.comment = "Integrasjonstest publication - comment 1"
        form.version = 3

        val publication = PublicationForm().toEntity(form)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(publication)
            .header("Content-type", "application/json")
            .post("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications")
            .then()
            .statusCode(201)
    }


    @Test
    @Order(4)
    fun `delete publication and verify delete record`() {

        //list publications
        var listPublicationsResponse =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications/")

        assertEquals(200, listPublicationsResponse.statusCode())

        val publicationList = listPublicationsResponse.body.jsonPath().getList("", Publication::class.java)
        val oldPublicationListLength = publicationList.size

        val publicationToDelete = publicationList.last()


        // list existing soft-deleted records
        val deletedRecordQuery: TypedQuery<DeletedRecord> =
            em.createNamedQuery("selectDeletedRecords", DeletedRecord::class.java)

        val numberOfDeletedRecordsBeforeTest = deletedRecordQuery.resultList.size


        // Delete action...
        val delete = given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications/${publicationToDelete.ref}")

        assertEquals(204, delete.statusCode)

        // Verify we have one more soft-deleted record
        val listDeletedRecordsAfterTest = deletedRecordQuery.resultList

        assertEquals(numberOfDeletedRecordsBeforeTest + 1, listDeletedRecordsAfterTest.size)


        // Verify the deleted publication is gone
        listPublicationsResponse =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications/")

        assertEquals(204, listPublicationsResponse.statusCode())


        val newPublicationListLength = listPublicationsResponse.body.jsonPath().getInt("data.size()")

        assertEquals(oldPublicationListLength - 1, newPublicationListLength)


        // Verify the deleted publication can be deserialized to a representation of the orginal publication
        val mapper = jacksonObjectMapper()
        mapper.registerModule(JavaTimeModule())

        val deserializeThisPublication = listDeletedRecordsAfterTest.last()
        val publicationEntity = mapper.readValue(deserializeThisPublication.data, Publication::class.java)

        assertEquals(publicationToDelete.ref, publicationEntity.ref)
        assertEquals(publicationToDelete.date, publicationEntity.date)
        assertEquals(publicationToDelete.version, publicationEntity.version)
        assertEquals(publicationToDelete.comment, publicationEntity.comment)
    }


    @Test
    @Order(5)
    fun `update existing publication`() {
        RestAssured.defaultParser = Parser.JSON

        val form = PublicationForm()
        form.comment = "Oppdaterer --->>>> Integrasjonstest publication - comment 1"
        form.version = 111

        val publication = PublicationForm().toEntity(form)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(publication)
            .header("Content-type", "application/json")
            .put("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/publications/zzz4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}