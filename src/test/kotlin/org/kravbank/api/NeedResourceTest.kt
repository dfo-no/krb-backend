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



@QuarkusTest
@QuarkusIntegrationTest
class NeedResourceTest {

    @Test
    fun getNeed() {
        RestAssured.given()
            //.pathParam("uuid", uuid)
            .`when`()
            .get("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/needs/need1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listNeed() {


        RestAssured.given()
            .`when`().get("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/needs/")
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))
    }

    @Test
    fun createNeed() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        //RestAssured.port = 8080;
        RestAssured.basePath = "/api/v1/projects";

        val needDTO = NeedForm()
        needDTO.title = "POST Integrasjonstest need - tittel 1"
        needDTO.description = "POST Integrasjonstest need - beskrivelse 1"

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
        RestAssured.baseURI = "http://localhost:8080"
        //RestAssured.port = 8080;
        RestAssured.basePath = "/api/v1/projects";

        val needDTO = NeedFormUpdate()
        needDTO.title = "PUT Integrasjonstest need - tittel 1"
        needDTO.description = "PUT Integrasjonstest need - beskrivelse 1"

        RestAssured.given()
            .`when`()
            .body(needDTO)
            .header("Content-type", "application/json")
            .put("/aaa4db69-edb2-431f-855a-4368e2bcddd1/needs/need1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200) //envt 200
    }


    @Test
    fun deleteNeed() {
        RestAssured.given()
            .`when`()
            .delete("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/needs/need2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
        //.body(`is`("Hello RESTEasy"))
    }
}