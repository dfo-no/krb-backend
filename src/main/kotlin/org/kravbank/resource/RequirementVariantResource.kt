package org.kravbank.resource;

import org.kravbank.utils.form.requirementvariant.RequirementVariantForm
import org.kravbank.utils.form.requirementvariant.RequirementVariantFormUpdate
import org.kravbank.service.RequirementVariantService
import org.kravbank.utils.mapper.requirementvariant.RequirementVariantMapper
import java.net.URI
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.enterprise.context.RequestScoped
import kotlin.streams.toList

@Path("/api/v1/projects/{projectRef}/requirements/{requirementRef}/requirementvariants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
class RequirementVariantResource(val requirementVariantService: RequirementVariantService) {

    @GET
    @Path("/{requirementVariantRef}")
    fun getRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String
    ): Response {
        val reqVariant = requirementVariantService.get(projectRef, requirementRef, requirementVariantRef)
        val reqVariantDTO = RequirementVariantMapper().fromEntity(reqVariant)
        return Response.ok(reqVariantDTO).build()
    }

    @GET
    fun listRequirementVariants(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
    ): Response {
        val reqVariantsDTO = requirementVariantService.list(projectRef, requirementRef)
            .stream()
            .map(RequirementVariantMapper()::fromEntity).toList()
        return Response.ok(reqVariantsDTO).build()
    }

    @Transactional
    @POST
    fun createRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        newReqVariant: RequirementVariantForm
    ): Response {
        val reqVariant = requirementVariantService.create(projectRef, requirementRef, newReqVariant)
        //returnerer ny req variant ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/requirements/$requirementRef/requirementvariants/" + reqVariant.ref))
            .build()
    }

    @DELETE
    @Path("/{requirementVariantRef}")
    @Transactional
    fun deleteRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String
    ): Response {
        val reqvariant = requirementVariantService.delete(projectRef, requirementRef, requirementVariantRef)
        val reqVariantDTO = RequirementVariantMapper().fromEntity(reqvariant)
        // returnerer slettet req variant ref i body
        return Response.ok(reqVariantDTO.ref).build()
    }

    @PUT
    @Path("{requirementVariantRef}")
    @Transactional
    fun updateRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String,
        updatedReqVariant: RequirementVariantFormUpdate
    ): Response {
        val reqVariant =
            requirementVariantService.update(projectRef, requirementRef, requirementVariantRef, updatedReqVariant)
        val reqVariantUpdateDTO = RequirementVariantMapper().fromEntity(reqVariant)
        return Response.ok(reqVariantUpdateDTO).build()
    }
}