package org.kravbank.api;

import org.kravbank.utils.form.requirement.RequirementForm
import org.kravbank.utils.form.requirement.RequirementFormUpdate
import org.kravbank.service.RequirementService
import org.kravbank.utils.mapper.requirement.RequirementMapper
import org.kravbank.utils.mapper.requirement.RequirementUpdateMapper
import org.kravbank.utils.mapper.requirementvariant.RequirementVariantMapper
import java.net.URI
import java.util.ArrayList
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.enterprise.context.RequestScoped

@Path("/api/v1/projects/{projectRef}/requirements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
class RequirementResource(val requirementService: RequirementService) {
    //GET REQUIREMENT
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

    //LIST REQUIREMENTS
    @GET
    fun listRequirements(@PathParam("projectRef") projectRef: String): Response {
        val requirements = requirementService.list(projectRef)
        val requirementsDTO = ArrayList<RequirementForm>()
        for (n in requirements) requirementsDTO.add(RequirementMapper().fromEntity(n))
        return Response.ok(requirementsDTO).build()
    }

    //CREATE REQUIREMENT
    @Transactional
    @POST
    fun createRequirement(@PathParam("projectRef") projectRef: String, requirementDTO: RequirementForm): Response {
        val requirement = requirementService.create(projectRef, requirementDTO)
        return Response.created(URI.create("/api/v1/projects/$projectRef/requirements/" + requirement.ref)).build()
    }

    //DELETE REQUIREMENT
    @DELETE
    @Path("/{requirementRef}")
    @Transactional
    fun deleteRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String
    ): Response {
        val requirement = requirementService.delete(projectRef, requirementRef)
        val requirementDTO = RequirementMapper().fromEntity(requirement)
        // sender slettet req ref i body
        return Response.ok(requirementDTO.ref).build()
    }

    //UPDATE REQUIREMENT
    @PUT
    @Path("{requirementRef}")
    @Transactional
    fun updateRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        requirement: RequirementFormUpdate
    ): Response {
        val requirement = requirementService.update(projectRef, requirementRef, requirement)
        val requirementUpdateDTO = RequirementUpdateMapper().fromEntity(requirement)
        return Response.ok(requirementUpdateDTO).build()
    }
}