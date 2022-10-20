package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.utils.form.requirementvariant.RequirementVariantForm
import org.kravbank.utils.form.requirementvariant.RequirementVariantFormUpdate

@QuarkusTest
@QuarkusIntegrationTest
class RequirementVariantResourceTest {

    @Test
    fun getRequirementVariant() {
        given()
            .`when`()
            .get("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants/rvrv2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listRequirementVariants() {
        given()
            .`when`().get("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants")
            .then()
            .statusCode(200)
    }

    @Test
    fun createRequirementVariant() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects"

        val rv = RequirementVariantForm ()
        rv.description = "Integrasjonstest rv desc"
        rv.requirementText = "Integrasjonstest rv reqtext"
        rv.instruction = "Integrasjonstest rv instruction"
        rv.useProduct = true
        rv.useSpesification = true
        rv.useQualification = true

        given()
            .`when`()
            .body(rv)
            .header("Content-type", "application/json")
            .post("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants")
            .then()
            .statusCode(201)
    }

    @Test
    fun deleteRequirementVariant() {
        given()
            .`when`()
            .delete("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants/rvrv1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun updateRequirementVariant() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects"

        val rv = RequirementVariantFormUpdate ()
        rv.description = "Integrasjonstest rv desc"
        rv.requirementText = "Integrasjonstest rv reqtext"
        rv.instruction = "Integrasjonstest rv instruction"
        rv.useProduct = true
        rv.useSpesification = true
        rv.useQualification = true

        given()
            .`when`()
            .body(rv)
            .header("Content-type", "application/json")
            .put("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants/rvrv2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}