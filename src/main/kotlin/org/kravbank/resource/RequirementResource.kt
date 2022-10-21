package org.kravbank.resource;

import org.kravbank.utils.requirement.dto.RequirementFormUpdate
import org.kravbank.service.RequirementService
import org.kravbank.utils.requirement.dto.RequirementFormCreate
import org.kravbank.utils.requirement.mapper.RequirementMapper
import org.kravbank.utils.requirement.mapper.RequirementUpdateMapper
import java.net.URI
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.enterprise.context.RequestScoped
import kotlin.streams.toList

@Path("/api/v1/projects/{projectRef}/requirements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
class RequirementResource(val requirementService: RequirementService) {
    @GET
    @Path("/{requirementRef}")
    fun getRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String
    ): Response {
        val requirement = requirementService.get(projectRef, requirementRef)
        val requirementDTO = RequirementMapper().fromEntity(requirement)
        return Response.ok(requirementDTO).build()
    }

    @GET
    fun listRequirements(@PathParam("projectRef") projectRef: String): Response {
        val requirementsDTO = requirementService.list(projectRef)
            .stream()
            .map(RequirementMapper()::fromEntity).toList()
        return Response.ok(requirementsDTO).build()
    }

    @Transactional
    @POST
    fun createRequirement(@PathParam("projectRef") projectRef: String, newRequirement: RequirementFormCreate): Response {
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
        val requirementDTO = RequirementMapper().fromEntity(requirement)
        // returnerer slettet req ref i body
        return Response.ok(requirementDTO.ref).build()
    }

    @PUT
    @Path("{requirementRef}")
    @Transactional
    fun updateRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        updatedRequirement: RequirementFormUpdate
    ): Response {
        val requirement = requirementService.update(projectRef, requirementRef, updatedRequirement)
        val requirementUpdateDTO = RequirementUpdateMapper().fromEntity(requirement)
        return Response.ok(requirementUpdateDTO).build()
    }
}