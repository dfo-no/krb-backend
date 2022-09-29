package org.kravbank.api;

import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.form.codelist.CodelistFormUpdate
import org.kravbank.service.CodelistService
import org.kravbank.service.ProjectService
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("/api/v1/projects/{projectRef}/codelists")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
class CodelistResource(val codelistService: CodelistService, val projectService: ProjectService) {
    //GET ONE CODELIST
    @GET
    @Path("/{codelistRef}")
    fun getCodelistByRef(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelisRef: String
    ): Response =
        codelistService.getCodelistByRefFromService(projectRef, codelisRef)

    //GET ALL CODELISTS
    @GET
    fun listCodelists(@PathParam("projectRef") projectRef: String
    ): Response =
        codelistService.listCodelistFromService(projectRef)

    //CREATE CODELIST
    @Transactional
    @POST
    fun createCodelist(@PathParam("projectRef") projectRef: String, codelist: CodelistForm
    ): Response =
        codelistService.createCodelistFromService(projectRef, codelist)

    //DELETE CODELIST
    @DELETE
    @Path("{codelistRef}")
    @Transactional
    fun deleteCodelistById(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String
    ): Response = codelistService.deleteCodelistFromService(projectRef, codelistRef)

    //UPDATE CODELIST
    @PUT
    @Path("{codelistRef}")
    @Transactional
    fun updateCodelist(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        updateCodelist: CodelistFormUpdate
    ): Response = codelistService.updateCodelistFromService(projectRef, codelistRef, updateCodelist)
}