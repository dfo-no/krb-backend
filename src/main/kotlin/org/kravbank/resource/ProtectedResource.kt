package org.kravbank.resource

import io.quarkus.security.ForbiddenException
import io.quarkus.security.identity.SecurityIdentity
import io.smallrye.mutiny.Uni
import org.keycloak.representations.idm.authorization.Permission
import javax.inject.Inject
import javax.security.auth.AuthPermission
import javax.ws.rs.GET
import javax.ws.rs.Path


@Path("/api/protected")
class ProtectedResource {
    @Inject
    lateinit var identity: SecurityIdentity
    
    @GET
    fun get(): Uni<List<Permission>> {
        return identity!!.checkPermission(AuthPermission("{resource_name}")).onItem()
            .transform<List<Permission>> { granted: Boolean ->
                if (granted) {
                    return@transform identity!!.getAttribute<Any>("permissions") as List<Permission>?
                }
                throw ForbiddenException()
            }
    }
}