package org.kravbank.api

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.domain.Codelist

@QuarkusTest
class CodelistResourceTest () {
    val baseUri= "http://localhost:8080"
    val basePath = "/api/v1/projects"
   val useProjectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"

    @Test
    fun getCodelist() {

        val getCodelistPath = "$baseUri$basePath$useProjectRef"

        given()
            .`when`().get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1")
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
        codelist.title = "Integrasjonstest - Tittel 1"
        codelist.description = "Integrasjonstest - Beskrivelse 1"

        //    val codelist = Codelist("Integrasjonstest - Tittel 1 dataclass", "Integrasjonstest - Beskrivelse 1 dataclass", "bbq4db69-edb2-431f-855a-4368e2bcddd1", null,null,null)

        given()
            .`when`()
            .body(codelist)
            .header("Content-type", "application/json")
            .post("$useProjectRef/codelists")
            .then()
            .statusCode(201) //envt 200
    }

    @Test
    fun deleteCodelistById() {
    }

    @Test
    fun updateCodelist() {
    }

    @Test
    fun getCodelistService() {
    }

    @Test
    fun getProjectService() {
    }
}