package org.kravbank.api;

import org.kravbank.utils.form.publication.PublicationForm
import org.kravbank.utils.form.publication.PublicationFormUpdate
import org.kravbank.service.PublicationService
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
        @PathParam("projectref") projectRef: String,
        @PathParam("publicationref") publicationRef: String
    ): Response =
        publicationService.get(projectRef, publicationRef)

    //LIST PUBLICATIONS
    @GET
    fun listPublications(@PathParam("projectref") projectRef: String): Response =
        publicationService.list(projectRef)

    //CREATE PUBLICATION
    @Transactional
    @POST
    fun createPublication(@PathParam("projectref") projectRef: String, publication: PublicationForm): Response =
        publicationService.create(projectRef, publication)

    //DELETE PUBLICATION
    @DELETE
    @Path("/{publicationref}")
    @Transactional
    fun deletePublication(
        @PathParam("projectref") projectRef: String,
        @PathParam("publicationref") publicationRef: String
    ): Response =
        publicationService.delete(projectRef, publicationRef)

    //UPDATE PUBLICATION
    @PUT
    @Path("{publicationref}")
    @Transactional
    fun updatePublication(
        @PathParam("projectref") projectRef: String,
        @PathParam("publicationref") publicationRef: String,
        publication: PublicationFormUpdate
    ): Response =
        publicationService.update(projectRef, publicationRef, publication)
}