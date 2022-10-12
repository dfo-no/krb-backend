package org.kravbank.api;

import org.kravbank.service.CodeService
import org.kravbank.utils.form.code.CodeForm
import org.kravbank.utils.form.code.CodeFormUpdate
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("/api/v1/projects/{projectRef}/codelists/{codelistRef}/codes")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
class CodeResource(val codeService: CodeService) {

    //GET CODE
    @GET
    @Path("/{codeRef}")
    fun getCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String
    ): Response =
        codeService.get(
            projectRef,
            codelistRef,
            codeRef
        )

    //LIST CODES
    @GET
    fun listCodes(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
    ): Response =
        codeService.list(projectRef, codelistRef)

    //CREATE REQUIREMENTVARIANT
    @Transactional
    @POST
    fun createCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        code: CodeForm
    ): Response =
        codeService.create(projectRef, codelistRef, code)

    //DELETE CODE
    @DELETE
    @Path("/{codeRef}")
    @Transactional
    fun deleteCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String
    ): Response =
        codeService.delete(projectRef, codelistRef, codeRef)

    //UPDATE CODE
    @PUT
    @Path("{codeRef}")
    @Transactional
    fun updateCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String,
        code: CodeFormUpdate
    ): Response =
        codeService.update(
            projectRef,
            codelistRef,
            codeRef,
            code
        )
}

