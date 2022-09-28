package org.kravbank.api

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.domain.Requirement
import org.kravbank.domain.RequirementVariant

@QuarkusIntegrationTest
internal class RequirementVariantResourceTest {
    private final val baseUri = "http://localhost:8080"
    private final val basePath = "/api/v1/projects"
    private final val useProjectRef = "/aaa4db69-edb2-431f-855a-4368e2bcddd1"
    private final val useRequirementURI = "/requirements/req1b69-edb2-431f-855a-4368e2bcddd1"
    private final val useResourceFolder = "/requirementvariants"
    private final val useRequirementVariantRef = "/rvrv1b69-edb2-431f-855a-4368e2bcddd1"
    private final val useRequirementVariantRefPut = "/rvrv2b69-edb2-431f-855a-4368e2bcddd1"
    private final val resourceUrl = "$baseUri$basePath$useProjectRef$useRequirementURI$useResourceFolder"
    private final val fullUrl = "$resourceUrl$useRequirementVariantRefPut"


    @Test
    fun getRequirementVariant() {
        RestAssured.given()
            //.pathParam("uuid", uuid)
            .`when`()
            .get("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants/rvrv2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
        // .body(`is`("hello $uuid"))
    }

    @Test
    fun listRequirementVariants() {
        RestAssured.given()
            .`when`().get("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants")
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))
    }

    @Test
    fun createRequirementVariant() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath

        val rv = RequirementVariant ()
        rv.description = "Integrasjonstest rv desc"
        rv.requirementText = "Integrasjonstest rv reqtext"
        rv.instruction = "Integrasjonstest rv instruction"
        rv.useProduct = true
        rv.useSpesification = true
        rv.useQualification = true

        RestAssured.given()
            .`when`()
            .body(rv)
            .header("Content-type", "application/json")
            .post("$useProjectRef$useRequirementURI$useResourceFolder")
            .then()
            .statusCode(201) //envt 200
    }

    @Test
    fun deleteRequirementVariant() {
        RestAssured.given()
            .`when`()
            .delete("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants/rvrv1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(204)
        //.body(`is`("Hello RESTEasy"))

    }

    @Test
    fun updateRequirementVariant() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080
        RestAssured.basePath = basePath

        val rv = RequirementVariant ()
        rv.description = "Integrasjonstest rv desc"
        rv.requirementText = "Integrasjonstest rv reqtext"
        rv.instruction = "Integrasjonstest rv instruction"
        rv.useProduct = true
        rv.useSpesification = true
        rv.useQualification = true

        RestAssured.given()
            .`when`()
            .body(rv)
            .header("Content-type", "application/json")
            .put("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants/rvrv2b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}