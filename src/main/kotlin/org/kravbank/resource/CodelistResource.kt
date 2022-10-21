package org.kravbank.resource;

import org.kravbank.utils.codelist.dto.CodelistForm
import org.kravbank.utils.codelist.dto.CodelistFormUpdate
import org.kravbank.service.CodelistService
import org.kravbank.utils.codelist.mapper.CodelistMapper
import org.kravbank.utils.codelist.mapper.CodelistUpdateMapper
import java.net.URI
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import kotlin.streams.toList

@Path("/api/v1/projects/{projectRef}/codelists")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
class CodelistResource(val codelistService: CodelistService) {

     @GET
     @Path("/{codelistRef}")
     fun getCodelistByRef(
         @PathParam("projectRef") projectRef: String,
         @PathParam("codelistRef") codelisRef: String
     ): Response {
         val codelist = codelistService.get(projectRef, codelisRef)
         val codelistDTO = CodelistMapper().fromEntity(codelist)
         return Response.ok(codelistDTO).build()
     }

    @GET
    fun listCodelists(
        @PathParam("projectRef") projectRef: String
    ): Response {
        val codelistsDTO = codelistService.list(projectRef).stream()
            .map(CodelistMapper()::fromEntity)
            .toList()
        return Response.ok(codelistsDTO).build()
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
        val codelistDTO = CodelistMapper().fromEntity(codelist)
        return Response.ok(codelistDTO.ref).build()
    }

    @PUT
    @Path("{codelistRef}")
    @Transactional
    fun updateCodelist(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        updateCodelist: CodelistFormUpdate
    ): Response {
        val codelist = codelistService.update(projectRef, codelistRef, updateCodelist)
        val codelistUpdateDTO = CodelistUpdateMapper().fromEntity(codelist)
        return Response.ok(codelistUpdateDTO).build()
    }
}