package org.kravbank.resource

import io.quarkus.security.Authenticated
import org.eclipse.microprofile.jwt.JsonWebToken
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/api/admin")
@Authenticated
class AdminResource {

    @Inject
    lateinit var jwt: JsonWebToken


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun admin(): String {
        //print("printer raw token admin --> ${jwt.rawToken}")
        print(jwt.subject)
        return "granted"
    }
}