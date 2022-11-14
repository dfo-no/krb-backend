package org.kravbank.domain

import io.quarkus.security.identity.SecurityIdentity


/*
Todo
Slettes senere
Denne klassen er kun for illustrasjon
 */
class User(val identity: SecurityIdentity) {

    lateinit var userName: String
    fun id(): String {
        userName = identity.principal.name.toString()
        return userName
    }
}