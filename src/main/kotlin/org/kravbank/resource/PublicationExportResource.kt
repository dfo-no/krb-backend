package org.kravbank.resource

import io.quarkus.security.Authenticated
import org.kravbank.dao.PublicationExportForm
import org.kravbank.domain.PublicationExport
import org.kravbank.repository.ProjectRepository
import org.kravbank.service.PublicationExportService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/api/v1/projects/{projectRef}/publications/{publicationRef}/publicationexports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@Authenticated
class PublicationExportResource(
    val publicationExportService: PublicationExportService,
    val projectRepository: ProjectRepository,
) {

    @Transactional
    @GET
    @Path("/{publicationExportRef}")
    fun get(
        @PathParam("projectRef") projectRef: String,
        @PathParam("publicationRef") publicationRef: String,
        @PathParam("publicationExportRef") publicationExportRef: String
    ): PublicationExportForm = publicationExportService.get(projectRef, publicationRef, publicationExportRef)


    @Transactional
    @POST
    fun create(
        @PathParam("projectRef") projectRef: String,
        @PathParam("publicationRef") publicationRef: String,
    ): Response {

        val newRef = publicationExportService.create(projectRef, publicationRef).ref

        return Response.created(
            URI.create("/api/v1/projects/$projectRef/publications/$publicationRef/publicationexports/$newRef")
        ).build()
    }


    @Transactional
    @GET
    fun list(
        @PathParam("projectRef") projectRef: String,
        @PathParam("publicationRef") publicationRef: String,
    ): List<PublicationExport> = publicationExportService.list(projectRef, publicationRef)

}




