package org.kravbank.api;

import org.kravbank.domain.Publication
import org.kravbank.form.product.ProductFormUpdate
import org.kravbank.form.publication.PublicationForm
import org.kravbank.form.publication.PublicationFormUpdate
import org.kravbank.service.ProjectService
import org.kravbank.service.PublicationService
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/api/v1/projects/{projectref}/publications")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)
@RequestScoped
class PublicationResource(val publicationService: PublicationService, val projectService: ProjectService) {
    //GET PUBLICATION
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{publicationref}")
    fun getPublication(
        @PathParam("projectref") projectRef: String,
        @PathParam("publicationref") publicationRef: String
    ): Response =
        publicationService.getPublicationByRefFromService(projectRef, publicationRef)


    //LIST PUBLICATIONS
    //@Operation(summary = "List all publications")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listPublications(@PathParam("projectref") projectRef: String): Response =
        publicationService.listPublicationsFromService(projectRef)

    //CREATE PUBLICATION
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createPublication(@PathParam("projectref") projectRef: String, publication: PublicationForm): Response =
        publicationService.createPublicationFromService(projectRef, publication)

    //DELETE PUBLICATION
    @DELETE
    @Path("/{publicationref}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deletePublication(
        @PathParam("projectref") projectRef: String,
        @PathParam("publicationref") publicationRef: String
    ): Response =
        publicationService.deletePublicationFromService(projectRef, publicationRef)


    //UPDATE PUBLICATION

    /**
     * todo
     * research "description"
     */
    @PUT
    @Path("{publicationref}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateProduct(
        @PathParam("projectref") projectRef: String,
        @PathParam("publicationref") publicationRef: String,
        publication: PublicationFormUpdate
    ): Response =
        publicationService.updatePublicationFromService(projectRef, publicationRef, publication)
}