package org.kravbank.resource

import io.quarkus.oidc.OidcConfigurationMetadata
import io.quarkus.oidc.UserInfo
import io.quarkus.security.identity.SecurityIdentity
import org.jboss.resteasy.annotations.cache.NoCache
import java.security.Principal
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path

/*
Todo
Slettes senere
Denne klassen er kun for illustrasjon
 */
@Path("/test/api/v1/users")
@RolesAllowed("user")
class UserResource {

    @Inject
    lateinit var identity: SecurityIdentity

    @Inject
    lateinit var userInfo: UserInfo

    @Inject
    lateinit var configMetadata: OidcConfigurationMetadata

    //@Inject
    //lateinit var requestContext: ContainerRequestContext

    @GET
    @Path("/user")
    @NoCache

    fun me(): MutableMap<String, Principal> {
        // var user = User(identity)
        // val id = identity.roles
        //print(configMetadata.issuer.toString())
        // return "User role =>> ${user.id()} with ${id} "

        return mutableMapOf("username" to identity.principal)
    }

    @GET
    @Path("/info")
    @NoCache
    fun info(): MutableMap<String, String> {
        //return userInfo.toString()
        return mutableMapOf(
            "sub" to userInfo.getString("sub"),
            "email" to userInfo.getString("email")
        )
    }
}