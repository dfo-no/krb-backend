package org.kravbank.api;


import org.kravbank.domain.Requirement
import org.kravbank.service.RequirementService
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import javax.enterprise.context.RequestScoped

@Path("/requirements")
@RequestScoped
class RequirementResource(val requirementService: RequirementService) {

    //GET REQUIREMENT
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    fun getRequirement(@PathParam("id") id: Long): Response {
        if (requirementService.exists(id)) {
            return Response.ok(requirementService.getRequirement(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    //LIST REQUIREMENTS
    //@Operation(summary = "List all requirement")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listRequirements(): MutableList<Requirement> =
        requirementService.listRequirements();

    //CREATE REQUIREMENT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createRequirement(publication: Requirement): Response? {
        requirementService.createRequirement(publication)
        if (publication.isPersistent) {
            return Response.created(URI.create("/requirement" + publication.id)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
    }

    //DELETE REQUIREMENT
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deleteRequirementById(@PathParam("id") id: Long): Response {
        val deleted = requirementService.deleteRequirement(id)
        return if (deleted) {
            //println(deleted)
            Response.noContent().build()
        } else Response.status(Response.Status.BAD_REQUEST).build()
    }

    //UPDATE REQUIREMENT
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateRequirement(@PathParam("id") id: Long, publication: Requirement): Response {
        if (requirementService.exists(id)) {
            requirementService.updateRequirement(id, publication)
            return Response.ok(requirementService.getRequirement(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()

        }
    }
}