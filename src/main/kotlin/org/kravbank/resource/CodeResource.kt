package org.kravbank.resource;

import org.kravbank.dao.code.CodeCreateRequest
import org.kravbank.dao.code.CodeUpdateRequest
import org.kravbank.dao.code.toResponse
import io.quarkus.security.Authenticated
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
@Authenticated
class CodeResource(val codeService: CodeService) {

    @GET
    @Path("/{codeRef}")
    fun getCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String
    ): Response {
        val code = codeService.get(projectRef, codelistRef, codeRef)
        return Response.ok(code.toResponse()).build()
    }

    @GET
    fun listCodes(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
    ): Response {
        val codes = codeService.list(projectRef, codelistRef)
        return Response.ok(codes.map { it.toResponse() }).build()
    }

    @Transactional
    @POST
    fun createCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        newCode: CodeCreateRequest
    ): Response {
        val code = codeService.create(projectRef, codelistRef, newCode)

        //returnerer ny code ref i response header
        return Response.created(
            URI.create("/api/v1/projects/$projectRef/codelists/$codelistRef/codes/" + code.ref)).build()
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
        return Response.ok(code.toResponse().ref).build()
    }

    @PUT
    @Path("{codeRef}")
    @Transactional
    fun updateCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String,
        updatedCode: CodeUpdateRequest
    ): Response {
        val updated = codeService.update(projectRef, codelistRef, codeRef, updatedCode)
        return Response.ok(updated.toResponse()).build()
    }
}