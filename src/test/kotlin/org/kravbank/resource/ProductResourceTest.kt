package org.kravbank.resource

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.parsing.Parser
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProductForm
import org.kravbank.utils.KeycloakAccess


@QuarkusTest
class ProductResourceTest {

    val token = KeycloakAccess.getAccessToken("alice")


    @Test
    fun getProduct() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/kuk4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun listProducts() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/")
            .then()
            .statusCode(200)
    }

    @Test
    fun createProduct() {
        RestAssured.defaultParser = Parser.JSON

        val form = ProductForm()
        form.title = "PRODUCT Integrasjonstest - Tittel 1"
        form.description = "PRODUCT Integrasjonstest - Beskrivelse 1"
        form.requirementVariantRef = "rvrv2b69-edb2-431f-855a-4368e2bcddd1"

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(form)
            .header("Content-type", "application/json")
            .post("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products")
            .then()
            .statusCode(201)
    }

    @Test
    fun deleteProdudct() {
        given()
            .auth()
            .oauth2(token)
            .`when`()
            .delete("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/edb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }

    @Test
    fun updateProduct() {
        RestAssured.defaultParser = Parser.JSON
        val form = ProductForm()
        form.title = "PUT Integrasjonstest - Tittel 1"
        form.description = "PUT Integrasjonstest - Beskrivelse 1"
        val product = ProductForm().toEntity(form)

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .body(product)
            .header("Content-type", "application/json")
            .put("/api/v1/projects/bbb4db69-edb2-431f-855a-4368e2bcddd1/products/edb4db69-edb2-431f-855a-4368e2bcddd1")
            .then()
            .statusCode(200)
    }
}

/*

    // Access token fra live keycloak server

    var ACCESS_TOKEN: String = ""
    val secret: String = ""
    val password: String = ""
    val username: String = ""

    val values = mapOf(
        "username" to "$username",
        "password" to "$password",
        "client_secret" to "$secret",
        "client_id" to "kravbank",
        "grant_type" to "password"
    )

    fun String.utf8(): String = URLEncoder.encode(this, "UTF-8")

    fun formData(data: Map<String, String>): HttpRequest.BodyPublisher? {
        val res = data.map { (k, v) -> "${(k.utf8())}=${v.utf8()}" }
            .joinToString("&")
        return HttpRequest.BodyPublishers.ofString(res)
    }

    @BeforeEach
    fun setup() {

        //Vi må ha Authorization bearer token fra keycloak for tilgang til ressursene
        //Setter opp en keycloak post request med credentials
        //Dette bør ideelt sett i fremtiden være en mocket request. Eventuelt skru av Authorization for %test


        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://krb-backend-auth.azurewebsites.net/realms/kravbank/protocol/openid-connect/token"))
            .POST(formData(values))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build()

        //get access token from keycloak response
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        ACCESS_TOKEN = response.body().split('"')[3]
    }


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

     */
