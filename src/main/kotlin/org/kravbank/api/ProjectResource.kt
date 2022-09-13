package org.kravbank.api

import org.kravbank.domain.Project
import org.kravbank.service.ProjectService
import java.lang.IllegalArgumentException
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

//@Tags(value = [Tag(name = "Read projects", description = "Read uploaded projects.")])
@Path("/projects")
@RequestScoped
//@SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
//@Authenticated
class ProjectResource (val projectService: ProjectService){
    //GET PROJECT
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    fun getProject(@PathParam("id") id : Long): Response {
        if (projectService.exists(id)){
            return Response.ok(projectService.getProject(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    //LIST PROJECTS
    //@Operation(summary = "List all projects")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listProjects():List<Project> =
        projectService.listProjects();

    //CREATE PROJECT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createProject(project: Project): Response? {
        try {

            projectService.createProject(project)
            if (project.isPersistent){
                return Response.created(URI.create("/projects" + project.id)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
        }catch (e: Exception){
            throw IllegalArgumentException ("Create project FAILED. Message: $e")
        }
    }
    //DELETE PROJECT
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deleteProjectById(@PathParam("id") id: Long): Response {
        val deleted = projectService.deleteProject(id)
        return if (deleted) {
            //println(deleted)
            Response.noContent().build()
        } else Response.status(Response.Status.BAD_REQUEST).build()
    }

    //UPDATE PROJECT
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateProject(@PathParam("id") id: Long, project: Project): Response {
        if (projectService.exists(id)) {
            try {
                projectService.updateProject(id, project)
                return Response.ok(projectService.getProject(id)).build()
            } catch(e: Exception) {
                throw IllegalArgumentException ("Updating project FAILED. Message: $e")
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }
}