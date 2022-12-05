package org.kravbank.resource

import io.quarkus.security.Authenticated
import org.kravbank.dao.ProjectForm
import org.kravbank.service.ProjectService
import java.net.URI
import javax.annotation.security.RolesAllowed
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
@Authenticated
class ProjectResource(val projectService: ProjectService) {

    @GET
    @RolesAllowed("user")
    @Path("/{projectRef}")
    fun getProject(@PathParam("projectRef") projectRef: String): Response {
        val project = projectService.get(projectRef)
        val form = ProjectForm().fromEntity(project)
        return Response.ok(form).build()
    }

    @GET
    @RolesAllowed("user")
    fun listProjects(): Response {
        val form = projectService.list()
            .stream()
            .map(ProjectForm()::fromEntity)
            .toList()
        return Response.ok(form).build()
    }

    @Transactional
    @POST
    @RolesAllowed("user")
    fun createProject(newProject: ProjectForm): Response {
        val project = projectService.create(newProject)
        return Response.created(URI.create("/api/v1/projects/" + project.ref))
            .build()
    }

    @DELETE
    @Path("{projectRef}")
    @Transactional
    @RolesAllowed("user")
    fun deleteProject(@PathParam("projectRef") projectRef: String): Response {
        val project = projectService.delete(projectRef)
        val form = ProjectForm().fromEntity(project)
        return Response.ok(form.ref).build()
    }

    @PUT
    @Path("{projectRef}")
    @Transactional
    @RolesAllowed("user")
    fun updateProject(@PathParam("projectRef") projectRef: String, updatedProject: ProjectForm): Response {
        val project = projectService.update(projectRef, updatedProject)
        val form = ProjectForm().fromEntity(project)
        return Response.ok(form).build()
    }
}
