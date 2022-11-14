package org.kravbank.resource;

import org.kravbank.dao.RequirementVariantForm
import org.kravbank.service.RequirementVariantService
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
        val form = RequirementVariantForm().fromEntity(reqVariant)
        return Response.ok(form).build()
    }

    @GET
    fun listRequirementVariants(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
    ): Response {
        val form = requirementVariantService.list(projectRef, requirementRef)
            .stream()
            .map(RequirementVariantForm()::fromEntity)
            .toList()
        return Response.ok(form).build()
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
        val form = RequirementVariantForm().fromEntity(reqvariant)
        // returnerer slettet req variant ref i body
        return Response.ok(form.ref).build()
    }

    @PUT
    @Path("{requirementVariantRef}")
    @Transactional
    fun updateRequirementVariant(
        @PathParam("projectRef") projectRef: String,
        @PathParam("requirementRef") requirementRef: String,
        @PathParam("requirementVariantRef") requirementVariantRef: String,
        updatedReqVariant: RequirementVariantForm
    ): Response {
        val reqVariant =
            requirementVariantService.update(projectRef, requirementRef, requirementVariantRef, updatedReqVariant)
        val form = RequirementVariantForm().fromEntity(reqVariant)
        return Response.ok(form).build()
    }
}