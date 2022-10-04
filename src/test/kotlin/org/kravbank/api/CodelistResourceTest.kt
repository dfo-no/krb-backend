package org.kravbank.api

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.domain.Codelist
import java.awt.PageAttributes.MediaType
import javax.print.attribute.standard.MediaTray

@QuarkusIntegrationTest
internal class CodelistResourceTest() {
    val baseUri = "http://localhost:8080"
    val basePath = "/api/v1/projects"
    val useProjectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"

    @Test
    fun getCodelistByRef() {

        //val getCodelistPath = "$baseUri$basePath$useProjectRef"

        given()
            .`when`()
            .get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))
    }

    @Test
    fun listCodelists() {
        given()
            .`when`().get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/")
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))
    }

    @Test
    fun createCodelist() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val codelist = Codelist()
        codelist.title = "CODELIST Integrasjonstest - Tittel 1"
        codelist.description = "CODELIST Integrasjonstest - Beskrivelse 1"

        given()
            .`when`()
            .body(codelist)
            .header("Content-type", "application/json")
            .post("$useProjectRef/codelists")
            .then()
            .statusCode(201) //envt 200
    }

    @Test
    fun deleteCodelistByRef() {
        given()
            .`when`()
            .delete("http://localhost:8080/api/v1/projects/prosjekt5-edb2-431f-855a-4368e2bcddd1/codelists/newlist2222db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(204)

        //.body(`is`("Hello RESTEasy"))
    }





    @Test
    fun updateCodelist() {

        //val ut = given().put("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/asd4db69-edb2-431f-855a-4368e2bcddd1").statusCode()
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val codelist = Codelist()
        codelist.title = "CODELIST Oppdatert integrasjonstest - Tittel 1"
        codelist.description = "CODELIST Oppdatert integrasjonstest - Beskrivelse 1"

        given()
            .`when`()
            .body(codelist)
            .header("Content-type", "application/json")
            .put("$useProjectRef/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200) //envt 200
    }
}