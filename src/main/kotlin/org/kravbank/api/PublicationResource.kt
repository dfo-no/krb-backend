package org.kravbank.api;

import org.kravbank.utils.form.publication.PublicationForm
import org.kravbank.utils.form.publication.PublicationFormUpdate
import org.kravbank.service.PublicationService
import org.kravbank.utils.mapper.codelist.CodelistUpdateMapper
import org.kravbank.utils.mapper.publication.PublicationMapper
import org.kravbank.utils.mapper.publication.PublicationUpdateMapper
import java.net.URI
import java.util.ArrayList
import java.util.ResourceBundle
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

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
        //mapper fra entity
        val publicationDTO = PublicationMapper().fromEntity(publication)
        return Response.ok(publicationDTO).build()
    }

    //LIST PUBLICATIONS
    @GET
    fun listPublications(@PathParam("projectref") projectRef: String): Response {
        val publications = publicationService.list(projectRef)
        val publicationsDTO = ArrayList<PublicationForm>()
        //mapper fra entity
        for (n in publications) publicationsDTO.add(PublicationMapper().fromEntity(n))
        return Response.ok(publicationsDTO).build()
    }
    //CREATE PUBLICATION
    @Transactional
    @POST
    fun createPublication(@PathParam("projectref") projectRef: String, publication: PublicationForm): Response {
        val publication = publicationService.create(projectRef, publication)
        //sender ny publication ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/publications/" + publication.ref)).build()
    }

    //DELETE PUBLICATION
    @DELETE
    @Path("/{publicationref}")
    @Transactional
    fun deletePublication(
        @PathParam("projectref") projectRef: String, @PathParam("publicationref") publicationRef: String
    ): Response {
        publicationService.delete(projectRef, publicationRef)
        return Response.noContent().build()
    }

    //UPDATE PUBLICATION
    @PUT
    @Path("{publicationref}")
    @Transactional
    fun updatePublication(
        @PathParam("projectref") projectRef: String,
        @PathParam("publicationref") publicationRef: String,
        updatePublication: PublicationFormUpdate
    ): Response {
        val publication = publicationService.update(projectRef, publicationRef, updatePublication)
        // mapper fra entity
        val publicationUpdateDTO = PublicationUpdateMapper().fromEntity(publication)
        return Response.ok(publicationUpdateDTO).build()
    }
}