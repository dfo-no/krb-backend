package org.kravbank.resource.real

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.*
import org.kravbank.dao.RequirementForm
import org.kravbank.domain.*
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.KeycloakAccess
import javax.inject.Inject
import javax.transaction.Transactional

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
class RequirementResourceTest {


    @Inject
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var needRepository: NeedRepository


    lateinit var token: String

    lateinit var projectRef: String

    var projectId: Long? = null

    var needId: Long? = null

    var needRef: String? = null

    lateinit var newRequirementRef: String


    private val emptyProducts: MutableList<Product> = mutableListOf()

    private val emptyPublications: MutableList<Publication> = mutableListOf()

    private val emptyRequirements: MutableList<Requirement> = mutableListOf()

    private val emptyNeeds: MutableList<Need> = mutableListOf()

    private val emptyCodelists: MutableList<Codelist> = mutableListOf()


    @BeforeAll
    @Transactional
    fun setUp() {

        token = KeycloakAccess.getAccessToken("alice")

        val useThisProject = Project().apply {
            ref = "testref-123"
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

        Need().apply {
            ref = "ytuiop-rtgsd-235235235-fdsfdsd"
            title = "Ny need tittel"
            project = useThisProject
            requirements = emptyRequirements
        }.also {
            needRepository.persistAndFlush(it)
            needRef = it.ref
            needId = it.id
        }
    }


    @Test
    @Order(1)
    fun `create then return 201 and verify ref has value`() {

        RestAssured.defaultParser = Parser.JSON

        val create = RequirementForm().apply {
            title = "Ny requirement"
            description = "Beskrivelse requirement"
        }

        val response =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .body(RequirementForm().toEntity(create))
                .header("Content-type", "application/json")
                .post("/api/v1/projects/$projectRef/needs/$needRef/requirements")

        Assertions.assertEquals(201, response.statusCode)
        Assertions.assertTrue(response.headers.hasHeaderWithName("Location"))


        val newUrlLocation = response.headers.getValue("Location")
        val newRefToUse = newUrlLocation.split("requirements/")[1]

        Assertions.assertTrue(newRefToUse.isNotEmpty())

        newRequirementRef = newRefToUse
    }


    @Order(2)
    @Test
    fun `get then return 200 and verify ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/needs/$needRef/requirements/$newRequirementRef")

        Assertions.assertEquals(200, response.statusCode())

        val entity = response.body.jsonPath().getObject("", RequirementVariant::class.java)

        Assertions.assertEquals(newRequirementRef, entity.ref)
    }

    @Test
    @Order(3)
    fun `list then return 200 and verify ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/needs/$needRef/requirements")

        Assertions.assertEquals(200, response.statusCode)

        val list = response.body.jsonPath().getList("", Requirement::class.java)

        val lastEntityInList = list.last()


        Assertions.assertEquals(newRequirementRef, lastEntityInList.ref)
    }

    @Test
    @Order(4)
    fun `update then return 200 and verify updated attributes`() {

        RestAssured.defaultParser = Parser.JSON

        val update = RequirementForm().apply {
            title = "Integrasjonstest requirement - tittel 1"
            description = "Integrasjonstest requirement - beskrivelse 1"
        }

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(RequirementForm().toEntity(update))
            .header("Content-type", "application/json")
            .put("/api/v1/projects/$projectRef/needs/$needRef/requirements/$newRequirementRef")

        Assertions.assertEquals(200, response.statusCode)


        val entity = response.body.jsonPath().getObject("", Requirement::class.java)

        Assertions.assertEquals(update.title, entity.title)
        Assertions.assertEquals(update.description, entity.description)
    }


    @Order(5)
    @Test
    fun `delete then return 200 and verify deleted ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/$projectRef/needs/$needRef/requirements/$newRequirementRef")

        Assertions.assertEquals(204, response.statusCode)
    }


    @AfterAll
    @Transactional
    fun `delete dependencies`() {

        projectRepository.deleteById(projectId)
        needRepository.deleteById(needId)
    }
}