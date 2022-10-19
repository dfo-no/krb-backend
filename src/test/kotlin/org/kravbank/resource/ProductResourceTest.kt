package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.utils.form.product.ProductFormCreate
import org.kravbank.utils.form.product.ProductFormUpdate
import org.kravbank.utils.mapper.product.ProductUpdateMapper

@QuarkusTest
@QuarkusIntegrationTest
class ProductResourceTest {


    val baseUri = "http://localhost:8080"
    val basePath = "/api/v1/projects"
    val useProjectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
    val useReqVariantRef = "rvrv2b69-edb2-431f-855a-4368e2bcddd1"


   // @Inject
    //lateinit var productService: ProductService


    @Test
    fun getProjectByRef() {
        RestAssured.given()
            //.pathParam("uuid", uuid)
            .`when`()
            .get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/kuk4db69-edb2-431f-855a-4368e2bcddd1")
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

        val productDTO = ProductFormCreate()
        productDTO.title = "PRODUCT Integrasjonstest - Tittel 1"
        productDTO.description = "PRODUCT Integrasjonstest - Beskrivelse 1"
        productDTO.requirementvariant = "rvrv2b69-edb2-431f-855a-4368e2bcddd1"

       // val product = ProductCreateMapper(productDTO.requirementvariant).toEntity(productDTO)


        //val prod = productService.create(useProjectRef, productDTO)


        RestAssured.given()
            .`when`()
            .body(productDTO)
            .header("Content-type", "application/json")
            .post("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products")
            //.post("$useProjectRef/products")
            .then()
            .statusCode(201)
    }



    @Test
    fun deleteProdudctById() {
        RestAssured.given()
            .`when`()
            .delete("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/edb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
        //.body(`is`("Hello RESTEasy"))
    }


    @Test
    fun updateProduct() {

        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val product = ProductFormUpdate()
        product.title = "Oppdatert integrasjonstest produkt - Tittel 1"
        product.description = "Oppdatert integrasjonstest produkt - Beskrivelse 1"

        val productMapper = ProductUpdateMapper().toEntity(product)


        RestAssured.given()
            .`when`()
            .body(productMapper)
            .header("Content-type", "application/json")
            .put("$useProjectRef/products/edb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200) //envt 200
    }


}