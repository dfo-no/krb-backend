package org.kravbank.resource.real

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.kravbank.domain.PublicationExport
import org.kravbank.utils.KeycloakAccess

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
class PublicationExportResourceTest {

    lateinit var token: String
    lateinit var publicationRef: String
    lateinit var projectRef: String
    lateinit var globalCreatedRefToUse: String


    @BeforeEach
    fun setUp() {

        token = KeycloakAccess.getAccessToken("alice")

        projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        publicationRef = "zzz4db69-edb2-431f-855a-4368e2bcddd1"
    }


    @Test
    @Order(1)
    fun `create publication export then success`() {

        val response = RestAssured.given()
            .auth()
            .oauth2(token)
            .`when`()
            .header("Content-type", "application/json")
            .post("/api/v1/projects/$projectRef/publications/$publicationRef/publicationexports/")

        assertEquals(201, response.statusCode)
        assertTrue(response.headers.hasHeaderWithName("Location"))

        val newUrlLocation = response.headers.getValue("Location")
        val createdRef = newUrlLocation.split("publicationexports/")[1]
        assertTrue(createdRef.isNotEmpty())

        globalCreatedRefToUse = createdRef
    }

    @Test
    @Order(2)
    fun `get publication export then success`() {

        RestAssured.given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("api/v1/projects/$projectRef/publications/$publicationRef/publicationexports/$globalCreatedRefToUse")
            .then()
            .statusCode(200)
            .extract().`as`(PublicationExport::class.java)
            .let {
                assertEquals(globalCreatedRefToUse, it.ref)
                assertEquals(publicationRef, it.publicationRef)
            }
    }
}