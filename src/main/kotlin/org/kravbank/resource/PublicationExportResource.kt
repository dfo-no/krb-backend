package org.kravbank.resource

import org.kravbank.domain.PublicationExport
import org.kravbank.repository.ProjectRepository
import org.kravbank.service.PublicationExportService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/api/v1/projects/{projectRef}/publications/{publicationRef}/publicationexport")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
//@Authenticated
class PublicationExportResource(
    val publicationExportService: PublicationExportService,
    val projectRepository: ProjectRepository,
) {


    /**
     * TODO
     * test
     */
    @GET
    @Transactional
    @Path("/blob/{publicationExportRef}")
    fun getBlob(
        @PathParam("projectRef") projectRef: String,
        @PathParam("publicationRef") publicationRef: String,
        @PathParam("publicationExportRef") publicationExportRef: String
    ): ArrayList<*> {

        return publicationExportService.getDecodedBlob(projectRef, publicationRef, publicationExportRef)
    }


    @Transactional
    @POST
    fun create(
        @PathParam("projectRef") projectRef: String,
        @PathParam("publicationRef") publicationRef: String,
    ): Response {

        val newRef = publicationExportService.save(projectRef, publicationRef)

        return Response.created(
            URI.create("/api/v1/projects/$projectRef/publications/$publicationRef/publicationexport/$newRef")
        ).build()
    }

    @Transactional
    @GET
    @Path("/{publicationExportRef}")
    fun get(
        @PathParam("projectRef") projectRef: String,
        @PathParam("publicationRef") publicationRef: String,
        @PathParam("publicationExportRef") publicationExportRef: String
    ): PublicationExport {

        return publicationExportService.get(projectRef, publicationRef, publicationExportRef)

    }


    @Transactional
    @GET
    fun list(
        @PathParam("projectRef") projectRef: String,
        @PathParam("publicationRef") publicationRef: String,

        ): List<PublicationExport> {

        return publicationExportService.list(projectRef, publicationRef)
    }

}