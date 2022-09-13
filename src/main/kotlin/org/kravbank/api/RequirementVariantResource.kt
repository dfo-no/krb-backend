package org.kravbank.api;

import org.kravbank.domain.RequirementVariant
import org.kravbank.service.RequirementVariantService
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import javax.enterprise.context.RequestScoped

@Path("/requirementvariants")
@RequestScoped
class RequirementVariantResource(val requirementVariantService: RequirementVariantService) {
    //GET REQUIREMENT VARIANT
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    fun getRequirementVariant(@PathParam("id") id: Long): Response {
        if (requirementVariantService.exists(id)) {
            return Response.ok(requirementVariantService.getRequirementVariant(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    //LIST REQUIREMENT VARIANTS
    //@Operation(summary = "List all requirementvariant")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listRequirementVariants(): MutableList<RequirementVariant> =
        requirementVariantService.listRequirementVariants();

    //CREATE REQUIREMENT VARIANT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createRequirementVariant(publication: RequirementVariant): Response? {
        requirementVariantService.createRequirementVariant(publication)
        if (publication.isPersistent) {
            return Response.created(URI.create("/requirementvariants" + publication.id)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
    }

    //DELETE REQUIREMENT VARIANT
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deleteRequirementVariantById(@PathParam("id") id: Long): Response {
        val deleted = requirementVariantService.deleteRequirementVariant(id)
        return if (deleted) {
            //println(deleted)
            Response.noContent().build()
        } else Response.status(Response.Status.BAD_REQUEST).build()
    }

    //UPDATE REQUIREMENT VARIANT
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateRequirementVariant(@PathParam("id") id: Long, publication: RequirementVariant): Response {
        if (requirementVariantService.exists(id)) {
            requirementVariantService.updateRequirementVariant(id, publication)
            return Response.ok(requirementVariantService.getRequirementVariant(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }
}

