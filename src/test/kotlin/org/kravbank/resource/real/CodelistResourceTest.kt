package org.kravbank.resource

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.kravbank.dao.CodelistForm
import org.kravbank.domain.*
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.KeycloakAccess
import java.util.*
import javax.inject.Inject
import javax.transaction.Transactional


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
class CodelistResourceTest {


    @Inject
    lateinit var projectRepository: ProjectRepository


    lateinit var token: String

    lateinit var projectRef: String

    var projectId: Long? = null

    lateinit var newCodelistRef: String


    lateinit var pathToUse: String

    val classTypeToUse = Codelist::class.java

    val formTypeToUse = CodelistForm()


    private val emptyProducts: MutableList<Product> = mutableListOf()

    private val emptyPublications: MutableList<Publication> = mutableListOf()

    private val emptyRequirements: MutableList<Requirement> = mutableListOf()

    private val emptyNeeds: MutableList<Need> = mutableListOf()

    private val emptyCodelists: MutableList<Codelist> = mutableListOf()


    @BeforeAll
    @Transactional
    fun setup() {

        token = KeycloakAccess.getAccessToken("alice")

        pathToUse = "codelists"

        Project().apply {
            ref = UUID.randomUUID().toString()
            title = "Prosjekttittel1"
            description = "Prosjektbeskrivelse1"
            products = emptyProducts
            publications = emptyPublications
            requirements = emptyRequirements
            needs = emptyNeeds
            codelist = emptyCodelists
        }.run {
            projectRepository.persistAndFlush(this)
            projectRef = ref
            projectId = id
        }
    }


    @Test
    @Order(1)
    fun `create then return 201 and verify ref has value`() {

        RestAssured.defaultParser = Parser.JSON

        val createForm = formTypeToUse.apply {
            title = "Codeform from integration test"
            description = "Codeform says hello"
        }

        val response =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .body(formTypeToUse.toEntity(createForm))
                .header("Content-type", "application/json")
                .post("/api/v1/projects/$projectRef/$pathToUse")

        assertEquals(201, response.statusCode)
        assertTrue(response.headers.hasHeaderWithName("Location"))


        val newUrlLocation = response.headers.getValue("Location")
        val newRefToUse = newUrlLocation.split("$pathToUse/")[1]

        assertTrue(newRefToUse.isNotEmpty())

        newCodelistRef = newRefToUse
    }


    @Order(2)
    @Test
    fun `get then return 200 and verify ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/$pathToUse/$newCodelistRef")

        assertEquals(200, response.statusCode())


        val entity = response.body.jsonPath().getObject("", classTypeToUse)

        assertEquals(newCodelistRef, entity.ref)
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


        val listOfCodelist = response.body.jsonPath().getList("", classTypeToUse)
        val lastEntityInList = listOfCodelist.last()


        assertEquals(newCodelistRef, lastEntityInList.ref)
    }


    @Test
    @Order(4)
    fun `update then return 200 and verify updated attributes`() {

        val updateForm = formTypeToUse.apply {
            title = "Endrer tittel"
            description = "Endrer beskrivelse"
        }

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(formTypeToUse.toEntity(updateForm))
            .header("Content-type", "application/json")
            .put("/api/v1/projects/$projectRef/$pathToUse/$newCodelistRef")

        assertEquals(200, response.statusCode)


        val entity = response.body.jsonPath().getObject("", classTypeToUse)

        assertEquals(updateForm.title, entity.title)
        assertEquals(updateForm.description, entity.description)
    }


    @Order(5)
    @Test
    fun `delete then return 204 and verify deleted ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/$projectRef/$pathToUse/$newCodelistRef")

        assertEquals(204, response.statusCode)
    }


    @AfterAll
    @Transactional
    fun `delete dependency project`() {

        projectRepository.deleteById(projectId)
    }
}