package org.kravbank.resource.real

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.kravbank.dao.RequirementVariantForm
import org.kravbank.domain.*
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementRepository
import org.kravbank.utils.KeycloakAccess
import javax.inject.Inject
import javax.transaction.Transactional

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
class RequirementVariantResourceTest {


    @Inject
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var needRepository: NeedRepository

    @Inject
    lateinit var requirementRepository: RequirementRepository


    lateinit var token: String

    lateinit var projectRef: String

    var projectId: Long? = null

    var needId: Long? = null

    var needRef: String? = null

    var requirementId: Long? = null

    lateinit var requirementRef: String

    lateinit var newReqVariantRef: String


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

        val useThisNeed = Need().apply {
            ref = "ytuiop-rtgsd-235235235-fdsfdsd"
            title = "Ny need tittel"
            project = useThisProject
            requirements = emptyRequirements
        }.also {
            needRepository.persistAndFlush(it)
            needRef = it.ref
            needId = it.id
        }

        Requirement().apply {
            ref = "sadasdasf-rtgsd-235gfdgfdåpskfæpdsa235235-fdsfdsd"
            title = "Ny req tittel"
            project = useThisProject
            need = useThisNeed
        }.also {
            requirementRepository.persistAndFlush(it)
            requirementRef = it.ref
            requirementId = it.id
        }
    }


    @Test
    @Order(1)
    fun `create then return 201 and verify ref has value`() {

        val rv = RequirementVariantForm().apply {
            description = "Integrasjonstest rv desc"
            requirementText = "Integrasjonstest rv reqtext"
            instruction = "Integrasjonstest rv instruction"
            useProduct = true
            useSpecification = true
            useQualification = true
        }

        val response =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .body(RequirementVariantForm().toEntity(rv))
                .header("Content-type", "application/json")
                .post("/api/v1/projects/$projectRef/needs/$needRef/requirements/$requirementRef/requirementvariants")

        assertEquals(201, response.statusCode)
        assertTrue(response.headers.hasHeaderWithName("Location"))


        val newUrlLocation = response.headers.getValue("Location")
        val newRefToUse = newUrlLocation.split("requirementvariants/")[1]

        assertTrue(newRefToUse.isNotEmpty())

        newReqVariantRef = newRefToUse
    }


    @Order(2)
    @Test
    fun `get then return 200 and verify ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/needs/$needRef/requirements/$requirementRef/requirementvariants/$newReqVariantRef")

        assertEquals(200, response.statusCode())


        val entity = response.body.jsonPath().getObject("", RequirementVariant::class.java)

        assertEquals(newReqVariantRef, entity.ref)
    }


    @Test
    @Order(3)
    fun `list then return 200 and verify ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/needs/$needRef/requirements/$requirementRef/requirementvariants/")

        assertEquals(200, response.statusCode)


        val list = response.body.jsonPath().getList("", RequirementVariant::class.java)
        val lastEntityInList = list.last()


        assertEquals(newReqVariantRef, lastEntityInList.ref)
    }


    @Test
    @Order(4)
    fun `update then return 200 and verify updated attributes`() {

        val update = RequirementVariantForm().apply {
            description = "Integrasjonstest rv desc"
            requirementText = "Integrasjonstest rv reqtext"
            instruction = "Integrasjonstest rv instruction"
            useProduct = true
            useSpecification = true
            useQualification = true
        }

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(RequirementVariantForm().toEntity(update))
            .header("Content-type", "application/json")
            .put("/api/v1/projects/$projectRef/needs/$needRef/requirements/$requirementRef/requirementvariants/$newReqVariantRef")

        assertEquals(200, response.statusCode)


        val entity = response.body.jsonPath().getObject("", RequirementVariant::class.java)

        assertEquals(update.description, entity.description)
        assertEquals(update.requirementText, entity.requirementText)
        assertEquals(update.instruction, entity.instruction)
        assertEquals(update.useProduct, entity.useProduct)
        assertEquals(update.useSpecification, entity.useSpecification)
        assertEquals(update.useQualification, entity.useQualification)
    }


    @Order(5)
    @Test
    fun `delete then return 200 and verify deleted ref`() {

        val response = given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/$projectRef/needs/$needRef/requirements/$requirementRef/requirementvariants/$newReqVariantRef")

        assertEquals(204, response.statusCode)
    }


    @AfterAll
    @Transactional
    fun `delete dependencies`() {

        projectRepository.deleteById(projectId)
        needRepository.deleteById(needId)
        requirementRepository.deleteById(requirementId)
    }
}