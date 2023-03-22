package org.kravbank.resource.real

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.*
import org.kravbank.dao.NeedForm
import org.kravbank.domain.*
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.KeycloakAccess
import java.util.*
import javax.inject.Inject
import javax.transaction.Transactional

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
class NeedResourceTest {


    @Inject
    lateinit var projectRepository: ProjectRepository


    lateinit var token: String

    lateinit var projectRef: String

    var projectId: Long? = null

    lateinit var newNeedRef: String


    lateinit var pathToUse: String

    val classTypeToUse = Need::class.java

    val formTypeToUse = NeedForm()


    private val emptyProducts: MutableList<Product> = mutableListOf()

    private val emptyPublications: MutableList<Publication> = mutableListOf()

    private val emptyRequirements: MutableList<Requirement> = mutableListOf()

    private val emptyNeeds: MutableList<Need> = mutableListOf()

    private val emptyCodelists: MutableList<Codelist> = mutableListOf()


    @BeforeAll
    @Transactional
    fun setUp() {

        token = KeycloakAccess.getAccessToken("alice")

        pathToUse = "needs"

        Project().apply {
            ref = UUID.randomUUID().toString()
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

        val create = formTypeToUse.apply {
            title = "need - tittel 1"
            description = "need - beskrivelse 1"
        }

        val response =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .body(formTypeToUse.toEntity(create))
                .header("Content-type", "application/json")
                .post("/api/v1/projects/$projectRef/needs")

        Assertions.assertEquals(201, response.statusCode)
        Assertions.assertTrue(response.headers.hasHeaderWithName("Location"))


        val newUrlLocation = response.headers.getValue("Location")
        val newRefToUse = newUrlLocation.split("$pathToUse/")[1]

        Assertions.assertTrue(newRefToUse.isNotEmpty())

        newNeedRef = newRefToUse
    }


    @Order(2)
    @Test
    fun `get then return 200 and verify ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/$pathToUse/$newNeedRef")

        Assertions.assertEquals(200, response.statusCode())


        val entity = response.body.jsonPath().getObject("", classTypeToUse)

        Assertions.assertEquals(newNeedRef, entity.ref)
    }

    @Test
    @Order(3)
    fun `list then return 200 and verify ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/needs")

        Assertions.assertEquals(200, response.statusCode)


        val list = response.body.jsonPath().getList("", classTypeToUse)
        val lastEntityInList = list.last()


        Assertions.assertEquals(newNeedRef, lastEntityInList.ref)
    }


    @Test
    @Order(4)
    fun `update then return 200 and verify updated attributes`() {

        val update = formTypeToUse.apply {
            title = "Oppdatert 123"
            description = "Oppdatert 321"
        }

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(formTypeToUse.toEntity(update))
            .header("Content-type", "application/json")
            .put("/api/v1/projects/$projectRef/needs/$newNeedRef")

        Assertions.assertEquals(200, response.statusCode)


        val entity = response.body.jsonPath().getObject("", classTypeToUse)

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
            .delete("/api/v1/projects/$projectRef/needs/$newNeedRef")

        Assertions.assertEquals(204, response.statusCode)
    }

    @AfterAll
    @Transactional
    fun `delete dependencies`() {

        projectRepository.deleteById(projectId)
    }
}