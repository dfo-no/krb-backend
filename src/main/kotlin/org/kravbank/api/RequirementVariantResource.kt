package org.kravbank.api;


import org.kravbank.form.requirementvariant.RequirementVariantForm
import org.kravbank.form.requirementvariant.RequirementVariantFormUpdate
import org.kravbank.service.RequirementVariantService
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.enterprise.context.RequestScoped

@Path("/api/v1/projects/{projectRef}/requirements/{requirementRef}/requirementvariants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
class RequirementVariantResource(val requirementVariantService: RequirementVariantService) {


    //GET REQUIREMENTVARIANT
    @GET
    @Path("/{requirementVariantRef}")
    fun getRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String
    ): Response =
        requirementVariantService.getRequirementVariantByRefFromService(projectRef, requirementRef, requirementVariantRef)

    //LIST REQUIREMENTVARIANTS
    @GET
    fun listRequirementVariants(@PathParam("projectRef") projectRef: String, @PathParam("requirementRef") requirementRef: String,  ): Response =
        requirementVariantService.listRequirementVariantsFromService(projectRef, requirementRef)

    //CREATE REQUIREMENTVARIANT
    @Transactional
    @POST
    fun createRequirementVariant(@PathParam("projectRef") projectRef: String, @PathParam("requirementRef") requirementRef: String, requirementVariant: RequirementVariantForm): Response =
        requirementVariantService.createRequirementVariantFromService(projectRef, requirementRef, requirementVariant)

    //DELETE REQUIREMENTVARIANT
    @DELETE
    @Path("/{requirementVariantRef}")
    @Transactional
    fun deleteRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String
    ): Response =
        requirementVariantService.deleteRequirementVariantFromService(projectRef, requirementVariantRef)

    //UPDATE REQUIREMENTVARIANT
    @PUT
    @Path("{requirementVariantRef}")
    @Transactional
    fun updateRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String,
        requirementVariant: RequirementVariantFormUpdate
    ): Response =
        requirementVariantService.updateRequirementVariantFromService(projectRef, requirementVariantRef, requirementVariant)
}


