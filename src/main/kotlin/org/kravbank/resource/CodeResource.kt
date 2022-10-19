package org.kravbank.api;

import org.kravbank.domain.Code
import org.kravbank.service.CodeService
import org.kravbank.utils.form.code.CodeForm
import org.kravbank.utils.form.code.CodeFormUpdate
import org.kravbank.utils.mapper.code.CodeMapper
import org.kravbank.utils.mapper.code.CodeUpdateMapper
import java.net.URI
import java.util.*
import java.util.stream.Stream
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response


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
    ): Response {
        //finner code
        val code = codeService.get(projectRef, codelistRef, codeRef)
        //lager DTO - mapper fra entity
        val codeDTO = CodeMapper().fromEntity(code)
        return Response.ok(codeDTO).build()
    }

    //LIST CODES
    @GET
    fun listCodes(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
    ): Response {
        //finner liste av codes
        val codes = codeService.list(projectRef, codelistRef)

        val codelistForm = ArrayList<CodeForm>()
       //for (c in codes) codelistForm.add(CodeMapper().fromEntity(c))
        codes.stream().forEach {c -> codelistForm.add(CodeMapper().fromEntity(c))}

        return Response.ok(codelistForm).build()
    }

    //CREATE CODE
    @Transactional
    @POST
    fun createCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        newCode: CodeForm
    ): Response {
        //lager ny code
        val code = codeService.create(projectRef, codelistRef, newCode)
        //sender ny code ref i response header
        return Response.created(URI.create("/api/v1/projects/$projectRef/codelists/$codelistRef/codes/" + code.ref))
            .build()
    }

    //DELETE CODE
    @DELETE
    @Path("/{codeRef}")
    @Transactional
    fun deleteCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String
    ): Response {
        //sletter code
        val code = codeService.delete(projectRef, codelistRef, codeRef)
        val codeDTO = CodeMapper().fromEntity(code)
        // sender slettet publication ref i body
        return Response.ok(codeDTO.ref).build()
    }

    //UPDATE CODE
    @PUT
    @Path("{codeRef}")
    @Transactional
    fun updateCode(
        @PathParam("projectRef") projectRef: String,
        @PathParam("codelistRef") codelistRef: String,
        @PathParam("codeRef") codeRef: String,
        updatedCode: CodeFormUpdate
    ): Response {
        //oppdaterer code
        val code = codeService.update(projectRef, codelistRef, codeRef, updatedCode)
        //sender DTO - mapper fra entity
        val codeUpdateDTO = CodeUpdateMapper().fromEntity(code)
        return Response.ok(codeUpdateDTO).build()
    }
}