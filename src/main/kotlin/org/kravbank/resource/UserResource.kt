package org.kravbank.resource

import io.quarkus.security.Authenticated
import io.quarkus.security.identity.SecurityIdentity
import org.jboss.resteasy.annotations.cache.NoCache
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/api/users")
class UserResource {

    lateinit var identity: SecurityIdentity

    @GET
    @Path("/me")
    @NoCache
    @Authenticated
    fun me(): String {
        return "ID Name =>>>${identity.principal.name}"
    }
}