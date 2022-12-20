package org.kravbank.resource

import io.quarkus.security.Authenticated
import org.kravbank.dao.CodeForm
import org.kravbank.service.CodeService
import java.net.URI
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("/api/v1/projects/{projectRef}/codelists/{codelistRef}/codes")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Authenticated
class CodeResource(val codeService: CodeService) {

    @GET
    @Path("/{codeRef}")
    fun getCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String
    ): CodeForm {
        val code = codeService.get(projectRef, codelistRef, codeRef)
        return CodeForm().fromEntity(code)
    }

    @GET
    fun listCodes(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
    ): List<CodeForm> {
        return codeService.list(projectRef, codelistRef)
            .stream()
            .map(CodeForm()::fromEntity)
            .toList()
    }

    @Transactional
    @POST
    fun createCode(
        @PathParam("projectRef") projectRef: String, @PathParam("codelistRef") codelistRef: String, newCode: CodeForm
    ): Response {
        val code = codeService.create(projectRef, codelistRef, newCode)
        //returnerer ny code ref i response header
        return Response.created(
            URI.create("/api/v1/projects/$projectRef/codelists/$codelistRef/codes/" + code.ref)
        ).build()
    }

    @DELETE
    @Path("/{codeRef}")
    @Transactional
    fun deleteCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String
    ): Response {
        codeService.delete(projectRef, codelistRef, codeRef)
        return Response.ok(codeRef).build()
    }

    @PUT
    @Path("{codeRef}")
    @Transactional
    fun updateCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String,
        updatedCode: CodeForm
    ): CodeForm {
        val updated = codeService.update(projectRef, codelistRef, codeRef, updatedCode)
        return CodeForm().fromEntity(updated)
    }
}