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


@Path("/api/users")
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
    @RolesAllowed("user")
    fun me(): String {

        var user = User(identity)

        //println(identity.principal.name)
        //print(identity.roles)
        //print(identity.credentials.toString())

        val id = identity.roles

        print(configMetadata.issuer.toString())

        //print(requestContext.request.method)


        return "User role =>> ${user.id()} \n" +
                "With ${id} "
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

    @GET
    @Path("/info")
    @RolesAllowed("user")
    @NoCache
    fun info(): Any {

        return userInfo.toString()


    }

}