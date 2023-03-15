package org.kravbank.resource.real

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.dao.RequirementForm
import org.kravbank.utils.KeycloakAccess

@QuarkusTest
class RequirementResourceTest {

    private val token = KeycloakAccess.getAccessToken("alice")


    @Test
    fun getRequirement() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/reqd2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listRequirements() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/")
            .then()
            .statusCode(200)
    }

    @Test
    fun createRequirement() {
        RestAssured.defaultParser = Parser.JSON
        val form = RequirementForm().apply {
            title = "Integrasjonstest requirement - tittel 1"
            description = "Integrasjonstest requirement - beskrivelse 1"
        }

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(form)
            .header("Content-type", "application/json")
            .post("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements")
            .then()
            .statusCode(201)
    }

    @Test
    fun updateRequirement() {
        RestAssured.defaultParser = Parser.JSON
        val form = RequirementForm()
        form.title = "Integrasjonstest requirement - tittel 1"
        form.description = "Integrasjonstest requirement - beskrivelse 1"


        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(form)
            .header("Content-type", "application/json")
            .put("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun deleteRequirement() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/reqd2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(204)
    }
}