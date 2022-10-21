package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test;
import org.kravbank.utils.requirement.dto.RequirementFormCreate
import org.kravbank.utils.requirement.dto.RequirementFormUpdate
import org.kravbank.utils.requirement.mapper.RequirementUpdateMapper

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

        val requirementDTO = RequirementFormCreate()
        requirementDTO.title = "Integrasjonstest requirement - tittel 1"
        requirementDTO.description = "Integrasjonstest requirement - beskrivelse 1"
        requirementDTO.need = "need1b69-edb2-431f-855a-4368e2bcddd1"

        given()
            .`when`()
            .body(requirementDTO)
            .header("Content-type", "application/json")
            .post("/aaa4db69-edb2-431f-855a-4368e2bcddd1/requirements/")
            .then()
            .statusCode(201)
    }

    @Test
    fun updateRequirement() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects";

        val requirement = RequirementFormUpdate()
        requirement.title = "Oppdatert Integrasjonstest requirement - tittel 1"
        requirement.description = "Oppdatert Integrasjonstest requirement - beskrivelse 1"
        val requirementMapper = RequirementUpdateMapper().toEntity(requirement)

        given()
            .`when`()
            .body(requirementMapper)
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