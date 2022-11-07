package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.dao.CodelistForm

@QuarkusTest
@QuarkusIntegrationTest
internal class CodelistResourceTest() {

    @Test
    fun getCodelist() {
        given()
            .`when`()
            .get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listCodelists() {
        given()
            .`when`().get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/")
            .then()
            .statusCode(200)
    }

    @Test
    fun createCodelist() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects";

        val codelist = CodelistForm()
        codelist.title = "CODELIST Integrasjonstest - Tittel 1"
        codelist.description = "CODELIST Integrasjonstest - Beskrivelse 1"
        val codelistMapper = CodelistForm().toEntity(codelist)

        given()
            .`when`()
            .body(codelistMapper)
            .header("Content-type", "application/json")
            .post("bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists")
            .then()
            .statusCode(201)
    }

    @Test
    fun deleteCodelist() {
        given()
            .`when`()
            .delete("http://localhost:8080/api/v1/projects/prosjekt5-edb2-431f-855a-4368e2bcddd1/codelists/newlist2222db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }


    @Test
    fun updateCodelist() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects"

        val codelist = CodelistForm()
        codelist.title = "CODELIST Oppdatert integrasjonstest - Tittel 1"
        codelist.description = "CODELIST Oppdatert integrasjonstest - Beskrivelse 1"
        val codelistMapper = CodelistForm().toEntity(codelist)

        given()
            .`when`()
            .body(codelistMapper)
            .header("Content-type", "application/json")
            .put("bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}