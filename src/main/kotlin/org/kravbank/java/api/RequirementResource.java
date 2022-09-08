package org.kravbank.java.api;


import org.kravbank.java.model.Requirement;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/requirements")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)

public class RequirementResource {
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
}

