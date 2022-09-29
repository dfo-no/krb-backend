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
class CodeResource (val codeService: CodeService) {

    //GET REQUIREMENTVARIANT
    @GET
    @Path("/{codeRef}")
    fun getCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String
    ): Response =
        codeService.getCodeByRefFromService(
            projectRef,
            codelistRef,
            codeRef
        )

    //LIST REQUIREMENTVARIANTS
    @GET
    fun listCodes(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
    ): Response =
        codeService.listCodesFromService(projectRef, codelistRef)

    //CREATE REQUIREMENTVARIANT
    @Transactional
    @POST
    fun createCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        code: CodeForm
    ): Response =
        codeService.createCodeFromService(projectRef, codelistRef, code)

    //DELETE REQUIREMENTVARIANT
    @DELETE
    @Path("/{codeRef}")
    @Transactional
    fun deleteCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String
    ): Response =
        codeService.deleteCodeFromService(projectRef, codelistRef, codeRef)

    //UPDATE REQUIREMENTVARIANT
    @PUT
    @Path("{codeRef}")
    @Transactional
    fun updateCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String,
        code: CodeFormUpdate
    ): Response =
        codeService.updateCodeFromService(
            projectRef,
            codelistRef,
            codeRef,
            code
        )
}

