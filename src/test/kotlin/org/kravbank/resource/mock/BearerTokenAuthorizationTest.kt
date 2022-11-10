package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.smallrye.jwt.build.Jwt
import org.junit.jupiter.api.Test


@QuarkusTest
class BearerTokenAuthorizationTest {

    @Test
    fun testBearerToken() {

        // print(RestAssured.given().auth().oauth2(getAccessToken("bob", mutableSetOf("user"))))

        /*RestAssured.given().auth().oauth2(getAccessToken("alice", mutableSetOf("user")))
            .`when`()["/api/users/user"]
            .then()
            .statusCode(200) // the test endpoint returns the name extracted from the injected SecurityIdentity Principal
            .body("userName", equalTo("alice"))

         */
    }

    @Test
    fun getAccessToken(userName: String, groups: Set<String>): String? {

        return Jwt.preferredUserName(userName)
            .groups(groups)
            .issuer("http://localhost:38953/realms/quarkus\n")
            .audience("https://service.example.com")
            .sign()
    }


}