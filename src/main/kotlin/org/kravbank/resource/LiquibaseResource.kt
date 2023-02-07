package org.kravbank.resource

import io.quarkus.liquibase.LiquibaseFactory
import liquibase.changelog.ChangeSetStatus
import liquibase.changelog.RanChangeSet
import java.util.stream.Collectors
import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response


@Path("/liquibase")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@RolesAllowed("admin")
class LiquibaseResource {

    @Inject
    lateinit var liquibaseFactory: LiquibaseFactory

    @GET
    fun getChangeLogs(): Response {
        liquibaseFactory.createLiquibase().use { liquibase ->
            val tmp: List<RanChangeSet> = liquibase
                .getChangeSetStatuses(liquibaseFactory.createContexts())
                .stream()
                .map(ChangeSetStatus::getRanChangeSet)
                .collect(Collectors.toList())
            return Response.ok(tmp).build()
        }
    }
}