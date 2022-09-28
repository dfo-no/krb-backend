package org.kravbank.api;

import org.kravbank.utils.form.requirement.RequirementForm
import org.kravbank.utils.form.requirement.RequirementFormUpdate
import org.kravbank.service.RequirementService
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
    ): Response =
        requirementService.getRequirementByRefFromService(projectRef, requirementRef)

    //LIST REQUIREMENTS
    @GET
    fun listRequirements(@PathParam("projectRef") projectRef: String): Response =
        requirementService.listRequirementsFromService(projectRef)

    //CREATE REQUIREMENT
    @Transactional
    @POST
    fun createRequirement(@PathParam("projectRef") projectRef: String, requirement: RequirementForm): Response =
        requirementService.createRequirementFromService(projectRef, requirement)

    //DELETE REQUIREMENT
    @DELETE
    @Path("/{requirementRef}")
    @Transactional
    fun deleteRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String
    ): Response =
        requirementService.deleteRequirementFromService(projectRef, requirementRef)

    //UPDATE REQUIREMENT
    @PUT
    @Path("{requirementRef}")
    @Transactional
    fun updateRequirement(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        requirement: RequirementFormUpdate
    ): Response =
        requirementService.updateRequirementFromService(projectRef, requirementRef, requirement)
}