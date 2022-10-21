package org.kravbank.resource

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.utils.code.dto.CodeForm
import org.kravbank.utils.code.dto.CodeFormUpdate
import org.kravbank.utils.code.mapper.CodeMapper
import org.kravbank.utils.code.mapper.CodeUpdateMapper

@QuarkusTest
@QuarkusIntegrationTest
class CodeResourceTest {

    @Test
    fun getCode() {
        given()
            .`when`()
            .get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1/codes/script1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listCode() {
        given()
            .`when`()
            .get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1/codes/")
            .then()
            .statusCode(200)
    }

    @Test
    fun createCode() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/api/v1/projects"

        val code = CodeForm()
        code.title = "CODE Integrasjonstest tittel"
        code.description = "CODE Integrasjonstest code desc"
        val codeMapper = CodeMapper().toEntity(code)

        given()
            .`when`()
            .body(codeMapper)
            .header("Content-type", "application/json")
            .post("/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1/codes")
            .then()
            .statusCode(201)
    }

    @Test
    fun deleteCode() {
        given()
            .`when`()
            .delete("http://localhost:8080/api/v1/projects/prosjekt6-edb2-431f-855a-4368e2bcddd1/codelists/newlist33333db69-edb2-431f-855a-4368e2bcddd1/codes/script6b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun updateCode() {
        RestAssured.defaultParser = Parser.JSON
        RestAssured.baseURI = "/api/v1/projects"
        RestAssured.basePath = "/api/v1/projects";

        val code = CodeFormUpdate()
        code.title = "CODE Integrasjonstest tittel"
        code.description = "CODE Integrasjonstest code desc"
        val codeMapper = CodeUpdateMapper().toEntity(code)

        given()
            .`when`()
            .body(codeMapper)
            .header("Content-type", "application/json")
            .put("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1/codes/script1b69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}