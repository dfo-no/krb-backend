package org.kravbank.api;

import org.kravbank.utils.form.requirementvariant.RequirementVariantForm
import org.kravbank.utils.form.requirementvariant.RequirementVariantFormUpdate
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
    //GET REQUIREMENT VARIANT
    @GET
    @Path("/{requirementVariantRef}")
    fun getRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String
    ): Response =
        requirementVariantService.get(
            projectRef,
            requirementRef,
            requirementVariantRef
        )

    //LIST REQUIREMENT VARIANTS
    @GET
    fun listRequirementVariants(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
    ): Response =
        requirementVariantService.list(projectRef, requirementRef)

    //CREATE REQUIREMENT VARIANT
    @Transactional
    @POST
    fun createRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        requirementVariant: RequirementVariantForm
    ): Response =
        requirementVariantService.create(projectRef, requirementRef, requirementVariant)

    //DELETE REQUIREMENT VARIANT
    @DELETE
    @Path("/{requirementVariantRef}")
    @Transactional
    fun deleteRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String
    ): Response =
        requirementVariantService.delete(projectRef, requirementRef, requirementVariantRef)

    //UPDATE REQUIREMENT VARIANT
    @PUT
    @Path("{requirementVariantRef}")
    @Transactional
    fun updateRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String,
        requirementVariant: RequirementVariantFormUpdate
    ): Response =
        requirementVariantService.update(
            projectRef,
            requirementRef,
            requirementVariantRef,
            requirementVariant
        )
}