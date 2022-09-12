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

    //@Operation(summary = "List all requirement")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listRequirement(): MutableList<Requirement> =
        requirementService.listRequirements();

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

    //UPDATE

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


/*


@GET
@Produces(MediaType.APPLICATION_JSON)
public Response getAll() {
    List<Requirement> requirements = Requirement.listAll();
    return Response.ok(requirements).build();
}

@GET
@Path("{id}")
@Produces(MediaType.APPLICATION_JSON)
public Response getOne(@PathParam("id") Long id) {
    return Requirement.findByIdOptional(id)
            .map(requirement -> Response.ok(requirement).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
}

@POST
@Transactional
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public Response create(Requirement requirement) {
    Requirement.persist((requirement));
    if (requirement.isPersistent()) {
        return Response.created(URI.create("/requirements" + requirement.id)).build();
    }
    return Response.status(Response.Status.BAD_REQUEST).build();
}


@GET
@Path ("requirement/{needid}")
@Produces(MediaType.APPLICATION_JSON)
public Response getByDate(@PathParam("needid") String needid) {
    List <Requirement> requirements = Requirement.list("SELECT p from Requirement p WHERE p.needId = ?1 ORDER BY id DESC", needid);
    return Response.ok(requirements).build();
}


/*
@GET
@Path ("requirement/{needid}")
@Produces(MediaType.APPLICATION_JSON)
public Response getByBankId(@PathParam("needid") String needid) {
    return Requirement.find("needId", needid)
            .singleResultOptional()
            .map(requirement -> Response.ok(requirement).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
}


 */
@DELETE
@Path("{id}")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public Response deleteById(@PathParam("id") Long id){
    boolean deleted = Requirement.deleteById(id);
    if (deleted){
        return Response.noContent().build();
    }
    return Response.status(Response.Status.BAD_REQUEST).build();
}

 */
