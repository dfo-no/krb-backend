package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.form.codelist.CodelistFormUpdate
import org.kravbank.utils.mapper.codelist.CodelistMapper
import org.kravbank.utils.mapper.codelist.CodelistUpdateMapper

@QuarkusTest
@QuarkusIntegrationTest
internal class CodelistResourceTest() {
    val baseUri = "http://localhost:8080"
    val basePath = "/api/v1/projects"
    val useProjectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"

    @Test
    fun getCodelistByRef() {

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

        val codelist = CodelistForm()
        codelist.title = "CODELIST Integrasjonstest - Tittel 1"
        codelist.description = "CODELIST Integrasjonstest - Beskrivelse 1"

        val codelistMapper = CodelistMapper().toEntity(codelist)

        given()
            .`when`()
            .body(codelistMapper)
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
            .statusCode(200)

        //.body(`is`("Hello RESTEasy"))
    }


    @Test
    fun updateCodelist() {

        //val ut = given().put("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/asd4db69-edb2-431f-855a-4368e2bcddd1").statusCode()
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val codelist = CodelistFormUpdate()
        codelist.title = "CODELIST Oppdatert integrasjonstest - Tittel 1"
        codelist.description = "CODELIST Oppdatert integrasjonstest - Beskrivelse 1"
        val codelistMapper = CodelistUpdateMapper().toEntity(codelist)

        given()
            .`when`()
            .body(codelistMapper)
            .header("Content-type", "application/json")
            .put("$useProjectRef/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200) //envt 200
    }
}