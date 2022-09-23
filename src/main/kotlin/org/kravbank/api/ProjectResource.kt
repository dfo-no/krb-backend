package org.kravbank.api

import org.kravbank.domain.Project
import org.kravbank.form.project.ProjectForm
import org.kravbank.form.project.ProjectFormUpdate
import org.kravbank.service.ProjectService
import org.kravbank.utils.project.ProjectMapper
import org.kravbank.utils.project.ProjectUpdateMapper
import java.lang.IllegalArgumentException
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

//@Tags(value = [Tag(name = "Read projects", description = "Read uploaded projects.")])
@Path("/api/v1/projects")
@RequestScoped
//@SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
//@Authenticated
class ProjectResource(val projectService: ProjectService) {
    //GET PROJECT
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{projectref}")
    fun getProjectByRef(@PathParam("projectref") projectref: String): Response {

        return try {
            if (projectService.refExists(projectref)) {
                val project = projectService.getProjectByRefCustomRepo(projectref)
                val projectMapper = ProjectMapper().fromEntity(project!!)
                Response.ok(projectMapper).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("GET One project FAILED. Message: $e")
        }
    }

    //LIST PROJECTS
    //@Operation(summary = "List all projects")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listProjects(): Response? {
           val projects = projectService.listProjects();
           val projectFormList = ArrayList<ProjectForm>()
           for (p in projects) projectFormList.add(ProjectMapper().fromEntity(p))
           return Response.ok(projectFormList).build()
    }

    //CREATE PROJECT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createProject(project: ProjectForm): Response? {
        try {
            val projectMapper = ProjectMapper().toEntity(project)
            projectService.createProject(projectMapper)
            return if (projectMapper.isPersistent) {
                Response.created(URI.create("/projects" + project.ref)).build();
            } else {
                Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Create project FAILED. Message: $e")
        }
    }

    //DELETE PROJECT
    @DELETE
    @Path("{projectref}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deleteProjectByRef(@PathParam("projectref") projectref: String): Response {
        try {
            return if (projectService.refExists(projectref)) {
                val project = projectService.getProjectByRefCustomRepo(projectref)
                val deleted = projectService.deleteProject(project!!.id)
                if (deleted) {
                    Response.noContent().build()
                } else Response.status(Response.Status.BAD_REQUEST).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Delete project FAILED. Message: $e")
        }
    }

    //UPDATE PROJECT
    @PUT
    @Path("{projectref}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateProject(@PathParam("projectref") projectref: String, project: ProjectFormUpdate): Response? {
        try {
            return if (projectService.refExists(projectref)) {
                val projectMapper = ProjectUpdateMapper().toEntity(project)
                val foundProject = projectService.getProjectByRefCustomRepo(projectref)
                projectService.updateProject(foundProject!!.id, projectMapper)
                Response.ok(project).build()
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Delete project FAILED. Message: $e")
        }
    }
}