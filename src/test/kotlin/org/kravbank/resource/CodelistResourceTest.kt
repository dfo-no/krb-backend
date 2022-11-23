package org.kravbank.resource

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class CodelistResourceTest() {
    /*
    //START

    TODO: Fiks integrasjonstest med authz/n for denne testklassen

    // trenger access token fra keycloak. Dette har jeg foreløpig ikke fått til for resource testene på localhost

    // mulighet 1 Mock rolle

        @InjectMock
        lateinit var identity: SecurityIdentity
        @BeforeEach
        fun setup() {
            Mockito.`when`(identity.hasRole("user")).thenReturn(true)
        }

     // mulighet 2: bruk keycloak (test) klient, sett så Rest assured given sin  auth / oauth2 med generert token

     var keycloakClient = KeycloakTestClient()

     protected fun getAccessToken(userName: String?): String? {
        return keycloakClient.getAccessToken(userName)
        }

        Feks:
         fun listProjects() {
        given()
            .auth().oauth2(getAccessToken("brukernavn"))
            .`when`()
            .get("http://localhost:8080/api/v1/projects")
            .then()
            .statusCode(200)


    //mulighet 3: annoter test metode med  @TestSecurity(user = "brukernavn", roles = ["admin", "user"])


    //mulighet 4: disable authorization @TestSecurity(authorizationEnabled = false)


    //mulighet 5:Generer en "fake" token ved å bruke en @QuarkusTestResource, eventuelt  generer en ekte token ved å implementere logikk for dette for direkte kall til keycloak-serveren.

     // eksempel med rest assured:
     .header("Authorization", "Bearer $BEARER_TOKEN")

//SLUTT

        @Test
        fun getCodelist() {
            given()
                .`when`()
                .get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1")
                .then()
                .statusCode(200)
        }

        @Test
        fun listCodelists() {
            given()
                .`when`().get("http://localhost:8080/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/")
                .then()
                .statusCode(200)
        }

        @Test
        fun createCodelist() {
            RestAssured.defaultParser = Parser.JSON
            RestAssured.baseURI = "http://localhost:8080"
            RestAssured.basePath = "/api/v1/projects"

            val codelist = CodelistForm()
            codelist.title = "CODELIST Integrasjonstest - Tittel 1"
            codelist.description = "CODELIST Integrasjonstest - Beskrivelse 1"
            val codelistMapper = CodelistForm().toEntity(codelist)

            given()
                .`when`()
                .body(codelistMapper)
                .header("Content-type", "application/json")
                .post("bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists")
                .then()
                .statusCode(201)
        }

        @Test
        fun deleteCodelist() {
            given()
                .`when`()
                .delete("http://localhost:8080/api/v1/projects/prosjekt5-edb2-431f-855a-4368e2bcddd1/codelists/newlist2222db69-edb2-431f-855a-4368e2bcddd1")
                .then()
                .statusCode(200)
        }


        @Test
        fun updateCodelist() {
            RestAssured.defaultParser = Parser.JSON
            RestAssured.baseURI = "http://localhost:8080"
            RestAssured.basePath = "/api/v1/projects"

            val codelist = CodelistForm()
            codelist.title = "CODELIST Oppdatert integrasjonstest - Tittel 1"
            codelist.description = "CODELIST Oppdatert integrasjonstest - Beskrivelse 1"
            val codelistMapper = CodelistForm().toEntity(codelist)

            given()
                .`when`()
                .body(codelistMapper)
                .header("Content-type", "application/json")
                .put("bbb4db69-edb2-431f-855a-4368e2bcddd1/codelists/qqq4db69-edb2-431f-855a-4368e2bcddd1")
                .then()
                .statusCode(200)
        }

     */
}