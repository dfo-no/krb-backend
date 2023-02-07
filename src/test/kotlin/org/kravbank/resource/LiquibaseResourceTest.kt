package org.kravbank.resource.real

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import liquibase.changelog.RanChangeSet
import org.junit.jupiter.api.Test
import org.kravbank.utils.KeycloakAccess

@QuarkusTest
class LiquibaseResourceTest {

    private val token = KeycloakAccess.getAccessToken("alice")

    @Test
    fun `assert liquibase changelogs is ok`() {

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/liquibase")
            .then()
            .statusCode(200)
            .extract().`as`(List::class.java)
            .let {
                it as List<RanChangeSet>
                println(it)
                assert(it.isNotEmpty())
            }
    }
}