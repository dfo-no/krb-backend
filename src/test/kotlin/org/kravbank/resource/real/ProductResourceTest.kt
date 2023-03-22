package org.kravbank.resource.real

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.kravbank.dao.ProductForm
import org.kravbank.domain.*
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.KeycloakAccess
import java.util.*
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.transaction.Transactional


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
class ProductResourceTest {


    @Inject
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var em: EntityManager


    lateinit var token: String

    var projectId: Long? = null

    lateinit var projectRef: String

    lateinit var newProductRef: String

    lateinit var pathToUse: String


    val classTypeToUse = Product::class.java

    val formTypeToUse = ProductForm()


    private val emptyProducts: MutableList<Product> = mutableListOf()

    private val emptyPublications: MutableList<Publication> = mutableListOf()

    private val emptyRequirements: MutableList<Requirement> = mutableListOf()

    private val emptyNeeds: MutableList<Need> = mutableListOf()

    private val emptyCodelists: MutableList<Codelist> = mutableListOf()


    @BeforeAll
    @Transactional
    fun setUp() {

        token = KeycloakAccess.getAccessToken("bob")

        pathToUse = "products"

        Project().apply {
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
    }


    @Test
    @Order(1)
    fun `create then return 201 and verify ref has value`() {

        val create = formTypeToUse.apply {
            title = "PRODUCT Integrasjonstest - Tittel 1"
            description = "PRODUCT Integrasjonstest - Beskrivelse 1"
        }

        val response = RestAssured.given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(formTypeToUse.toEntity(create))
            .header("Content-type", "application/json")
            .post("/api/v1/projects/$projectRef/$pathToUse")

        assertEquals(201, response.statusCode)
        Assertions.assertTrue(response.headers.hasHeaderWithName("Location"))

        val newUrlLocation = response.headers.getValue("Location")
        val createdRef = newUrlLocation.split("$pathToUse/")[1]
        Assertions.assertTrue(createdRef.isNotEmpty())

        newProductRef = createdRef
    }


    @Order(2)
    @Test
    fun `get then return 200 and verify ref`() {

        val response = RestAssured.given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/products/$newProductRef")


        println(response.body.peek())
        assertEquals(200, response.statusCode)

        val entity = response.body.jsonPath().getObject("", classTypeToUse)

        assertEquals(newProductRef, entity.ref)
    }


    @Test
    @Order(3)
    fun `list then return 200 and verify ref`() {

        val response = RestAssured.given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/$projectRef/$pathToUse")

        assertEquals(200, response.statusCode)


        val list = response.body.jsonPath().getList("", classTypeToUse)

        val lastEntityInList = list.last()


        assertEquals(newProductRef, lastEntityInList.ref)
    }


    @Test
    @Order(4)
    fun `update then return 200 and verify updated attributes`() {

        val update = formTypeToUse.apply {
            title = "oppdatert 123"
            description = "oppdatert 321"
        }
        val response = RestAssured.given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(formTypeToUse.toEntity(update))
            .header("Content-type", "application/json")
            .put("/api/v1/projects/$projectRef/$pathToUse/$newProductRef")

        assertEquals(200, response.statusCode)


        val entity = response.body.jsonPath().getObject("", classTypeToUse)


        assertEquals(update.title, entity.title)
        assertEquals(update.description, entity.description)
    }


    @Order(5)
    @Test
    fun `delete then return 200 and verify deleted ref`() {

        var listProductsResponse =
            RestAssured.given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects/$projectRef/$pathToUse")


        assertEquals(200, listProductsResponse.statusCode())

        val productList = listProductsResponse.body.jsonPath().getList("", classTypeToUse)
        val oldProductListLength = productList.size

        val productToDelete = productList.last()

        // list existing soft-deleted records
        val deletedRecordQuery: TypedQuery<DeletedRecord> =
            em.createNamedQuery("selectDeletedRecords", DeletedRecord::class.java)

        val numberOfDeletedRecordsBeforeTest = deletedRecordQuery.resultList.size

        val delete = RestAssured.given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/$projectRef/$pathToUse/$newProductRef")

        assertEquals(204, delete.statusCode)

        // Verify we have one more soft-deleted record
        val listDeletedRecordsAfterTest = deletedRecordQuery.resultList

        assertEquals(numberOfDeletedRecordsBeforeTest + 1, listDeletedRecordsAfterTest.size)


        // Verify the deleted product is gone
        listProductsResponse =
            RestAssured.given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects/$projectRef/$pathToUse")

        assertEquals(200, listProductsResponse.statusCode())


        val newProductListLength = listProductsResponse.body.jsonPath().getInt("data.size()")

        assertEquals(oldProductListLength - 1, newProductListLength)


        // Verify the deleted product can be deserialized to a representation of the orginal product
        val mapper = jacksonObjectMapper()
        val deserializeThisProduct = listDeletedRecordsAfterTest.last()
        val productEntity = mapper.readValue(deserializeThisProduct.data, classTypeToUse)

        assertEquals(productToDelete.ref, productEntity.ref)
        assertEquals(productToDelete.title, productEntity.title)
        assertEquals(productToDelete.description, productEntity.description)
    }


    @AfterAll
    @Transactional
    fun `delete dependencies`() {

        projectRepository.deleteById(projectId)
    }
}