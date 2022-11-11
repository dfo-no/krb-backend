package org.kravbank.resource

import io.quarkus.oidc.OidcConfigurationMetadata
import io.quarkus.oidc.UserInfo
import io.quarkus.security.identity.SecurityIdentity
import org.jboss.resteasy.annotations.cache.NoCache
import org.kravbank.domain.User
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path

/*
Todo
Slettes senere
Denne klassen er kun for illustrasjon
 */
@Path("/api/users")
//@Authenticated
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
    // @RolesAllowed("user")
    @NoCache
    fun me(): String {
        var user = User(identity)

        val id = identity.roles

        print(configMetadata.issuer.toString())

        return "User role =>> ${user.id()} \n" +
                "With ${id} "
    }

    @GET
    @Path("/info")
    // @RolesAllowed("user")
    @NoCache
    fun info(): Any {
        return userInfo.toString()
    }

}