package org.kravbank.api

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test;
import org.kravbank.domain.Need
import org.kravbank.repository.NeedRepository
import org.kravbank.utils.form.requirement.RequirementForm
import org.kravbank.utils.form.requirement.RequirementFormCreate
import org.kravbank.utils.form.requirement.RequirementFormUpdate
import org.kravbank.utils.mapper.requirement.RequirementCreateMapper
import org.kravbank.utils.mapper.requirement.RequirementMapper
import org.kravbank.utils.mapper.requirement.RequirementUpdateMapper
import javax.inject.Inject


//@QuarkusTest
@QuarkusIntegrationTest
class RequirementResourceTest {


    private final val baseUri = "http://localhost:8080"
    private final val basePath = "/api/v1/projects"
    private final val useProjectRef = "/aaa4db69-edb2-431f-855a-4368e2bcddd1"
    private final val useResourceFolder = "/requirements"
    private final val useRequirementRefPut = "/req1b69-edb2-431f-855a-4368e2bcddd1"
    private final val useRequirementRef = "/reqd2b69-edb2-431f-855a-4368e2bcddd1"
    private final val resourceUrl = "$baseUri$basePath$useProjectRef$useResourceFolder"
    private final val fullUrl = "$resourceUrl$useRequirementRefPut"


    //@Inject
    @SuppressWarnings("initialization.fields.uninitialized")
    var needRepository: NeedRepository? = null

    @Test
    fun getRequirement() {
        RestAssured.given()
            //.pathParam("uuid", uuid)
            .`when`()
            .get("$resourceUrl$useRequirementRef")
            .then()
            .statusCode(200)
        // .body(`is`("hello $uuid"))
    }

    @Test
    fun listRequirements() {
        RestAssured.given()
            .`when`().get(resourceUrl)
            .then()
            .statusCode(200)
        //.body(, equalTo("Integrasjonstest prosjektittel"))
    }

    @Test
    fun createRequirement() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val requirementDTO = RequirementFormCreate ()
        requirementDTO.title = "Integrasjonstest requirement - tittel 1"
        requirementDTO.description = "Integrasjonstest requirement - beskrivelse 1"
        requirementDTO.need = "need2b69-edb2-431f-855a-4368e2bcddd1"

        val newReq = RequirementCreateMapper().toEntity(requirementDTO)

        RestAssured.given()
            .`when`()
            .body(requirementDTO)
            .header("Content-type", "application/json")
            .post("/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/")
            .then()
            .statusCode(201) //envt 200
    }



    @Test
    fun updateRequirement() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = baseUri
        //RestAssured.port = 8080;
        RestAssured.basePath = basePath;

        val requirement = RequirementFormUpdate ()
        requirement.title = "Oppdatert Integrasjonstest requirement - tittel 1"
        requirement.description = "Oppdatert Integrasjonstest requirement - beskrivelse 1"
        val requirementMapper = RequirementUpdateMapper().toEntity(requirement)

        RestAssured.given()
            .`when`()
            .body(requirementMapper)
            .header("Content-type", "application/json")
            .put(fullUrl)
            .then()
            .statusCode(200) //envt 200
    }

    @Test
    fun deleteRequirement() {
        RestAssured.given()
            .`when`()
            .delete("$resourceUrl$useRequirementRef")
            .then()
            .statusCode(200)
        //.body(`is`("Hello RESTEasy"))
    }



}