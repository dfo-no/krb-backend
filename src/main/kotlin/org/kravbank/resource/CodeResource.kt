package org.kravbank.resource;

import org.kravbank.dao.CodeForm
import org.kravbank.service.CodeService
import java.net.URI
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response
import kotlin.streams.toList


@Path("/api/v1/projects/{projectRef}/codelists/{codelistRef}/codes")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
class CodeResource(val codeService: CodeService) {

    @GET
    @Path("/{codeRef}")
    fun getCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String
    ): Response {
        val code = codeService.get(projectRef, codelistRef, codeRef)
        val form = CodeForm().fromEntity(code)
        return Response.ok(form).build()
    }

    @GET
    fun listCodes(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
    ): Response {
        val form = codeService.list(projectRef, codelistRef)
            .stream()
            .map(CodeForm()::fromEntity).toList()
        return Response.ok(form).build()

    }

    @Transactional
    @POST
    fun createCode(
        @PathParam("projectRef") projectRef: String, @PathParam("codelistRef") codelistRef: String, newCode: CodeForm
    ): Response {
        val code = codeService.create(projectRef, codelistRef, newCode)
        //returnerer ny code ref i response header
        return Response.created(
            URI.create("/api/v1/projects/$projectRef/codelists/$codelistRef/codes/" + code.ref))
            .build()
    }

    @DELETE
    @Path("/{codeRef}")
    @Transactional
    fun deleteCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String
    ): Response {
        val code = codeService.delete(projectRef, codelistRef, codeRef)
        val form = CodeForm().fromEntity(code)
        return Response.ok(form.ref).build()
    }

    @PUT
    @Path("{codeRef}")
    @Transactional
    fun updateCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String,
        updatedCode: CodeForm
    ): Response {
        val updated = codeService.update(projectRef, codelistRef, codeRef, updatedCode)
        val form = CodeForm().fromEntity(updated)
        return Response.ok(form).build()
    }
}