package org.kravbank.api;

import org.kravbank.utils.form.need.NeedForm
import org.kravbank.utils.form.need.NeedFormUpdate
import org.kravbank.service.NeedService
import org.kravbank.utils.mapper.codelist.CodelistMapper
import org.kravbank.utils.mapper.codelist.CodelistUpdateMapper
import org.kravbank.utils.mapper.need.NeedMapper
import org.kravbank.utils.mapper.need.NeedUpdateMapper
import java.net.URI
import java.util.ArrayList
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/projects/{projectRef}/needs")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class NeedResource(val needService: NeedService) {
    //GET NEED
    @GET
    @Path("/{needRef}")
    fun getNeed(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String
    ): Response {
        val need = needService.get(projectRef, needRef)
        //mapper fra entity
        val needDTO = NeedMapper().fromEntity(need)
        return Response.ok(needDTO).build()
    }

    //LIST NEEDS
    @GET
    fun listPublications(@PathParam("projectRef") projectRef: String): Response {
        val needs = needService.list(projectRef)
        val needsDTO = ArrayList<NeedForm>()
        for (n in needs) needsDTO.add(NeedMapper().fromEntity(n))
        return Response.ok(needsDTO).build()
    }

    //CREATE NEED
    @Transactional
    @POST
    fun createNeed(@PathParam("projectRef") projectRef: String, need: NeedForm): Response {
        val need = needService.create(projectRef, need)
        //sender ny need ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/needs/" + need.ref)).build()
    }


    //DELETE NEED
    @DELETE
    @Path("/{needRef}")
    @Transactional
    fun deletePublication(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String
    ): Response {
        val need = needService.delete(projectRef, needRef)
        val needDTO = NeedMapper().fromEntity(need)

        // sender slettet need ref i body
        return Response.ok(needDTO.ref).build()
    }

    //UPDATE NEED
    @PUT
    @Path("{needRef}")
    @Transactional
    fun updatePublication(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        need: NeedFormUpdate
    ): Response {
        val need = needService.update(projectRef, needRef, need)
        // mapper fra entity
        val needUpdateDTO = NeedUpdateMapper().fromEntity(need)
        return Response.ok(needUpdateDTO).build()
    }
}