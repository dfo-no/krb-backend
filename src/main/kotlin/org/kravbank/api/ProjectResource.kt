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
@Path("/api/v1/projects")
@RequestScoped
//@SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
//@Authenticated
class ProjectResource (val projectService: ProjectService){
    //GET PROJECT
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{ref}")
    fun getProject(@PathParam("ref") ref : String): Response {

        try {
            val project = projectService.getProjectByRefCustomRepo(ref)!!
            return if (projectService.exists(project.id)) {
                Response.ok(project).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException ("GET One project FAILED. Message: $e")
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
            return if (project.isPersistent){
                Response.created(URI.create("/projects" + project.id)).build();
            } else {
                Response.status(Response.Status.BAD_REQUEST).build()
            }
        }catch (e: Exception){
            throw IllegalArgumentException ("Create project FAILED. Message: $e")
        }
    }
    //DELETE PROJECT
    @DELETE
    @Path("{projectref}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deleteProjectById(@PathParam("projectref") projectref: String): Response {
        try {
            if (projectService.refExists(projectref)) {
                val project = projectService.getProjectByRefCustomRepo(projectref)
                val deleted = projectService.deleteProject(project!!.id)
                return if (deleted) {
                    //println(deleted)
                    Response.noContent().build()
                } else Response.status(Response.Status.BAD_REQUEST).build()
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
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
    fun updateProject(@PathParam("projectref") projectref: String, project: Project): Response {

        if (projectService.refExists(projectref)) {
            val project = projectService.getProjectByRefCustomRepo(projectref)
            try {

                /**
                 * todo
                 */
                projectService.updateProject(project!!.id, project)
                return Response.ok(projectService.getProjectByRefCustomRepo(projectref)).build()
            } catch(e: Exception) {
                throw IllegalArgumentException ("Updating project FAILED. Message: $e")
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }
}