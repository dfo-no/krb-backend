package org.kravbank.resource.real

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.kravbank.dao.PublicationForm
import org.kravbank.domain.*
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.KeycloakAccess
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.transaction.Transactional

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
class PublicationResourceTest {


    @Inject
    lateinit var em: EntityManager

    @Inject
    lateinit var projectRepository: ProjectRepository


    lateinit var token: String

    lateinit var projectRef: String

    lateinit var newPublicationRef: String

    var projectId: Long? = null

    lateinit var pathToUse: String

    val classTypeToUse = Publication::class.java

    val formTypeToUse = PublicationForm()

    private val emptyProducts: MutableList<Product> = mutableListOf()

    private val emptyPublications: MutableList<Publication> = mutableListOf()

    private val emptyRequirements: MutableList<Requirement> = mutableListOf()

    private val emptyNeeds: MutableList<Need> = mutableListOf()

    private val emptyCodelists: MutableList<Codelist> = mutableListOf()


    @BeforeAll
    @Transactional
    fun setUp() {

        token = KeycloakAccess.getAccessToken("alice")

        pathToUse = "publications"

        Project().apply {
            ref = "testdfsdfsdfsdfref-123"
            title = "Prosjekttittel1"
            description = "Prosjektbeskrivelse1"
            products = emptyProducts
            publications = emptyPublications
            requirements = emptyRequirements
            needs = emptyNeeds
            codelist = emptyCodelists
        }.also {
            projectRepository.persistAndFlush(it)
            projectRef = it.ref
            projectId = it.id
        }
    }


    @Test
    @Order(1)
    fun `create then return 201 and verify ref has value`() {

        RestAssured.defaultParser = Parser.JSON

        val create = formTypeToUse.apply {
            comment = "En krav kommentar"
        }

        val response =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .body(formTypeToUse.toEntity(create))
                .header("Content-type", "application/json")
                .post("/api/v1/projects/$projectRef/$pathToUse")

        assertEquals(201, response.statusCode)
        assertTrue(response.headers.hasHeaderWithName("Location"))


        val newUrlLocation = response.headers.getValue("Location")
        val newRef = newUrlLocation.split("$pathToUse/")[1]

        assertTrue(newRef.isNotEmpty())

        newPublicationRef = newRef
    }

    @Order(2)
    @Test
    fun `get then return 200 and verify ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/$pathToUse/$newPublicationRef")

        assertEquals(200, response.statusCode())

        val entity = response.body.jsonPath().getObject("", classTypeToUse)

        assertEquals(newPublicationRef, entity.ref)
    }

    @Test
    @Order(3)
    fun `list then return 200 and verify ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/$pathToUse")

        assertEquals(200, response.statusCode)


        val list = response.body.jsonPath().getList("", classTypeToUse)

        val lastEntityInList = list.last()


        assertEquals(newPublicationRef, lastEntityInList.ref)
    }

    @Test
    @Order(4)
    fun `update then return 200 and verify updated attributes`() {

        RestAssured.defaultParser = Parser.JSON

        val update = formTypeToUse.apply {
            comment = "Oppdatert kommentar"
        }

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(formTypeToUse.toEntity(update))
            .header("Content-type", "application/json")
            .put("/api/v1/projects/$projectRef/$pathToUse/$newPublicationRef")

        assertEquals(200, response.statusCode)


        val entity = response.body.jsonPath().getObject("", classTypeToUse)

        assertEquals(update.comment, entity.comment)
    }


    @Test
    @Order(5)
    fun `delete and verify delete record`() {

        //list publications
        var listPublicationsResponse =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects/$projectRef/$pathToUse/$newPublicationRef")

        assertEquals(200, listPublicationsResponse.statusCode())


        println(listPublicationsResponse.body.peek())

        val publicationList = listPublicationsResponse.body.jsonPath().getList("", classTypeToUse)
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
            .delete("/api/v1/projects/$projectRef/$pathToUse/${publicationToDelete.ref}")

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
                .get("/api/v1/projects/$projectRef/$pathToUse/$newPublicationRef")

        assertEquals(200, listPublicationsResponse.statusCode())


        val newPublicationListLength = listPublicationsResponse.body.jsonPath().getInt("data.size()")

        assertEquals(oldPublicationListLength - 1, newPublicationListLength)


        // Verify the deleted publication can be deserialized to a representation of the orginal publication
        val mapper = jacksonObjectMapper()
        mapper.registerModule(JavaTimeModule())

        val deserializeThisPublication = listDeletedRecordsAfterTest.last()
        val publicationEntity = mapper.readValue(deserializeThisPublication.data, classTypeToUse)

        assertEquals(publicationToDelete.ref, publicationEntity.ref)
        assertEquals(publicationToDelete.comment, publicationEntity.comment)
    }


    @AfterAll
    @Transactional
    fun `delete dependencies`() {

        projectRepository.deleteById(projectId)
    }
}