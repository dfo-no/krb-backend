package org.kravbank.resource;

import org.kravbank.service.CodeService
import org.kravbank.utils.code.dto.CodeForm
import org.kravbank.utils.code.dto.CodeFormUpdate
import org.kravbank.utils.code.mapper.CodeMapper
import org.kravbank.utils.code.mapper.CodeUpdateMapper
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
        val codeDTO = CodeMapper().fromEntity(code)
        return Response.ok(codeDTO).build()
    }

    @GET
    fun listCodes(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
    ): Response {
        val codesDTO = codeService.list(projectRef, codelistRef)
            .stream()
            .map(CodeMapper()::fromEntity).toList()
        return Response.ok(codesDTO).build()
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
        val codeDTO = CodeMapper().fromEntity(code)
        // returnerer slettet publication ref i body
        return Response.ok(codeDTO.ref).build()
    }

    @PUT
    @Path("{codeRef}")
    @Transactional
    fun updateCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String,
        updatedCode: CodeFormUpdate
    ): Response {
        val code = codeService.update(projectRef, codelistRef, codeRef, updatedCode)
        val codeUpdateDTO = CodeUpdateMapper().fromEntity(code)
        return Response.ok(codeUpdateDTO).build()
    }
}