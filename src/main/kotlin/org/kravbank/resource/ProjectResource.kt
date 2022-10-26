package org.kravbank.resource

import org.kravbank.dao.ProjectForm
import org.kravbank.service.ProjectService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.streams.toList

@Path("/api/v1/projects")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
//@Authenticated
class ProjectResource(val projectService: ProjectService) {

    @GET
    @Path("/{projcetRef}")
    fun getProject(@PathParam("projcetRef") projcetRef: String): Response {
        val project = projectService.get(projcetRef)
        val form = ProjectForm().fromEntity(project)
        return Response.ok(form).build()
    }

    @GET
    fun listProjects(): Response {
        val form = projectService.list()
            .stream()
            .map(ProjectForm()::fromEntity)
            .toList()
        return Response.ok(form).build()
    }

    @Transactional
    @POST
    fun createProject(newProject: ProjectForm): Response {
        val project = projectService.create(newProject)
        return Response.created(URI.create("/api/v1/projects/" + project.ref))
            .build();
    }

    @DELETE
    @Path("{projcetRef}")
    @Transactional
    fun deleteProject(@PathParam("projcetRef") projcetRef: String): Response {
        val project = projectService.delete(projcetRef)
        val form = ProjectForm().fromEntity(project)
        return Response.ok(form.ref).build()
    }

    @PUT
    @Path("{projcetRef}")
    @Transactional
    fun updateProject(@PathParam("projcetRef") projcetRef: String, updatedProject: ProjectForm): Response {
        val project = projectService.update(projcetRef, updatedProject)
        val form = ProjectForm().fromEntity(project)
        return Response.ok(form).build()
    }
}
