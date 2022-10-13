package org.kravbank.api;

import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.form.codelist.CodelistFormUpdate
import org.kravbank.service.CodelistService
import org.kravbank.utils.mapper.codelist.CodelistMapper
import org.kravbank.utils.mapper.codelist.CodelistUpdateMapper
import java.net.URI
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("/api/v1/projects/{projectRef}/codelists")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
class CodelistResource(val codelistService: CodelistService) {
    //GET ONE CODELIST
    @GET
    @Path("/{codelistRef}")
    fun getCodelistByRef(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelisRef: String
    ): Response {
        val codelist = codelistService.get(projectRef, codelisRef)
        //mapper fra entity
        val codelistDTO = CodelistMapper().fromEntity(codelist)
        return Response.ok(codelistDTO).build()
    }

    //GET ALL CODELISTS
    @GET
    fun listCodelists(
        @PathParam("projectRef") projectRef: String
    ): Response {
        val codelists = codelistService.list(projectRef)
        val codelistsDTO = ArrayList<CodelistForm>()
        //mapper fra entity
        for (n in codelists) codelistsDTO.add(CodelistMapper().fromEntity(n))
        return Response.ok(codelistsDTO).build()
    }

    //CREATE CODELIST
    @Transactional
    @POST
    fun createCodelist(
        @PathParam("projectRef") projectRef: String, codelistDTO: CodelistForm
    ): Response {
        val codelist = codelistService.create(projectRef, codelistDTO)
        //sender ny codelist ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/codelists/" + codelist.ref)).build()
    }

    //DELETE CODELIST
    @DELETE
    @Path("{codelistRef}")
    @Transactional
    fun deleteCodelist(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String
    ): Response {
        codelistService.delete(projectRef, codelistRef)
        return Response.noContent().build()
    }

    //UPDATE CODELIST
    @PUT
    @Path("{codelistRef}")
    @Transactional
    fun updateCodelist(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        updateCodelist: CodelistFormUpdate
    ): Response {
        val codelist = codelistService.update(projectRef, codelistRef, updateCodelist)
        // mapper fra entity
        val codelistUpdateDTO = CodelistUpdateMapper().fromEntity(codelist)
        return Response.ok(codelistUpdateDTO).build()
    }
}