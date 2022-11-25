package org.kravbank.resource.utils

import io.quarkus.test.keycloak.client.KeycloakTestClient
import javax.ws.rs.BadRequestException

class KeycloakAccess {

    companion object {
        var keycloakClient = KeycloakTestClient()

        fun getAccessToken(userName: String): String {
            if (userName == "alice" || userName == "bob") return keycloakClient.getAccessToken(userName)
            throw BadRequestException("Available usernames in Keycloak Test Client: bob or alice")
        }
    }
}
