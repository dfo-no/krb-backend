package org.kravbank.resource

import io.quarkus.security.Authenticated
import org.kravbank.dao.RequirementForm
import org.kravbank.service.RequirementService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.streams.toList

@Path("/api/v1/projects/{projectRef}/requirements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@Authenticated
class RequirementResource(val requirementService: RequirementService) {
    @GET
    @Path("/{requirementRef}")
    fun getRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String
    ): Response {
        val requirement = requirementService.get(projectRef, requirementRef)
        val form = RequirementForm().fromEntity(requirement)
        return Response.ok(form).build()
    }

    @GET
    fun listRequirements(@PathParam("projectRef") projectRef: String): Response {
        val form = requirementService.list(projectRef)
            .stream()
            .map(RequirementForm()::fromEntity)
            .toList()
        return Response.ok(form).build()
    }

    @Transactional
    @POST
    fun createRequirement(@PathParam("projectRef") projectRef: String, newRequirement: RequirementForm): Response {
        val requirement = requirementService.create(projectRef, newRequirement)
        //returnerer ny requirement ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/requirements/" + requirement.ref))
            .build()
    }

    @DELETE
    @Path("/{requirementRef}")
    @Transactional
    fun deleteRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String
    ): Response {
        val requirement = requirementService.delete(projectRef, requirementRef)
        val form = RequirementForm().fromEntity(requirement)
        // returnerer slettet req ref i body
        return Response.ok(form.ref).build()
    }

    @PUT
    @Path("{requirementRef}")
    @Transactional
    fun updateRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        updatedRequirement: RequirementForm
    ): Response {
        val requirement = requirementService.update(projectRef, requirementRef, updatedRequirement)
        val form = RequirementForm().fromEntity(requirement)
        return Response.ok(form).build()
    }
}