package org.kravbank.resource

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/")
class RootResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun rootPage(): String {
        return "Welcome to Kravbank"
    }
}