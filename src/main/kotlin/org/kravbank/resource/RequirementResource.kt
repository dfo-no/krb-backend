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
    ): RequirementForm {
        val requirement = requirementService.get(projectRef, requirementRef)
        return RequirementForm().fromEntity(requirement)
    }

    @GET
    fun listRequirements(@PathParam("projectRef") projectRef: String): List<RequirementForm> {
        return requirementService.list(projectRef)
            .stream()
            .map(RequirementForm()::fromEntity)
            .toList()
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
    ): RequirementForm {
        val requirement = requirementService.update(projectRef, requirementRef, updatedRequirement)
        return RequirementForm().fromEntity(requirement)
    }
}