package org.kravbank.resource;

import org.kravbank.utils.form.publication.PublicationForm
import org.kravbank.utils.form.publication.PublicationFormUpdate
import org.kravbank.service.PublicationService
import org.kravbank.utils.mapper.publication.PublicationMapper
import org.kravbank.utils.mapper.publication.PublicationUpdateMapper
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
    //GET PUBLICATION
    @GET
    @Path("/{publicationref}")
    fun getPublication(
        @PathParam("projectref") projectRef: String, @PathParam("publicationref") publicationRef: String
    ): Response {
        val publication = publicationService.get(projectRef, publicationRef)
        val publicationDTO = PublicationMapper().fromEntity(publication)
        return Response.ok(publicationDTO).build()
    }

    @GET
    fun listPublications(@PathParam("projectref") projectRef: String): Response {
        val publicationsDTO = publicationService.list(projectRef)
            .stream()
            .map(PublicationMapper()::fromEntity).toList()
        return Response.ok(publicationsDTO).build()
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
        val publicationDTO = PublicationMapper().fromEntity(publication)
        // returnerer slettet publication ref i body
        return Response.ok(publicationDTO.ref).build()
    }

    @PUT
    @Path("{publicationref}")
    @Transactional
    fun updatePublication(
        @PathParam("projectref") projectRef: String,
        @PathParam("publicationref") publicationRef: String,
        updatedPublication: PublicationFormUpdate
    ): Response {
        val publication = publicationService.update(projectRef, publicationRef, updatedPublication)
        val publicationUpdateDTO = PublicationUpdateMapper().fromEntity(publication)
        return Response.ok(publicationUpdateDTO).build()
    }
}