package org.kravbank.api;

import org.kravbank.form.need.NeedForm
import org.kravbank.form.need.NeedFormUpdate
import org.kravbank.service.NeedService
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/projects/{projectRef}/needs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class NeedResource (val needService: NeedService) {
    //GET NEED
    @GET
    @Path("/{needRef}")
    fun getNeed(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String
    ): Response =
        needService.getNeedByRefFromService(projectRef, needRef)

    //LIST NEEDS
    @GET
    fun listPublications(@PathParam("projectRef") projectRef: String): Response =
        needService.listNeedsFromService(projectRef)

    //CREATE NEED
    @Transactional
    @POST
    fun createNeed(@PathParam("projectRef") projectRef: String, need: NeedForm): Response =
        needService.createNeedFromService(projectRef, need)


    //DELETE NEED
    @DELETE
    @Path("/{needRef}")
    @Transactional
    fun deletePublication(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String
    ): Response =
        needService.deleteNeedFromService(projectRef, needRef)

    //UPDATE NEED
    @PUT
    @Path("{needRef}")
    @Transactional
    fun updatePublication(
        @PathParam("projectRef") projectRef: String,
        @PathParam("needRef") needRef: String,
        need: NeedFormUpdate
    ): Response =
        needService.updateNeedFromService(projectRef, needRef, need)
}