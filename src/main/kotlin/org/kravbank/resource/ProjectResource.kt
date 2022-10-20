package org.kravbank.resource

import org.kravbank.utils.form.project.ProjectForm
import org.kravbank.utils.form.project.ProjectFormUpdate
import org.kravbank.service.ProjectService
import org.kravbank.utils.mapper.project.ProjectMapper
import org.kravbank.utils.mapper.project.ProjectUpdateMapper
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

//@Tags(value = [Tag(name = "Read projects", description = "Read uploaded projects.")])
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
        val projectDTO = ProjectMapper().fromEntity(project)

        return Response.ok(projectDTO).build()
    }

    @GET
    fun listProjects(): Response {

        val projects = projectService.list()
        val projectsDTO = ArrayList<ProjectForm>()
        for (p in projects) projectsDTO.add(ProjectMapper().fromEntity(p))

        return Response.ok(projectsDTO).build()
    }

    @Transactional
    @POST
    fun createProject(newProject: ProjectForm): Response {

        val project = projectService.create(newProject)

        return Response.created(URI.create("/projects/" + project.ref)).build();
    }

    @DELETE
    @Path("{projcetRef}")
    @Transactional
    fun deleteProjectByRef(@PathParam("projcetRef") projcetRef: String): Response {

        val project = projectService.delete(projcetRef)
        val projectDTO = ProjectMapper().fromEntity(project)

        return Response.ok(projectDTO.ref).build()
    }

    @PUT
    @Path("{projcetRef}")
    @Transactional
    fun updateProject(@PathParam("projcetRef") projcetRef: String, updatedProject: ProjectFormUpdate): Response {

        val project = projectService.update(projcetRef, updatedProject)
        val projectUpdateDTO = ProjectUpdateMapper().fromEntity(project)

        return Response.ok(projectUpdateDTO).build()

    }
}
