package org.kravbank.api

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.kravbank.domain.Code
import org.kravbank.domain.Codelist
import org.kravbank.domain.RequirementVariant
import org.kravbank.utils.form.code.CodeForm
import org.kravbank.utils.form.code.CodeFormUpdate
import org.kravbank.utils.mapper.code.CodeMapper
import org.kravbank.utils.mapper.code.CodeUpdateMapper
import org.kravbank.utils.mapper.project.ProjectMapper

@QuarkusIntegrationTest
class CodeResourceTest {

    private final val baseUri = "http://localhost:8080"
    private final val basePath = "/api/v1/projects"
    private final val useProjectRef = "/prosjekt4-edb2-431f-855a-4368e2bcddd1"
    private final val useRequirementURI = "/codelists/newlist14db69-edb2-431f-855a-4368e2bcddd1"
    private final val useResourceFolder = "/codes"
    private final val useRequirementVariantRef = "/script4b69-edb2-431f-855a-4368e2bcddd1"
    private final val useRequirementVariantRefPut = "/script4b69-edb2-431f-855a-4368e2bcddd1"
    private final val resourceUrl = "$baseUri$basePath$useProjectRef$useRequirementURI$useResourceFolder"
    private final val fullUrl = "$resourceUrl$useRequirementVariantRefPut"


    // Prosjekt slettes

    // ccc4db69-edb2-431f-855a-4368e2bcddd1
    ///ccc4db69-edb2-431f-855a-4368e2bcddd1"

    //prosjekt oppdateres
    //bbb4db69-edb2-431f-855a-4368e2bcddd1


    //codelist oppdateres
    //prosjekt: bbb4db69-edb2-431f-855a-4368e2bcddd1
   // codelist: qqq4db69-edb2-431f-855a-4368e2bcddd1

    //codelist slettes
    //prosjekt: prosjekt5-edb2-431f-855a-4368e2bcddd1
    // newlist2222db69-edb2-431f-855a-4368e2bcddd1



    @Test
    fun getCode() {
        RestAssured.given()
            //.pathParam("uuid", uuid)
            .`when`()
            .get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1/codes/script1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
        // .body(`is`("hello $uuid"))
    }

    @Test
    fun listCode() {
        RestAssured.given()
            .`when`().get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1/codes/")
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

        val code = CodeForm ()
        code.title = "CODE Integrasjonstest tittel"
        code.description = "CODE Integrasjonstest code desc"

        val codeMapper = CodeMapper().toEntity(code)


        RestAssured.given()
            .`when`()
            .body(codeMapper)
            .header("Content-type", "application/json")
            .post("/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1/codes")
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

        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;


        val code = CodeFormUpdate ()
        code.title = "CODE Integrasjonstest tittel"
        code.description = "CODE Integrasjonstest code desc"

        val codeMapper = CodeUpdateMapper().toEntity(code)




        RestAssured.given()
            .`when`()
            .body(codeMapper)
            .header("Content-type", "application/json")
            .put("http://localhost:8080/api/v1/projects/prosjekt4-edb2-431f-855a-4368e2bcddd1/codelists/newlist14db69-edb2-431f-855a-4368e2bcddd1/codes/script4b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200) //envt 200

        //http://localhost:8080/api/v1/projects/prosjekt4-edb2-431f-855a-4368e2bcddd1/codelists/newlist14db69-edb2-431f-855a-4368e2bcddd1/codes/script4b69-edb2-431f-855a-4368e2bcddd1

        //

    }
}