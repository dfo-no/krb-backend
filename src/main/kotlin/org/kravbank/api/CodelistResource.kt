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

@Path("/api/v1/projects/{pid}/codelists")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)

class CodelistResource (val codelistService: CodelistService, val projectService: ProjectService) {
    //GET CODELIST
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    fun getCodelist(@PathParam("pid") pid : Long, @PathParam("id") id : Long): Response {
        if (projectService.exists(pid)) {
            val codelist = projectService.getProject(pid).codeList.find { codelist ->
                codelist.id == id
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
    fun createCodelist(codelist: Codelist): Response? {
        try {
            codelistService.createCodelist(codelist)
            if (codelist.isPersistent){
                return Response.created(URI.create("/codelists" + codelist.id)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
        }catch (e: Exception){
            throw IllegalArgumentException ("Creating codelist FAILED. Message: $e")
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

