package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.utils.product.dto.ProductGetDTO
import org.kravbank.utils.product.dto.ProductPostDTO
import org.kravbank.utils.product.dto.ProductPutDTO
import org.kravbank.utils.product.mapper.ProductPutMapper

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

        val postDTO = ProductPostDTO()
        postDTO.title = "PRODUCT Integrasjonstest - Tittel 1"
        postDTO.description = "PRODUCT Integrasjonstest - Beskrivelse 1"
        postDTO.requirementvariant = "rvrv2b69-edb2-431f-855a-4368e2bcddd1"

        given()
            .`when`()
            .body(postDTO)
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

        val putDTO = ProductPutDTO()
        putDTO.title = "Oppdatert integrasjonstest produkt - Tittel 1"
        putDTO.description = "Oppdatert integrasjonstest produkt - Beskrivelse 1"

        given()
            .`when`()
            .body(putDTO)
            .header("Content-type", "application/json")
            .put("/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/edb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}