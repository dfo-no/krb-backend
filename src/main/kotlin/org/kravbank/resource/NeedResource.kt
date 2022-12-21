package org.kravbank.resource

import io.quarkus.security.Authenticated
import org.kravbank.dao.NeedForm
import org.kravbank.service.NeedService
import java.net.URI
import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/api/v1/projects/{projectRef}/needs")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
class NeedResource(val needService: NeedService) {

    @GET
    @Path("/{needRef}")
    fun getNeed(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String
    ): NeedForm {
        val need = needService.get(projectRef, needRef)
        return NeedForm().fromEntity(need)
    }

    @GET
    fun listNeeds(@PathParam("projectRef") projectRef: String): List<NeedForm> {
        return needService.list(projectRef)
            .stream()
            .map(NeedForm()::fromEntity)
            .toList()
    }

    @Transactional
    @POST
    fun createNeed(@PathParam("projectRef") projectRef: String, newNeed: NeedForm): Response {
        val need = needService.create(projectRef, newNeed)
        //returnerer ny need ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/needs/" + need.ref)).build()
    }

    @DELETE
    @Path("/{needRef}")
    @Transactional
    fun deleteNeed(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String
    ): Response {
        needService.delete(projectRef, needRef)
        return Response.ok(needRef).build()
    }

    @PUT
    @Path("{needRef}")
    @Transactional
    fun updateNeed(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String, updatedNeed: NeedForm
    ): NeedForm {
        val need = needService.update(projectRef, needRef, updatedNeed)
        return NeedForm().fromEntity(need)
    }
}