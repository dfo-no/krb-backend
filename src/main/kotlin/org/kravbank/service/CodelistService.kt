package org.kravbank.service

import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.form.codelist.CodelistFormUpdate
import org.kravbank.utils.mapper.codelist.CodelistMapper
import org.kravbank.utils.mapper.codelist.CodelistUpdateMapper
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response
import kotlin.collections.ArrayList


@ApplicationScoped
class CodelistService(val codelistRepository: CodelistRepository, val projectService: ProjectService, val projectRepository: ProjectRepository) {
    @Throws(BackendException::class)
    fun list(projectRef: String): Response {
        //list codelist by project ref
        val foundProjectCodelists = projectRepository.findByRef(projectRef).codelist
        //convert to array of form
        val codelistFormList = ArrayList<CodelistForm>()
        for (p in foundProjectCodelists) codelistFormList.add(CodelistMapper().fromEntity(p))
        //returns the custom codelist form
        return Response.ok(codelistFormList).build()
    }

    @Throws(BackendException::class)
    fun get(projectRef: String, codelistRef: String): Response {
        val foundProjectCodelist = projectRepository.findByRef(projectRef).codelist.find { codelist ->
            codelist.ref == codelistRef
        }
        Optional.ofNullable(foundProjectCodelist)
            .orElseThrow { NotFoundException("Codelist not found by ref $codelistRef in project by ref $projectRef") }
        val codelistMapper = CodelistMapper().fromEntity(foundProjectCodelist!!)
        return Response.ok(codelistMapper).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, codelist: CodelistForm): Response {
        val codelistMapper = CodelistMapper().toEntity(codelist)
        val project = projectRepository.findByRef(projectRef)
        project.codelist.add(codelistMapper)
        projectService.updateProject(project.id, project)
        if (codelistMapper.isPersistent)
            return Response.created(URI.create("/api/v1/projects/$projectRef/codelists/" + codelist.ref)).build()
        else throw BadRequestException("Bad request! Did not create codelist")
    }

    @Throws(BackendException::class)
                fun delete(projectRef: String, codelistRef: String): Response {
                    val foundCodelist = projectRepository.findByRef(projectRef).codelist.find { codelist ->
                        codelist.ref == codelistRef
                    }
                    Optional.ofNullable(foundCodelist)
                        .orElseThrow { NotFoundException("Codelist not found by ref $codelistRef in project by ref $projectRef") }
        foundCodelist!!.delete()
        return Response.noContent().build()
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, codelistRef: String, codelist: CodelistFormUpdate): Response {
        val foundCodelist = projectRepository.findByRef(projectRef).codelist.find { codelist ->
            codelist.ref == codelistRef
        }
        Optional.ofNullable(foundCodelist)
            .orElseThrow { NotFoundException("Codelist not found by ref $codelistRef in project by ref $projectRef") }
        val codelistMapper = CodelistUpdateMapper().toEntity(codelist)
        codelistRepository.update(
            "title = ?1, description = ?2 where id= ?3",
            codelistMapper.title,
            codelistMapper.description,
            //codelist.deletedDate,
            foundCodelist!!.id
        )
        return Response.ok(codelist).build()
    }

    //fun exists(id: Long?): Boolean = codelistRepository.count("id", id) == 1L
    fun refExists(ref: String): Boolean = codelistRepository.count("ref", ref) == 1L

}