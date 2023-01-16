package org.kravbank.resource

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
import org.kravbank.dao.ProductForm
import org.kravbank.domain.DeleteRecord
import org.kravbank.domain.Product
import org.kravbank.utils.KeycloakAccess
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class
)
@QuarkusTest
class ProductResourceTest {

    val token = KeycloakAccess.getAccessToken("alice")

    @Inject
    lateinit var em: EntityManager


    @Test
    @Order(1)
    fun `get one product`() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/kuk4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    @Order(2)
    fun `list all products`() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/")
            .then()
            .statusCode(200)
    }


    @Test
    @Order(3)
    fun `create a new product`() {

        RestAssured.defaultParser = Parser.JSON

        val form = ProductForm()
        form.title = "PRODUCT Integrasjonstest - Tittel 1"
        form.description = "PRODUCT Integrasjonstest - Beskrivelse 1"
        form.requirementVariantRef = "rvrv2b69-edb2-431f-855a-4368e2bcddd1"

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(form)
            .header("Content-type", "application/json")
            .post("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products")
            .then()
            .statusCode(201)
    }


    @Test
    @Order(4)
    fun `update existing product`() {
        RestAssured.defaultParser = Parser.JSON
        val form = ProductForm()
        form.title = "PUT Integrasjonstest - Tittel 1"
        form.description = "PUT Integrasjonstest - Beskrivelse 1"
        val product = ProductForm().toEntity(form)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(product)
            .header("Content-type", "application/json")
            .put("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/edb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }


    @Test
    @Order(5)
    fun `delete product and verify delete record`() {

        //list products
        var listProductsResponse =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/")

        val formatProductList = listProductsResponse.body.jsonPath().getList("", Product::class.java)
        val oldProductListLength = formatProductList.size
        assertEquals(200, listProductsResponse.statusCode())

        val deleteThisProduct = formatProductList[0]


        // list existing delete records
        val namedSelectQuery = "selectDeletedRecords"
        val deleteRecordQuery: TypedQuery<DeleteRecord> =
            em.createNamedQuery(namedSelectQuery, DeleteRecord::class.java)
        val listDeletedRecordsBeforeTest =
            deleteRecordQuery.resultList // as MutableList<DeleteRecord> //the named query in DeleteRecord is returning resultClass DeleteRecord
        val oldDeletedRecordsListLength = listDeletedRecordsBeforeTest.size

        // Delete action...
        val delete = given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/${deleteThisProduct.ref}")

        assertEquals(200, delete.statusCode)

        //check delete record +1
        // deleteRecordQuery = em.createNamedQuery(namedSelectQuery, DeleteRecord::class.java)
        val listDeletedRecordsAfterTest =
            deleteRecordQuery.resultList// as MutableList<DeleteRecord> //the named query in DeleteRecord is returning resultClass DeleteRecord

        assertEquals(oldDeletedRecordsListLength + 1, listDeletedRecordsAfterTest.size)


        //check product list -1
        listProductsResponse =
            given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/")

        assertEquals(200, listProductsResponse.statusCode())
        val newProductListLength = listProductsResponse.body.jsonPath().getInt("data.size()")
        assertEquals(oldProductListLength - 1, newProductListLength)


        // verify deleted product
        val mapper = jacksonObjectMapper()
        val deserializeThisProduct = listDeletedRecordsAfterTest[listDeletedRecordsAfterTest.size - 1]
        val productEntity = mapper.readValue(deserializeThisProduct.data, Product::class.java)

        assertEquals(deleteThisProduct.ref, productEntity.ref)
        assertEquals(deleteThisProduct.title, productEntity.title)
        assertEquals(deleteThisProduct.description, productEntity.description)
    }
}
