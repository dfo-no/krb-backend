package org.kravbank.java.api;



import org.kravbank.java.model.Publication;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/publications")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)

public class PublicationResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Publication> publications = Publication.listAll();
        return Response.ok(publications).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@PathParam("id") Long id) {
        return Publication.findByIdOptional(id)
                .map(publication -> Response.ok(publication).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Publication publication) {
        Publication.persist((publication));
        if (publication.isPersistent()) {
            return Response.created(URI.create("/publications" + publication.id)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path ("publication/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByDate(@PathParam("date") String date) {
        List <Publication> projects = Publication.list("SELECT p from Publication p WHERE p.date = ?1 ORDER BY id DESC", date);
        return Response.ok(projects).build();
    }

    @GET
    @Path ("bankid/{bankid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByBankId(@PathParam("bankid") String bankid) {
        return Publication.find("bankId", bankid)
                .singleResultOptional()
                .map(publication -> Response.ok(publication).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteById(@PathParam("id") Long id){
        boolean deleted = Publication.deleteById(id);
        if (deleted){
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}

