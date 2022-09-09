package org.kravbank

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.kravbank.domain.ProjectKtl

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

        val project = ProjectKtl()

        project.description ="beskrivelse"
        project.title = "Tittel"

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




}