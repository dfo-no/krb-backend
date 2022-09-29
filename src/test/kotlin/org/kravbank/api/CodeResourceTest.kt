package org.kravbank.api

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.kravbank.domain.Code
import org.kravbank.domain.Codelist
import org.kravbank.domain.RequirementVariant

@QuarkusIntegrationTest
internal class CodeResourceTest {

    private final val baseUri = "http://localhost:8080"
    private final val basePath = "/api/v1/projects"
    private final val useProjectRef = "/prosjekt4-edb2-431f-855a-4368e2bcddd1"
    private final val useRequirementURI = "/codelists/newlist14db69-edb2-431f-855a-4368e2bcddd1"
    private final val useResourceFolder = "/codes"
    private final val useRequirementVariantRef = "/script4b69-edb2-431f-855a-4368e2bcddd1"
    private final val useRequirementVariantRefPut = "/codecode2b69-edb2-431f-855a-4368e2bcddd1"
    private final val resourceUrl = "$baseUri$basePath$useProjectRef$useRequirementURI$useResourceFolder"
    private final val fullUrl = "$resourceUrl$useRequirementVariantRefPut"




    @Test
    fun getCode() {
        RestAssured.given()
            //.pathParam("uuid", uuid)
            .`when`()
            .get("http://localhost:8080/api/v1/projects/prosjekt4-edb2-431f-855a-4368e2bcddd1/codelists/newlist14db69-edb2-431f-855a-4368e2bcddd1/codes/script4b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
        // .body(`is`("hello $uuid"))
    }

    @Test
    fun listCode() {
        RestAssured.given()
            .`when`().get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/asd4db69-edb2-431f-855a-4368e2bcddd1/codes/")
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))


    }

    @Test
    fun createCode() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath

        val code = Code ()
        code.title = "Integrasjonstest tittel"
        code.description = "Integrasjonstest code desc"

        RestAssured.given()
            .`when`()
            .body(code)
            .header("Content-type", "application/json")
            .post("$useProjectRef$useRequirementURI$useResourceFolder")
            .then()
            .statusCode(201) //envt 200
    }

    @Test
    fun deleteCodeById() {
        RestAssured.given()
            .`when`()
            .delete("http://localhost:8080/api/v1/projects/prosjekt6-edb2-431f-855a-4368e2bcddd1/codelists/newlist33333db69-edb2-431f-855a-4368e2bcddd1/codes/script6b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(204)
        //.body(`is`("Hello RESTEasy"))

    }

    @Test
    fun updateCode() {
        //val ut = given().put("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/asd4db69-edb2-431f-855a-4368e2bcddd1").statusCode()
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val codelist = Codelist()
        codelist.title = "Oppdatert integrasjonstest - Tittel 1"
        codelist.description = "Oppdatert integrasjonstest - Beskrivelse 1"

        RestAssured.given()
            .`when`()
            .body(codelist)
            .header("Content-type", "application/json")
            .put("http://localhost:8080/api/v1/projects/prosjekt4-edb2-431f-855a-4368e2bcddd1/codelists/newlist14db69-edb2-431f-855a-4368e2bcddd1/codes/script4b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200) //envt 200
    }
}