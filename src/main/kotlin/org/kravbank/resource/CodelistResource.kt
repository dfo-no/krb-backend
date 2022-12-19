package org.kravbank.resource

import io.quarkus.security.Authenticated
import org.kravbank.dao.CodelistForm
import org.kravbank.service.CodelistService
import java.net.URI
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("/api/v1/projects/{projectRef}/codelists")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Authenticated
class CodelistResource(val codelistService: CodelistService) {

    @GET
    @Path("/{codelistRef}")
    fun getCodelistByRef(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelisRef: String
    ): CodelistForm {
        val codelist = codelistService.get(projectRef, codelisRef)
        return CodelistForm().fromEntity(codelist)
    }

    @GET
    fun listCodelists(
        @PathParam("projectRef") projectRef: String
    ): List<CodelistForm> {
        return codelistService.list(projectRef)
            .stream()
            .map(CodelistForm()::fromEntity)
            .toList()
    }

    @Transactional
    @POST
    fun createCodelist(
        @PathParam("projectRef") projectRef: String, newCodelist: CodelistForm
    ): Response {
        val codelist = codelistService.create(projectRef, newCodelist)
        //sender ny codelist ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/codelists/" + codelist.ref))
            .build()
    }

    @DELETE
    @Path("{codelistRef}")
    @Transactional
    fun deleteCodelist(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String
    ): Response {
        val codelist = codelistService.delete(projectRef, codelistRef)
        val form = CodelistForm().fromEntity(codelist)
        return Response.ok(form.ref).build()
    }

    @PUT
    @Path("{codelistRef}")
    @Transactional
    fun updateCodelist(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        updateCodelist: CodelistForm
    ): CodelistForm {
        val codelist = codelistService.update(projectRef, codelistRef, updateCodelist)
        return CodelistForm().fromEntity(codelist)
    }
}