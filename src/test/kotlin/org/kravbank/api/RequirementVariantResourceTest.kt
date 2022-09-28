package org.kravbank.api

import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class RequirementVariantResourceTest {

    private final val baseUri = "http://localhost:8080"
    private final val basePath = "/api/v1/projects"
    private final val useProjectRef = "/aaa4db69-edb2-431f-855a-4368e2bcddd1"
    private final val useRequirementURI = "/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/"
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
            .get("http://localhost:8080/api/v1/projects/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/req1b69-edb2-431f-855a-4368e2bcddd1/requirementvariants/rvrv1b69-edb2-431f-855a-4368e2bcddd1")
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
        //assert(false)
    }

    @Test
    fun deleteRequirementVariant() {
    }

    @Test
    fun updateRequirementVariant() {
    }

    @Test
    fun getRequirementVariantService() {
    }
}