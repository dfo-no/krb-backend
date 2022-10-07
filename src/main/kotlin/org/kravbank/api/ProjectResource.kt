package org.kravbank.api

import org.kravbank.utils.form.project.ProjectForm
import org.kravbank.utils.form.project.ProjectFormUpdate
import org.kravbank.service.ProjectService
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
    //GET PROJECT
    @GET
    @Path("/{projcetRef}")
    fun getProject(@PathParam("projcetRef") projcetRef: String): Response =
        projectService.get(projcetRef)

    //LIST PROJECTS
    @GET
    fun listProjects(): Response =
        projectService.list()

    //CREATE PROJECT
    @Transactional
    @POST
    fun createProject(newProject: ProjectForm): Response =
        projectService.create(newProject)

    //DELETE PROJECT
    @DELETE
    @Path("{projcetRef}")
    @Transactional
    fun deleteProjectByRef(@PathParam("projcetRef") projcetRef: String): Response =
        projectService.delete(projcetRef)

    //UPDATE PROJECT
    @PUT
    @Path("{projcetRef}")
    @Transactional
    fun updateProject(@PathParam("projcetRef") projcetRef: String, updateProject: ProjectFormUpdate): Response = projectService.update(projcetRef, updateProject)
}