package org.kravbank.resource

import javax.annotation.security.RolesAllowed
import javax.ws.rs.Path

@Path("/migration/")
@RolesAllowed("user")
class MigrationResource {

}