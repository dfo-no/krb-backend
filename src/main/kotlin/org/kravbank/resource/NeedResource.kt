package org.kravbank.resource;

import org.kravbank.dao.NeedForm
import org.kravbank.service.NeedService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import kotlin.streams.toList

@Path("/api/v1/projects/{projectRef}/needs")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class NeedResource(val needService: NeedService) {

    @GET
    @Path("/{needRef}")
    fun getNeed(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String
    ): Response {
        val need = needService.get(projectRef, needRef)
        val form = NeedForm().fromEntity(need)
        return Response.ok(form).build()
    }

    @GET
    fun listNeeds(@PathParam("projectRef") projectRef: String): Response {
        val form = needService.list(projectRef)
            .stream()
            .map(NeedForm()::fromEntity)
            .toList()
        return Response.ok(form).build()
    }

    @Transactional
    @POST
    fun createNeed(@PathParam("projectRef") projectRef: String, newNeed: NeedForm): Response {
        val need = needService.create(projectRef, newNeed)
        //returnerer ny need ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/needs/" + need.ref))
            .build()
    }

    @DELETE
    @Path("/{needRef}")
    @Transactional
    fun deleteNeed(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String
    ): Response {
        val need = needService.delete(projectRef, needRef)
        val form = NeedForm().fromEntity(need)
        // returnerer slettet need ref i body
        return Response.ok(form.ref).build()
    }

    @PUT
    @Path("{needRef}")
    @Transactional
    fun updateNeed(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        updatedNeed: NeedForm
    ): Response {
        val need = needService.update(projectRef, needRef, updatedNeed)
        val form = NeedForm().fromEntity(need)
        return Response.ok(form).build()
    }
}