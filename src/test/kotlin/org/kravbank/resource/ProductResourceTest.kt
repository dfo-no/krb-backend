package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProductForm

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

        val form = ProductForm()
        form.title = "PRODUCT Integrasjonstest - Tittel 1"
        form.description = "PRODUCT Integrasjonstest - Beskrivelse 1"
        form.requirementVariantRef = "rvrv2b69-edb2-431f-855a-4368e2bcddd1"

        given()
            .`when`()
            .body(form)
            .header("Content-type", "application/json")
            .post("/bbb4db69-edb2-431f-855a-4368e2bcddd1/products")
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

        val form = ProductForm()
        form.title = "PUT Integrasjonstest - Tittel 1"
        form.description = "PUT Integrasjonstest - Beskrivelse 1"

        val product = ProductForm().toEntity(form)

        given()
            .`when`()
            .body(product)
            .header("Content-type", "application/json")
            .put("/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/edb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}