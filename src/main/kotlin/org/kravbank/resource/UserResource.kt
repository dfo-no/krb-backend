package org.kravbank.resource

import io.quarkus.oidc.UserInfo
import io.quarkus.security.identity.SecurityIdentity
import org.jboss.resteasy.annotations.cache.NoCache
import java.security.Principal
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/test/api/v1/users")
@RolesAllowed("user")
class UserResource {

    @Inject
    lateinit var identity: SecurityIdentity

    @Inject
    lateinit var userInfo: UserInfo

    @GET
    @Path("/user")
    @NoCache
    fun me(): MutableMap<String, Principal> {
        return mutableMapOf("username" to identity.principal)
    }

    @GET
    @Path("/info")
    @NoCache
    fun info(): MutableMap<String, String> {
        return mutableMapOf(
            "sub" to userInfo.getString("sub"),
            "email" to userInfo.getString("email")
        )
    }
}