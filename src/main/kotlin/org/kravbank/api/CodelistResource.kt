package org.kravbank.api;

import org.kravbank.domain.Codelist;
import org.kravbank.service.CodelistService
import java.lang.IllegalArgumentException
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/codelists")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)

class CodelistResource (val codelistService: CodelistService) {
    //GET CODELIST
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    fun getCodelist(@PathParam("id") id : Long): Response {
        if (codelistService.exists(id)){
            return Response.ok(codelistService.getCodelist(id)).build()
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

