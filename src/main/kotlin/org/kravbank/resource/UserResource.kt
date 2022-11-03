package org.kravbank.resource

import io.quarkus.security.identity.SecurityIdentity
import org.jboss.resteasy.annotations.cache.NoCache
import org.kravbank.domain.User
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/api/users")
class UserResource {

    @Inject
    lateinit var identity: SecurityIdentity

    @GET
    @Path("/user")
    @NoCache
    @RolesAllowed("user")
    fun me(): String {

        var user = User(identity)

        //println(identity.principal.name)
        //print(identity.roles)
        //print(identity.credentials.toString())

        return "User role =>> ${user.id()}"
    }

    @GET
    @Path("/admin")
    @NoCache
    @RolesAllowed("admin")
    fun me2(): String {

        //println(identity.principal.name)
        //print(identity.roles)
        //print(identity.credentials.toString())

        return "Admin role =>> ${identity.principal.name}"
    }

}