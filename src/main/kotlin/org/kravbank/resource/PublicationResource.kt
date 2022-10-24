package org.kravbank.resource;


import org.kravbank.dao.PublicationForm
import org.kravbank.service.PublicationService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response
import kotlin.streams.toList

@Path("/api/v1/projects/{projectref}/publications")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@RequestScoped
class PublicationResource(val publicationService: PublicationService) {

    @GET
    @Path("/{publicationref}")
    fun getPublication(
        @PathParam("projectref") projectRef: String, @PathParam("publicationref") publicationRef: String
    ): Response {
        val publication = publicationService.get(projectRef, publicationRef)
        val form = PublicationForm().fromEntity(publication)
        return Response.ok(form).build()
    }

    @GET
    fun listPublications(@PathParam("projectref") projectRef: String): Response {
        val form = publicationService.list(projectRef)
            .stream()
            .map(PublicationForm()::fromEntity).toList()
        return Response.ok(form).build()
    }

    @Transactional
    @POST
    fun createPublication(@PathParam("projectref") projectRef: String, newPublication: PublicationForm): Response {
        val publication = publicationService.create(projectRef, newPublication)
        //returnerer ny publication ref i respons header
        return Response.created(URI.create("/api/v1/projects/$projectRef/publications/" + publication.ref))
            .build()
    }

    @DELETE
    @Path("/{publicationref}")
    @Transactional
    fun deletePublication(
        @PathParam("projectref") projectRef: String, @PathParam("publicationref") publicationRef: String
    ): Response {
        val publication = publicationService.delete(projectRef, publicationRef)

        //val form = PublicationForm().fromEntity(publication)
        // returnerer slettet publication ref i body
        return Response.ok(publication.ref).build()
    }

    @PUT
    @Path("{publicationref}")
    @Transactional
    fun updatePublication(
        @PathParam("projectref") projectRef: String,
        @PathParam("publicationref") publicationRef: String,
        updatedPublication: PublicationForm
    ): Response {
        val publication = publicationService.update(projectRef, publicationRef, updatedPublication)
        val form = PublicationForm().fromEntity(publication)
        return Response.ok(form).build()
    }
}