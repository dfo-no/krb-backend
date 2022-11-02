package org.kravbank.resource

import io.quarkus.security.Authenticated
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/api/admin")
@Authenticated
class AdminResource {
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun admin(): String {
        return "granted"
    }

}