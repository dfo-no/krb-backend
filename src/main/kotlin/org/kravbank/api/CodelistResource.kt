package org.kravbank.api;

import org.kravbank.domain.Codelist;
import org.kravbank.service.CodelistService
import org.kravbank.service.ProjectService
import org.kravbank.utils.CodelistMapper
import java.lang.IllegalArgumentException
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/api/v1/projects/{projectref}/codelists")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)

class CodelistResource(val codelistService: CodelistService, val projectService: ProjectService) {
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{codelistref}")
    fun getCodelist(
        @PathParam("projectref") projectref: String,
        @PathParam("codelistref") codelistref: String
    ): Response {

        //val project = projectService.getProjectByRef(projectref)!!
        //hvis prosjektet eksisterer finn kodeliste i prosjektet
        // eller returner HTTP 404 NOT FOUND

        /**
         * todo
         * delete ref exists
         * backend exception mapper
         * error 500 og 404
         * all logikk i service
         */
        // var m = CodelistMapper().fromEntity(codelist)

        if (projectService.refExists(projectref)) {
            val project = projectService.getProjectByRefCustomRepo(projectref)!!
            val codelist = project.codelist.find { codelist ->
                codelist.ref == codelistref
            }
            val codelistMapper = CodelistMapper().fromEntity(codelist!!)
            
            return Response.ok(codelistMapper).build()

        } else return Response.status(Response.Status.NOT_FOUND).build()

        /* } catch (e: Exception) {
             return Response.status(Response.Status.BAD_REQUEST).build()
             // throw IllegalArgumentException("GET ONE codelist failed")
         }

         */
    }

    //GET CODELIST
    //@Operation(summary = "List all codelists")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listCodelists(@PathParam("projectref") projectref: String): Response {
        try {
            if (projectService.refExists(projectref)) {
                val project = projectService.getProjectByRefCustomRepo(projectref)!!
                // list codelist by project ref
                return Response.ok(project.codelist).build()
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            return Response.status(Response.Status.BAD_REQUEST).build()
            // throw IllegalArgumentException("GET codelists failed")
        }
    }


    //CREATE CODELIST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createCodelist(@PathParam("projectref") projectref: String, codelist: Codelist): Response? {
        //lager codeliste tilhørende prosjektet
        // legger til i prosjekt med kobling til prosjektet ved opprettelse
        try {

            //var m = CodelistMapper()
            //m.toEntity(codelist)
            // val createCodelist = Codelist.ModelMapper.from(codelist)
            if (projectService.refExists(projectref)) {
                val project = projectService.getProjectByRefCustomRepo(projectref)!!
                //codelistService.createCodelist(codelist) //codelist.persist
                // legger til kodelisten i prosjekt
                project.codelist.add(codelist)
                //oppdaterer codeliste i prosjekt
                projectService.updateProject(project.id, project)
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
            if (codelist.isPersistent) {
                return Response.created(URI.create("/api/v1/projects/$projectref/codelists" + codelist.ref)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            return Response.status(Response.Status.BAD_REQUEST).build()
            // throw IllegalArgumentException("Creating codelist FAILED. Message: $e")
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
        return try {
            if (projectService.refExists(projectref)) {
                val project = projectService.getProjectByRefCustomRepo(projectref)!!
                //  val codelist = codelistService.getCodelistByRef(ref)!!
                val codelist = project.codelist.find { codelist -> codelist.ref == ref }
                val deleted = codelistService.deleteCodelist(codelist!!.id)
                if (deleted) {
                    project.codelist.remove(codelist) //nødvendig?
                    projectService.updateProject(project.id, project)
                    return Response.noContent().build()
                } else Response.status(Response.Status.BAD_REQUEST).build()
            } else Response.status(Response.Status.NOT_FOUND).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("DELETING codelist FAILED. Message: $e")
        }
    }


    //UPDATE CODELIST

    /***
     * todo
     *
     * Fix id = null ved endring -> DTO / FORM
     */

    @PUT
    @Path("{codelistref}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateCodelist(
        @PathParam("projectref") projectref: String,
        @PathParam("codelistref") codelistref: String,
        codelist: Codelist
    ): Response? {
        return try {
            if (projectService.refExists(projectref) && codelistService.refExists(codelistref)) {
                // val project = projectService.getProjectByRefCustomRepo(projectref)!!
                val foundCodelist = codelistService.getCodelistByRef(codelistref)
                codelistService.updateCodelist(foundCodelist!!.id, codelist)
                Response.ok(codelist).build()
            } else Response.status(Response.Status.BAD_REQUEST).build()
        } catch (e: Exception) {
            Response.status(Response.Status.BAD_REQUEST).build()
            // throw IllegalArgumentException("Updating codelist FAILED. Message: $e")
        }
    }
}
