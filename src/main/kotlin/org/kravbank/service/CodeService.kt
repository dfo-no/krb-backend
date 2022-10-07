package org.kravbank.service

import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import org.kravbank.repository.CodeRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.code.CodeForm
import org.kravbank.utils.form.code.CodeFormUpdate
import org.kravbank.utils.mapper.code.CodeMapper
import org.kravbank.utils.mapper.code.CodeUpdateMapper
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class CodeService(
    val codeRepository: CodeRepository,
    val projectService: ProjectService,
    val codelistService: CodelistService,
    val projectRepository: ProjectRepository
)  {
    @Throws(BackendException::class)
    fun list(projectRef: String, codelistRef: String): Response {
        //list codes by codelist ref
        val foundProjectCodelist = projectRepository.findByRef(projectRef).codelist.find { codelist -> codelist.ref == codelistRef }
        Optional.ofNullable(foundProjectCodelist)
            .orElseThrow { NotFoundException("Codelist not found by ref $codelistRef in project by ref $projectRef") }
        val codes = foundProjectCodelist!!.codes
        //convert to array of form
        val codesFormList = ArrayList<CodeForm>()
        for (code in codes) codesFormList.add(CodeMapper().fromEntity(code))
        //returns the custom code form
        return Response.ok(codesFormList).build()
    }

    @Throws(BackendException::class)
    fun get(projectRef: String, codelistRef: String, codeRef: String): Response {
        val foundProjectCodelist = projectRepository.findByRef(projectRef).codelist.find { codelist -> codelist.ref == codelistRef }
        Optional.ofNullable(foundProjectCodelist)
            .orElseThrow { NotFoundException("Codelist not found by ref $codelistRef in project by ref $projectRef") }
        val foundCodelistCode = foundProjectCodelist!!.codes.find { code -> code.ref == codeRef }
        Optional.ofNullable(foundCodelistCode)
            .orElseThrow { NotFoundException("Code not found by ref $codeRef in codelist by ref $codelistRef") }
        val codeMapper = CodeMapper().fromEntity(foundCodelistCode!!)
        return Response.ok(codeMapper).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, codelistRef: String, code: CodeForm): Response {
        val codeMapper = CodeMapper().toEntity(code)
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = foundProject.codelist.find { codelist -> codelist.ref == codelistRef }!!
        Optional.ofNullable(foundCodelist).orElseThrow{ NotFoundException("Did not find codelist by ref $codelistRef in project by ref $projectRef") }
        val added = foundCodelist.codes.add(codeMapper)
        if (added) {
            projectService.updateProject(foundCodelist.id, foundProject)
            return Response.created(URI.create("/api/v1/projects/$projectRef/codelists/$codelistRef/codes/" + code.ref)).build()
        } else throw BadRequestException("Bad request! Did not create code")
    }
    @Throws(BackendException::class)
    fun delete(projectRef: String, codelistRef: String, codeRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = foundProject.codelist.find { codelist -> codelist.ref == codelistRef }!!
        Optional.ofNullable(foundCodelist).orElseThrow{ NotFoundException("Did not find codelist by ref $codelistRef in project by ref $projectRef") }
        val foundCode = foundCodelist.codes.find { code -> code.ref == codeRef }!!
        Optional.ofNullable(foundCode).orElseThrow{ NotFoundException("Did not find code by ref $codeRef in codelist by ref $codelistRef") }
        val deleted = foundCodelist.codes.remove(foundCode)
        if (deleted){
            projectService.updateProject(foundProject.id, foundProject)
            Response.noContent().build()
            return Response.noContent().build()
        } else throw NotFoundException("Bad request! Code not deleted")
    }
    @Throws(BackendException::class)
    fun update(projectRef: String, codelistRef: String, codeRef: String, code: CodeFormUpdate): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = foundProject.codelist.find { codelist -> codelist.ref == codelistRef }!!
        Optional.ofNullable(foundCodelist).orElseThrow{ NotFoundException("Did not find codelist by ref $codelistRef in project by ref $projectRef") }
        val foundCode = foundCodelist.codes.find { code -> code.ref == codeRef }!!
        Optional.ofNullable(foundCode).orElseThrow{ NotFoundException("Did not find code by ref $codeRef in codelist by ref $codelistRef") }
        val codeMapper = CodeUpdateMapper().toEntity(code)
        codeRepository.update(
            "title = ?1, description = ?2 where id= ?3",
            codeMapper.title,
            codeMapper.description,
            //code.deletedDate,
            foundCode.id
        )
        return Response.ok(code).build()
    }
    //fun refExists(ref: String): Boolean = codeRepository.count("ref", ref) == 1L
}