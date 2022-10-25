package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test;
import org.kravbank.dao.NeedForm
import org.kravbank.dao.RequirementForm


@QuarkusTest
@QuarkusIntegrationTest
class RequirementResourceTest {

    @Test
    fun getRequirement() {
        given()
            .`when`()
            .get("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/reqd2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listRequirements() {
        given()
            .`when`()
            .get("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/")
            .then()
            .statusCode(200)
    }

    @Test
    fun createRequirement() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects"

        val form = RequirementForm()
        form.title = "Integrasjonstest requirement - tittel 1"
        form.description = "Integrasjonstest requirement - beskrivelse 1"
        form.needRef = "need2b69-edb2-431f-855a-4368e2bcddd1"

       //val requirement = RequirementFormDAO().toEntity(form)

    /*    val form = NeedForm()
        form.title = "POST Integrasjonstest need - tittel 1"
        form.description = "POST Integrasjonstest need - beskrivelse 1"
     */


        given()
            .`when`()
            .body(form)
            .header("Content-type", "application/json")
            .post("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements")
            .then()
            .statusCode(201)
    }

    @Test
    fun updateRequirement() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects";

        val form = RequirementForm()
        form.title = "Integrasjonstest requirement - tittel 1"
        form.description = "Integrasjonstest requirement - beskrivelse 1"

        //val requirement = RequirementFormDAO().toEntity(form)

        given()
            .`when`()
            .body(form)
            .header("Content-type", "application/json")
            .put("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun deleteRequirement() {
        given()
            .`when`()
            .delete("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/reqd2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}