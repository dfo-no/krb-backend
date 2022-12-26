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

@Path("/api/v1/projects/{projectref}/publicationexport") //TODO add publication
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
//@Authenticated
class PublicationExportResource(
    val publicationExportService: PublicationExportService,
    val projectRepository: ProjectRepository
) {

    @Transactional
    @POST
    fun create(
        @PathParam("projectref") projectRef: String,
        //newPublicationExport: PublicationExport
    ): Response {

        print("hello")
        val useProject = projectRepository.findByRef(projectRef) // TODO  impl

        val newRef = publicationExportService.saveBlob(useProject)

        return Response.created(URI.create("/api/v1/projects/$projectRef/publicationexport/" + newRef)).build()
    }

    @Transactional
    @GET
    @Path("/{publicationExportRef}")
    fun get(
        @PathParam("projectref") projectRef: String,
        @PathParam("publicationExportRef") publicationExportRef: String
    ): String {

        val useProject = projectRepository.findByRef(projectRef) //TODO impl

        val publicationExport = publicationExportService.get(publicationExportRef)

        return publicationExport.toString()

    }

    @Transactional
    @GET
    fun list(
        @PathParam("projectref") projectRef: String,
    ): MutableList<PublicationExport> {

        val useProject = projectRepository.list(projectRef) //TODO impl

        return publicationExportService.list()
    }

}