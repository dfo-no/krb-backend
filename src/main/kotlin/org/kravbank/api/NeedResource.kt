package org.kravbank.api;

import org.kravbank.utils.form.need.NeedForm
import org.kravbank.utils.form.need.NeedFormUpdate
import org.kravbank.service.NeedService
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
    ): Response =
        needService.get(projectRef, needRef)

    //LIST NEEDS
    @GET
    fun listPublications(@PathParam("projectRef") projectRef: String): Response =
        needService.list(projectRef)

    //CREATE NEED
    @Transactional
    @POST
    fun createNeed(@PathParam("projectRef") projectRef: String, need: NeedForm): Response =
        needService.create(projectRef, need)


    //DELETE NEED
    @DELETE
    @Path("/{needRef}")
    @Transactional
    fun deletePublication(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String
    ): Response =
        needService.delete(projectRef, needRef)

    //UPDATE NEED
    @PUT
    @Path("{needRef}")
    @Transactional
    fun updatePublication(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        need: NeedFormUpdate
    ): Response =
        needService.update(projectRef, needRef, need)
}