package org.kravbank.domain

import io.quarkus.security.identity.SecurityIdentity


class User(val identity: SecurityIdentity) {

    lateinit var userName: String

    fun id(): String {
        userName = identity.principal.name.toString()
        return userName
    }
}