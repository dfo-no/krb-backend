package org.kravbank.api;


import org.kravbank.domain.Need;
import org.kravbank.service.NeedService
import java.lang.IllegalArgumentException
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/needs")
//@Produces(APPLICATION_JSON)
//@Consumes(APPLICATION_JSON)

class NeedResource (val needService: NeedService) {


    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{id}")
    fun getNeed(@PathParam("id") id : Long): Response {
        if (needService.exists(id)){
            return Response.ok(needService.getNeed(id)).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    //@Operation(summary = "List all needs")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun listNeeds(): MutableList<Need> =
        needService.listNeeds();

    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    fun createNeed(need: Need): Response? {
        try {

            needService.createNeed(need)
            if (need.isPersistent){
                return Response.created(URI.create("/needs" + need.id)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
        }catch (e: Exception){
            throw IllegalArgumentException ("FAILED update need. Message: "+e )

        }

    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun deleteNeedById(@PathParam("id") id: Long): Response {
        val deleted = needService.deleteNeed(id)
        return if (deleted) {
            //println(deleted)
            Response.noContent().build()
        } else Response.status(Response.Status.BAD_REQUEST).build()
    }

    //UPDATE
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun updateNeed(@PathParam("id") id: Long, need: Need): Response {
        if (needService.exists(id)) {
            try {
                needService.updateNeed(id, need)
                return Response.ok(needService.getNeed(id)).build()
            } catch(e: Exception) {
                throw IllegalArgumentException ("FAILED update need. Message: "+e)
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }
}

