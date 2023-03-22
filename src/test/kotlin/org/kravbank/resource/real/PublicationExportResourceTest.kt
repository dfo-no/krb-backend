package org.kravbank.resource.real

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.kravbank.domain.*
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationRepository
import org.kravbank.utils.KeycloakAccess
import java.util.*
import javax.inject.Inject
import javax.transaction.Transactional

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
class PublicationExportResourceTest {


    @Inject
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var publicationRepository: PublicationRepository


    lateinit var token: String

    var projectId: Long? = null

    lateinit var projectRef: String


    lateinit var publicationRef: String

    var publicationId: Long? = null


    lateinit var newExportRef: String

    lateinit var pathToUse: String

    val classTypeToUse = PublicationExport::class.java


    private val emptyProducts: MutableList<Product> = mutableListOf()

    private val emptyPublications: MutableList<Publication> = mutableListOf()

    private val emptyRequirements: MutableList<Requirement> = mutableListOf()

    private val emptyNeeds: MutableList<Need> = mutableListOf()

    private val emptyCodelists: MutableList<Codelist> = mutableListOf()


    @BeforeAll
    @Transactional
    fun setUp() {

        token = KeycloakAccess.getAccessToken("alice")

        pathToUse = "publicationexports"

        val useThisProject = Project().apply {
            ref = UUID.randomUUID().toString()
            title = "ABC"
            description = "CBA"
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

        Publication().apply {
            ref = UUID.randomUUID().toString()
            comment = "Ny need tittel"
            project = useThisProject
            // var date: LocalDateTime = LocalDateTime.now() TODO unit test
            // var version: Long = 0 TODO unit test
            var publicationExportRef: String? = null
        }.also {
            publicationRepository.persistAndFlush(it)
            publicationRef = it.ref
            publicationId = it.id
        }
    }


    @Test
    @Order(1)
    fun `create then return 201 and verify ref has value`() {

        RestAssured.defaultParser = Parser.JSON

        val response = RestAssured.given()
            .auth()
            .oauth2(token)
            .`when`()
            .header("Content-type", "application/json")
            .post("/api/v1/projects/$projectRef/publications/$publicationRef/publicationexports")

        assertEquals(201, response.statusCode)
        assertTrue(response.headers.hasHeaderWithName("Location"))

        val newUrlLocation = response.headers.getValue("Location")
        val createdRef = newUrlLocation.split("$pathToUse/")[1]
        assertTrue(createdRef.isNotEmpty())

        newExportRef = createdRef

        println(newExportRef)

    }

    @Test
    @Order(2)
    fun `get then return 200 and verify ref`() {

        println(newExportRef)
        RestAssured.defaultParser = Parser.JSON

        val response = RestAssured.given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/publications/$publicationRef/publicationexports/$newExportRef")

        assertEquals(200, response.statusCode())

        val entity = response.body.jsonPath().getObject("", classTypeToUse)

        assertEquals(newExportRef, entity.ref)
    }
}