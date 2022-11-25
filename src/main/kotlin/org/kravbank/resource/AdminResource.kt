package org.kravbank.resource

import org.eclipse.microprofile.jwt.JsonWebToken
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/test/api/v1/admin")
@RolesAllowed("admin")
class AdminResource {

    @Inject
    lateinit var jwt: JsonWebToken

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun admin(): MutableMap<String, String> {
        return mutableMapOf(
            "subject" to jwt.subject,
            "preferred_username" to jwt.getClaim("preferred_username")
        )
    }
}