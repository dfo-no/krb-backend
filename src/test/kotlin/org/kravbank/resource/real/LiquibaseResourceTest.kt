package org.kravbank.resource.real

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import liquibase.changelog.RanChangeSet
import org.junit.jupiter.api.Test
import org.kravbank.utils.KeycloakAccess

@QuarkusTest
class LiquibaseResourceTest {

    private val token = KeycloakAccess.getAccessToken("alice")

    /**
     *
     * This test retrieves a list of RanChangeSet objects representing the current state of the Liquibase changelog.
     * It is a way to ensure that the LiquibaseResource endpoint is returning data in the expected format and working properly
     *
     */

    @Test
    fun `assert liquibase changelogs is ok`() {

        given()
            .auth()
            .oauth2(token)
            .`when`()
            .get("/api/v1/liquibase")
            .then()
            .statusCode(200)
            .extract().body().jsonPath().getList<RanChangeSet>("")
            .also {
                assert(it.isNotEmpty())
                assert(it.size > 5)
                //  TODO  val changeLogToCheck = it[6].toString().substringAfter("changeLog=").substringBefore(",") assertEquals("db/changeLog.sql", changeLogToCheck)

            }
    }
}