package org.kravbank.utils

import io.quarkus.test.keycloak.client.KeycloakTestClient
import org.kravbank.lang.NotFoundException

class KeycloakAccess {

    companion object {
        var keycloakClient = KeycloakTestClient()

        fun getAccessToken(userName: String): String {
            if (userName == "alice" || userName == "bob") return keycloakClient.getAccessToken(userName)
            throw NotFoundException("Username not found. Available usernames in Keycloak Test Client: bob or alice")
        }
    }
}
