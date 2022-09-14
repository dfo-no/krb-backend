package org.kravbank

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.kravbank.domain.Product
import org.kravbank.domain.Project
import java.util.UUID

@QuarkusTest
class ProjectResourceTest {

    @Test
    fun testHelloEndpoint() {
        given()
          .`when`().get("/hello")
          .then()
             .statusCode(200)
             .body(`is`("Hello RESTEasy"))
    }


    @Test
    fun createProject() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080";
        //RestAssured.port = 8080;
        RestAssured.basePath = "/kt";

        val product = Product();
        product.title ="Integrasjonstittle produkttittel"
        product.description="Integrasjonstest produktbeskrivelse"
        product.deletedDate="21-02-91"


        val project = Project()
        project.title = "Integrasjonstest prosjektittel"
        project.description ="Integrasjonstest prosjektbeskrivelse"
        //project.projectId = UUID.randomUUID().toString()
        project.version="1.0"
        project.publishedDate="Not published"
        project.deletedDate="11-11-11"
        //project.products[0] = product

        given()
            .`when`()
            .body(project)
            .header("Content-type", "application/json")
            .post("/projects")
            .then()
            .statusCode(201) //envt 200

       // RestAssured.reset()
        //body(both(startsWith("")).and(not(endsWith("null"))))


    }
    @Test
    fun getProjects() {

        //val response = RestAssured.get("http://localhost:8080/kt/projects")
       // println(response.statusCode())
            given()
                .`when`().get("http://localhost:8080/kt/projects")
                .then()
                .statusCode(200)
                //.body(, equalTo("Integrasjonstest prosjektittel"))

        val statusCode = given().get("http://localhost:8080/kt/projects").statusCode

        println(statusCode)

        }

    @Test
    fun deleteProject() {

        //feil status code
        //fungerer i postman


        given()
            .`when`().delete("http://localhost:8080/kt/project/1")
            .then()
            .statusCode(204)
            //.body(`is`("Hello RESTEasy"))

    }


}