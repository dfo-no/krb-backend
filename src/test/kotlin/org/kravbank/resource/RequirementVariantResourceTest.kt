package org.kravbank.resource

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.kravbank.dao.RequirementVariantForm
import org.kravbank.utils.KeycloakAccess

@QuarkusTest
class RequirementVariantResourceTest {

    private val token = KeycloakAccess.getAccessToken("alice")

    @Test
    @Order(1)
    fun getRequirementVariant() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants/rvrv2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    @Order(2)
    fun listRequirementVariants() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants")
            .then()
            .statusCode(200)
    }

    @Test
    @Order(3)
    fun createRequirementVariant() {
        RestAssured.defaultParser = Parser.JSON
        val rv = RequirementVariantForm()
        rv.description = "Integrasjonstest rv desc"
        rv.requirementText = "Integrasjonstest rv reqtext"
        rv.instruction = "Integrasjonstest rv instruction"
        rv.useProduct = true
        rv.useSpecification = true
        rv.useQualification = true

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(rv)
            .header("Content-type", "application/json")
            .post("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants")
            .then()
            .statusCode(201)
    }

    @Test
    @Order(4)
    fun updateRequirementVariant() {
        RestAssured.defaultParser = Parser.JSON
        val rv = RequirementVariantForm()
        rv.description = "Integrasjonstest rv desc"
        rv.requirementText = "Integrasjonstest rv reqtext"
        rv.instruction = "Integrasjonstest rv instruction"
        rv.useProduct = true
        rv.useSpecification = true
        rv.useQualification = true

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(rv)
            .header("Content-type", "application/json")
            .put("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants/rvrv2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    @Order(5)
    fun deleteRequirementVariant() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants/rvrv3b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

}