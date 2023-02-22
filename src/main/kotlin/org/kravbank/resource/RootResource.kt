package org.kravbank.resource

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/")
class RootResource {

    @GET
    fun get(): Response = Response.ok("Kravbank").build()
}