package org.kravbank.api;

import org.kravbank.domain.Codelist;
import org.kravbank.service.CodelistService
import org.kravbank.service.ProjectService
import java.lang.IllegalArgumentException
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/api/v1/projects/{projectref}/codelists")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)

class CodelistResource (val codelistService: CodelistService, val projectService: ProjectService) {
    //GET CODELIST
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{codelistref}")
    fun getCodelist(@PathParam("projectref") projectref : String, @PathParam("codelistref") ref : String): Response {
        //finn prosjekt med ref
      /* val foundProject = projectService.listProjects().find{
                  project -> project.ref == projectref
        }
        //prosjektets id
        val id: Long = foundProject!!.id

       */

        val project = projectService.getProjectByRef(projectref)!!

        //hvis prosjektet eksisterer finn finn kodeliste eller returner HTTP 404 NOT FOUND
        if (projectService.exists(project.id)) {
            val codelist = projectService.getProject(project.id).codelist.find { codelist ->
                codelist.ref == ref
            } ?: return Response.status(Response.Status.NOT_FOUND).build()
            return Response.ok(codelist).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    //LIST CODELIST
    //@Operation(summary = "List all codelists")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listCodelists(): MutableList<Codelist> =
        codelistService.listCodelists();

    //CREATE CODELIST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createCodelist(@PathParam("projectref") pid : Long, codelist: Codelist): Response? {

        /*

        //lager codeliste tilhørende prosjekt

        // legger til i prosjekt med kobling til prosjektet ved opprettelse

         */
        try {

            // val createCodelist = Codelist.ModelMapper.from(codelist)

            if (projectService.exists(pid)) {
                codelistService.createCodelist(codelist) //codelist.persist
/*
                var project = projectService.getProject(pid)
                var updateProject = project.codelist.add(codelist)

                // legg til project UUID
                projectService.updateProject(pid, project)
 */
            } else {
                // eventuelt opprett project
                return Response.status(Response.Status.NOT_FOUND).build()
            }
            if (codelist.isPersistent) {

                return Response.created(URI.create("/codelists" + codelist.id)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Creating codelist FAILED. Message: $e")
        }
    }

    //DELETE CODELIST
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deleteCodelistById(@PathParam("id") id: Long): Response {
        val deleted = codelistService.deleteCodelist(id)
        return if (deleted) {
            //println(deleted)
            Response.noContent().build()
        } else Response.status(Response.Status.BAD_REQUEST).build()
    }

    //UPDATE CODELIST
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateCodelist(@PathParam("id") id: Long, codelist: Codelist): Response {
        if (codelistService.exists(id)) {
            try {
                codelistService.updateCodelist(id, codelist)
                return Response.ok(codelistService.getCodelist(id)).build()
            } catch(e: Exception) {
                throw IllegalArgumentException ("Updating codelist FAILED. Message: $e")
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }
}

