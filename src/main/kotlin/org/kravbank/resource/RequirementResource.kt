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

@Path("/api/v1/projects/{projectRef}/needs/{needRef}/requirements")


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@Authenticated
class RequirementResource(val requirementService: RequirementService) {
    @GET
    @Path("/{requirementRef}")
    fun getRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        @PathParam("requirementRef") requirementRef: String
    ): RequirementForm {
        val requirement = requirementService.get(projectRef, needRef, requirementRef)
        return RequirementForm().fromEntity(requirement)
    }

    @GET
    fun listRequirements(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String
    ): List<RequirementForm> {
        return requirementService.list(projectRef, needRef)
            .stream()
            .map(RequirementForm()::fromEntity)
            .toList()
    }

    @Transactional
    @POST
    fun createRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        newRequirement: RequirementForm
    )
            : Response {
        val requirement = requirementService.create(projectRef, needRef, newRequirement)
        //returnerer ny requirement ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/requirements/" + requirement.ref))
            .build()
    }

    @DELETE
    @Path("/{requirementRef}")
    @Transactional
    fun deleteRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        @PathParam("requirementRef") requirementRef: String
    ): Response {
        requirementService.delete(projectRef, needRef, requirementRef)
        return Response.ok(requirementRef).build()
    }

    @PUT
    @Path("{requirementRef}")
    @Transactional
    fun updateRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        @PathParam("requirementRef") requirementRef: String,
        updatedRequirement: RequirementForm
    ): RequirementForm {
        val requirement = requirementService.update(projectRef, needRef, requirementRef, updatedRequirement)
        return RequirementForm().fromEntity(requirement)
    }
}