package org.kravbank.api

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.domain.Need
import org.kravbank.utils.form.need.NeedForm
import org.kravbank.utils.form.need.NeedFormUpdate
import org.kravbank.utils.mapper.need.NeedMapper
import org.kravbank.utils.mapper.need.NeedUpdateMapper
import org.wildfly.common.Assert


/**
 * todo
 * debug need test
 */


//@QuarkusTest
//@QuarkusIntegrationTest
class NeedResourceTest {

/*
    private final val baseUri = "http://localhost:8080"
    private final val basePath = "/api/v1/projects"
    private final val useProjectRef = "/aaa4db69-edb2-431f-855a-4368e2bcddd1"
    private final val useResourceFolder = "/needs"
    private final val useNeedRef = "/need1b69-edb2-431f-855a-4368e2bcddd1"
    private final val useNeedRefPut = "/need2b69-edb2-431f-855a-4368e2bcddd1"
    private final val resourceUrl = "$baseUri$basePath$useProjectRef$useResourceFolder"
    private final val fullUrl = "$resourceUrl$useNeedRefPut"

    @Test
    fun getNeed() {
        RestAssured.given()
            //.pathParam("uuid", uuid)
            .`when`()
            .get("$resourceUrl$useNeedRef")
            .then()
            .statusCode(200)
        // .body(`is`("hello $uuid"))
    }

    @Test
    fun listNeed() {
        RestAssured.given()
            .`when`().get(resourceUrl)
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))
    }

    @Test
    fun createNeed() {

        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val needDTO = NeedForm ()
        needDTO.title = "Integrasjonstest need - tittel 1"
        needDTO.description = "Integrasjonstest need - beskrivelse 1"

        val need = NeedMapper().toEntity(needDTO)


        RestAssured.given()
            .`when`()
            .body(needDTO)
            .header("Content-type", "application/json")
            .post("/aaa4db69-edb2-431f-855a-4368e2bcddd1/needs/")
            .then()
            .statusCode(201) //envt 200


    }

    @Test
    fun updateNeed() {

       RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val need = NeedFormUpdate ()
        need.title = "Oppdatert Integrasjonstest need - tittel 1"
        need.description = "Oppdatert Integrasjonstest need - beskrivelse 1"

        //val needMapper = NeedUpdateMapper().toEntity(need)

        RestAssured.given()
            .`when`()
            .body(need)
            .header("Content-type", "application/json")
            .put(fullUrl)
            .then()
            .statusCode(200) //envt 200
    }




    @Test
    fun deleteNeed() {
        RestAssured.given()
            .`when`()
            .delete("$resourceUrl$useNeedRef")
            .then()
            .statusCode(200)
        //.body(`is`("Hello RESTEasy"))
    }

*/


}