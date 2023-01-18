package org.kravbank.resource

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.kravbank.dao.CodelistForm
import org.kravbank.domain.Codelist
import org.kravbank.utils.KeycloakAccess


@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // beholder globale variablen newCodelistRef som blir satt i testens lifecycle
class CodelistResourceTest {

    val token: String = KeycloakAccess.getAccessToken("alice")

    lateinit var projectRef: String

    lateinit var newCodelistRef: String


    @BeforeAll
    fun `set up`() {
        projectRef = "prosjekt6-edb2-431f-855a-4368e2bcddd1"

        RestAssured.defaultParser = Parser.JSON

    }

    @Nested
    @Order(1)
    inner class CreateCodelist {

        @Test
        fun `create new codelist`() {
            val createForm = CodelistForm().apply {
                title = "Ekte resource test"
                description = "Ekte beskrivelse"
            }

            val response =
                given()
                    .auth()
                    .oauth2(token)
                    .`when`()
                    .body(CodelistForm().toEntity(createForm))
                    .header("Content-type", "application/json")
                    .post("/api/v1/projects/$projectRef/codelists")

            assertEquals(201, response.statusCode)
            assertTrue(response.headers.hasHeaderWithName("Location"))

            val newUrlLocation = response.headers.getValue("Location")
            val newRefToUse = newUrlLocation.split("codelists/")[1]
            assertTrue(newRefToUse.isNotEmpty())

            newCodelistRef = newRefToUse
        }
    }

    @Nested
    @Order(2)
    inner class GetCodelist {
        @Test
        fun `get the newly created codelist`() {
            val response = given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects/$projectRef/codelists/$newCodelistRef")

            assertEquals(200, response.statusCode())


            val entity = response.body.jsonPath().getObject("", Codelist::class.java)
            assertEquals(newCodelistRef, entity.ref)
        }
    }


    @Nested
    @Order(3)
    inner class ListCodelist {
        @Test
        fun `list all codelists`() {
            val response = given()
                .auth()
                .oauth2(token)
                .`when`()
                .get("/api/v1/projects/$projectRef/codelists/")

            assertEquals(200, response.statusCode)


            val listOfCodelist = response.body.jsonPath().getList("", Codelist::class.java)
            val lastEntityInList = listOfCodelist.last()
            assertEquals(newCodelistRef, lastEntityInList.ref)
        }
    }

    @Nested
    @Order(4)
    inner class UpdateCodelist {
        @Test
        fun `update newly created codelist`() {

            val updateForm = CodelistForm().apply {
                title = "Endrer tittel"
                description = "Endrer beskrivelse"
            }

            val response = given()
                .auth()
                .oauth2(token)
                .`when`()
                .body(CodelistForm().toEntity(updateForm))
                .header("Content-type", "application/json")
                .put("/api/v1/projects/$projectRef/codelists/$newCodelistRef")

            assertEquals(200, response.statusCode)


            val entity = response.body.jsonPath().getObject("", Codelist::class.java)
            assertEquals(newCodelistRef, entity.ref)
            assertEquals(updateForm.title, entity.title)
            assertEquals(updateForm.description, entity.description)
        }
    }

    @Nested
    @Order(5)
    inner class DeleteCodelist {

        @Test
        fun `delete the newly created codelist`() {
            val response = given()
                .auth()
                .oauth2(token)
                .`when`()
                .delete("/api/v1/projects/$projectRef/codelists/$newCodelistRef")

            assertEquals(200, response.statusCode)


            val deletedRefConfirm = response.body.asPrettyString()
            assertEquals(newCodelistRef, deletedRefConfirm)
        }
    }
}