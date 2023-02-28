package org.kravbank.resource.real

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

    lateinit var token: String

    lateinit var projectRef: String

    var projectId: Long? = null

    lateinit var newCodelistRef: String


    @Inject
    lateinit var projectRepository: ProjectRepository

    private val mockedProducts: MutableList<Product> = mutableListOf()

    private val mockedPublications: MutableList<Publication> = mutableListOf()

    private val mockedRequirements: MutableList<Requirement> = mutableListOf()

    private val mockedNeeds: MutableList<Need> = mutableListOf()

    private val mockedCodelists: MutableList<Codelist> = mutableListOf()


    @BeforeAll
    @Transactional
    fun `set up`() {

        token = KeycloakAccess.getAccessToken("alice")

        Project().apply {
            ref = "testref-123"
            title = "Prosjekttittel1"
            description = "Prosjektbeskrivelse1"
            products = mockedProducts
            publications = mockedPublications
            requirements = mockedRequirements
            needs = mockedNeeds
            codelist = mockedCodelists
        }.also {
            projectRepository.persistAndFlush(it)
            projectRef = it.ref
            projectId = it.id
        }
    }


    @Test
    @Order(1)
    fun `create new codelist`() {

        RestAssured.defaultParser = Parser.JSON

        val createForm = CodelistForm().apply {
            title = "Codeform from integration test"
            description = "Codeform says hello"
        }

        val response =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .body(CodelistForm().toEntity(createForm))
                .header("Content-type", "application/json")
                .post("/api/v1/projects/$projectRef/codelists")

        assertEquals(201, response.statusCode)
        assertTrue(response.headers.hasHeaderWithName("Location"))


        val newUrlLocation = response.headers.getValue("Location")
        val newRefToUse = newUrlLocation.split("codelists/")[1]

        assertTrue(newRefToUse.isNotEmpty())

        newCodelistRef = newRefToUse
    }


    @Order(2)
    @Test
    fun `get codelist`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/codelists/$newCodelistRef")

        assertEquals(200, response.statusCode())


        val entity = response.body.jsonPath().getObject("", Codelist::class.java)

        assertEquals(newCodelistRef, entity.ref)
    }


    @Test
    @Order(3)
    fun `list codelists`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/codelists/")

        assertEquals(200, response.statusCode)


        val listOfCodelist = response.body.jsonPath().getList("", Codelist::class.java)
        val lastEntityInList = listOfCodelist.last()


        assertEquals(newCodelistRef, lastEntityInList.ref)
    }


    @Test
    @Order(4)
    fun `update codelist`() {

        val updateForm = CodelistForm().apply {
            title = "Endrer tittel"
            description = "Endrer beskrivelse"
        }

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(CodelistForm().toEntity(updateForm))
            .header("Content-type", "application/json")
            .put("/api/v1/projects/$projectRef/codelists/$newCodelistRef")

        assertEquals(200, response.statusCode)


        val entity = response.body.jsonPath().getObject("", Codelist::class.java)


        assertEquals(newCodelistRef, entity.ref)
        assertEquals(updateForm.title, entity.title)
        assertEquals(updateForm.description, entity.description)
    }


    @Order(5)
    @Test
    fun `delete codelist`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/$projectRef/codelists/$newCodelistRef")

        assertEquals(200, response.statusCode)


        val deletedRefConfirm = response.body.asPrettyString()
        assertEquals(newCodelistRef, deletedRefConfirm)
    }

    @AfterAll
    @Transactional
    fun `delete dependency project`() {

        projectRepository.deleteById(projectId)
    }

}