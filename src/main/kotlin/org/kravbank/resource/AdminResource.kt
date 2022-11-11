package org.kravbank.resource

import io.quarkus.security.identity.SecurityIdentity
import org.eclipse.microprofile.jwt.JsonWebToken
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/*
Todo
Slettes senere
Denne klassen er kun for illustrasjon
 */

@Path("/api/admin")
//@Authenticated
@RolesAllowed("admin")
class AdminResource {

    @Inject
    lateinit var jwt: JsonWebToken

    @Inject
    lateinit var identity: SecurityIdentity

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun admin(): String {
        print("printer raw token admin --> ${jwt.rawToken}")
        print(jwt.subject)
        return "Granted! Admin role =>> ${identity.principal.name}"
    }

}