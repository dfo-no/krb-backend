package org.kravbank.api;


import org.kravbank.domain.Code;
import org.kravbank.service.CodeService
import java.lang.IllegalArgumentException
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/codes")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)

class CodeResource (val codeService: CodeService) {
    //GET CODE
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    fun getCode(@PathParam("id") id : Long): Response {
        if (codeService.exists(id)){
            return Response.ok(codeService.getCode(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    //LIST CODE
    //@Operation(summary = "List all needs")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listCode(): MutableList<Code> =
        codeService.listCodes();

    //CREATE CODE
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createCode(need: Code): Response? {
        try {
            codeService.createCode(need)
            if (need.isPersistent){
                return Response.created(URI.create("/needs" + need.id)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
        }catch (e: Exception){
            throw IllegalArgumentException ("Creating code FAILED. Message: $e")
        }
    }

    //DELETE CODE
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deleteCodeById(@PathParam("id") id: Long): Response {
        val deleted = codeService.deleteCode(id)
        return if (deleted) {
            //println(deleted)
            Response.noContent().build()
        } else Response.status(Response.Status.BAD_REQUEST).build()
    }

    //UPDATE CODE
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateCode(@PathParam("id") id: Long, need: Code): Response {
        if (codeService.exists(id)) {
            try {
                codeService.updateCode(id, need)
                return Response.ok(codeService.getCode(id)).build()
            } catch(e: Exception) {
                throw IllegalArgumentException ("Updating code FAILED. Message: $e")
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }
}

