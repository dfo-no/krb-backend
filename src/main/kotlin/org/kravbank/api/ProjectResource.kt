package org.kravbank.api

import org.kravbank.domain.ProjectKtl
import org.kravbank.service.ProjectService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response


//@Tags(value = [Tag(name = "Read projects", description = "Read uploaded projects.")])
@Path("/kt")
@RequestScoped
//@SecurityScheme(securitySchemeName = "jwt", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
//@Authenticated

class ProjectResource (val projectService: ProjectService){

   // @Inject
  //  lateinit var projectRepo : ProjectRepository

    //@Operation(summary = "List all projects")
    @Produces("application/json")
    @Path("projects/")
    @GET
    fun listProjects():List<ProjectKtl> =
        projectService.listProjects();

    @Transactional
    @Produces("application/json")
    @Path("projects/")
    @POST
    fun createProject(projectKtl: ProjectKtl): Response? {
        projectService.createProject(projectKtl)
        if (projectKtl.isPersistent){
            return Response.created(URI.create("/projects" + projectKtl.id)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build()
        }
    }
}