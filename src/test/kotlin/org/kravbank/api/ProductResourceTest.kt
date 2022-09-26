package org.kravbank.api

import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.kravbank.domain.Codelist

internal class ProductResourceTest {
    val baseUri = "http://localhost:8080"
    val basePath = "/api/v1/projects"
    val useProjectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"


    @Test
    fun getProjectByRef() {
        RestAssured.given()
            //.pathParam("uuid", uuid)
            .`when`().get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/kuk4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
        // .body(`is`("hello $uuid"))
    }

    @Test
    fun listProducts() {
        RestAssured.given()
            .`when`().get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/")
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))

    }

    @Test
    fun createProduct() {

        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val codelist = Codelist()
        codelist.title = "Integrasjonstest - Tittel 1"
        codelist.description = "Integrasjonstest - Beskrivelse 1"

        RestAssured.given()
            .`when`()
            .body(codelist)
            .header("Content-type", "application/json")
            .post("$useProjectRef/codelists")
            .then()
            .statusCode(201) //envt 200
    }


    @Test
    fun deleteProdudctById() {
    }

    @Test
    fun updateProduct() {
    }

    @Test
    fun getProductService() {
    }

    @Test
    fun getProjectService() {
    }
}