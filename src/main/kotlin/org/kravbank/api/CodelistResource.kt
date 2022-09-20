package org.kravbank.api;

import org.kravbank.domain.Codelist;
import org.kravbank.domain.Project
import org.kravbank.service.CodelistService
import org.kravbank.service.ProjectService
import java.lang.IllegalArgumentException
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ResourceBundle
import java.util.UUID

@Path("/api/v1/projects/{projectref}/codelists")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)

class CodelistResource(val codelistService: CodelistService, val projectService: ProjectService) {
    //GET CODELIST
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{codelistref}")
    fun getCodelist(@PathParam("projectref") projectref: String, @PathParam("codelistref") ref: String): Response {
        //finn prosjekt med ref
        /* val foundProject = projectService.listProjects().find{
                    project -> project.ref == projectref
          }
          //prosjektets id
          val id: Long = foundProject!!.id

         */
        try {
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
        } catch (e: Exception) {

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
    fun createCodelist(@PathParam("projectref") projectref: String, codelist: Codelist): Response? {

        /***

        Beskrivelse:

        //lager codeliste tilhørende prosjekt

        // legger til i prosjekt med kobling til prosjektet ved opprettelse


         */


        try {


            /***
             *
             * TODO
             *
             * FORM CODELIST
             * DTO
             * MAPPER
             *
             *
             */

            // val createCodelist = Codelist.ModelMapper.from(codelist)
            val project = projectService.getProjectByRef(projectref)!!

            if (projectService.exists(project.id)) {
                codelistService.createCodelist(codelist) //codelist.persist

                // appender codelisten i prosjekt og knytter relasjon mellom codeliste og prosjekt
                project.codelist.add(codelist)

                //oppdaterer codeliste i prosjekt
                projectService.updateProject(project.id, project)

            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
            if (codelist.isPersistent) {

                return Response.created(URI.create("/api/v1/projects/$projectref/codelists" + codelist.id)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Creating codelist FAILED. Message: $e")
        }
    }

    //DELETE CODELIST
    @DELETE
    @Path("{codelistref}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deleteCodelistById(
        @PathParam("projectref") projectref: String,
        @PathParam("codelistref") ref: String
    ): Response {


        //try catch block
        try {
            //
            val project = projectService.getProjectByRef(projectref)!!
            return if (projectService.exists(project.id)) {
                val codelist = codelistService.getCodelistByRef(ref)!! //Bruk find metode på prosjekt sin codeliste
                val deleted = codelistService.deleteCodelist(codelist.id)
                return if (deleted) {
                    Response.noContent().build()
                } else Response.status(Response.Status.BAD_REQUEST).build()
            } else Response.status(Response.Status.NOT_FOUND).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("DELETING codelist FAILED. Message: $e")
        }
    }


    //UPDATE CODELIST
    @PUT
    @Path("{codelistref}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateCodelist(
        @PathParam("projectref") projectref: String,
        @PathParam("codelistref") ref: String,
        codelist: Codelist
    ): Response? {
        val project = projectService.getProjectByRef(projectref)!!
        try {
            if (projectService.exists(project.id)) {
                val foundCodelist = codelistService.getCodelistByRef(ref)
                if (codelistService.exists(foundCodelist!!.id)) {
                    codelistService.updateCodelist(foundCodelist.id, codelist)
                    return Response.ok(codelist).build()
                }
            } else return Response.status(Response.Status.BAD_REQUEST).build()
        } catch (e: Exception) {
            return Response.status(Response.Status.BAD_REQUEST).build()
            // throw IllegalArgumentException("Updating codelist FAILED. Message: $e")
        }
        return TODO("Provide the return value")
    }
}
