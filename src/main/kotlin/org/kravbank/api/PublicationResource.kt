package org.kravbank.api;



import org.kravbank.domain.Publication
import org.kravbank.service.PublicationService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path("/publications")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)
@RequestScoped

 class PublicationResource (val publicationService: PublicationService) {


    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    fun getPublication(@PathParam("id") id : Long): Response {
        if (publicationService.exists(id)){
            return Response.ok(publicationService.getPublication(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    //@Operation(summary = "List all publications")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listPublication(): List<Publication> =
        publicationService.listPublications();

    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createPublication(publication: Publication): Response? {
        publicationService.createPublication(publication)
        if (publication.isPersistent){
            return Response.created(URI.create("/publications" + publication.id)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deletePublicationById(@PathParam("id") id: Long): Response {
        val deleted = publicationService.deletePublication(id)
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
    fun updatePublication(@PathParam("id") id: Long, publication: Publication): Response {
        if (publicationService.exists(id)) {
            publicationService.updatePublication(id, publication)
            return Response.ok(publicationService.getPublication(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()

        }
    }
}


/*
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


    @Produces("application/json")
    @GET
    @Path("/{id}")
    fun getPublication(@PathParam("id") id : Long): Response {
        if (publicationService.exists(id)){
            return Response.ok(publicationService.getPublication(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    //@Operation(summary = "List all publications")
    @Produces("application/json")
    @GET
    fun listPublication(): kotlin.collections.List<Publication> =
        publicationService.listPublications();

    @Transactional
    @Produces("application/json")
    @POST
    fun createPublication(publication: Publication): Response? {
        publicationService.createPublication(publication)
        if (publication.isPersistent){
            return Response.created(URI.create("/publications" + publication.id)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
    }

    @DELETE
    @Path("{id}")
    @Produces("application/json")
    @Transactional
    fun deletePublicationById(@PathParam("id") id: Long): Response {
        val deleted = publicationService.deletePublication(id)
        return if (deleted) {
            //println(deleted)
            Response.noContent().build()
        } else Response.status(Response.Status.BAD_REQUEST).build()
    }

 */

    //UPDATE
