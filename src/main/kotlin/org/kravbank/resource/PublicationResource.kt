package org.kravbank.resource

import io.quarkus.security.Authenticated
import org.kravbank.dao.PublicationForm
import org.kravbank.service.PublicationService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("/api/v1/projects/{projectref}/publications")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@RequestScoped
@Authenticated
class PublicationResource(val publicationService: PublicationService) {

    @GET
    @Path("/{publicationref}")
    fun getPublication(
        @PathParam("projectref") projectRef: String, @PathParam("publicationref") publicationRef: String
    ): PublicationForm {
        val publication = publicationService.get(projectRef, publicationRef)
        return PublicationForm().fromEntity(publication)
    }

    @GET
    fun listPublications(@PathParam("projectref") projectRef: String): List<PublicationForm> {
        return publicationService
            .list(projectRef)
            .stream()
            .map(PublicationForm()::fromEntity)
            .toList()
    }

    @Transactional
    @POST
    fun createPublication(@PathParam("projectref") projectRef: String, newPublication: PublicationForm): Response {
        val publication = publicationService.create(projectRef, newPublication)
        //returnerer ny publication ref i respons header
        return Response.created(URI.create("/api/v1/projects/$projectRef/publications/" + publication.ref)).build()
    }

    @DELETE
    @Path("/{publicationref}")
    @Transactional
    fun deletePublication(
        @PathParam("projectref") projectRef: String, @PathParam("publicationref") publicationRef: String
    ): Response {
        publicationService.delete(projectRef, publicationRef)
        return Response.noContent().build()
    }

    @PUT
    @Path("{publicationref}")
    @Transactional
    fun updatePublication(
        @PathParam("projectref") projectRef: String,
        @PathParam("publicationref") publicationRef: String,
        updatedPublication: PublicationForm
    ): PublicationForm {
        val publication = publicationService.update(projectRef, publicationRef, updatedPublication)
        return PublicationForm().fromEntity(publication)
    }
}