package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.utils.form.product.ProductFormCreate
import org.kravbank.utils.form.product.ProductFormUpdate
import org.kravbank.utils.mapper.product.ProductUpdateMapper

@QuarkusTest
@QuarkusIntegrationTest
class ProductResourceTest {

    @Test
    fun getProject() {
        given()
            .`when`()
            .get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/kuk4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listProducts() {
        given()
            .`when`().get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/")
            .then()
            .statusCode(200)
    }

    @Test
    fun createProduct() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects"

        val productDTO = ProductFormCreate()
        productDTO.title = "PRODUCT Integrasjonstest - Tittel 1"
        productDTO.description = "PRODUCT Integrasjonstest - Beskrivelse 1"
        productDTO.requirementvariant = "rvrv2b69-edb2-431f-855a-4368e2bcddd1"

        given()
            .`when`()
            .body(productDTO)
            .header("Content-type", "application/json")
            .post("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products")
            .then()
            .statusCode(201)
    }

    @Test
    fun deleteProdudct() {
        given()
            .`when`()
            .delete("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/edb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun updateProduct() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects"

        val product = ProductFormUpdate()
        product.title = "Oppdatert integrasjonstest produkt - Tittel 1"
        product.description = "Oppdatert integrasjonstest produkt - Beskrivelse 1"
        val productMapper = ProductUpdateMapper().toEntity(product)

        given()
            .`when`()
            .body(productMapper)
            .header("Content-type", "application/json")
            .put("/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/edb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}