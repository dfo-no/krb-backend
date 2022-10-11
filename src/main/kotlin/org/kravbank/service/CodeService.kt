package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.exception.BackendException
import org.kravbank.repository.CodeRepository
import org.kravbank.repository.CodelistRepository
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
    val codelistRepository: CodelistRepository,
    val projectRepository: ProjectRepository
) {
    // @CacheResult(cacheName = "code-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, codelistRef: String, codeRef: String): Response {
        val project = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(project.id, codelistRef)
        val foundCode = codeRepository.findByRef(foundCodelist.id, codeRef)
        val codeForm = CodeMapper().fromEntity(foundCode)
        return Response.ok(codeForm).build()
    }

    // @CacheResult(cacheName = "code-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String, codelistRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        val foundCode = codeRepository.listAllCodes(foundCodelist.id)
        val codelistForm = ArrayList<CodeForm>()
        for (c in foundCode) codelistForm.add(CodeMapper().fromEntity(c))
        return Response.ok(codelistForm).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, codelistRef: String, codeForm: CodeForm): Response {
        val project = projectRepository.findByRef(projectRef)
        val codelist = codelistRepository.findByRef(project.id, codelistRef)
        codeForm.codelist = codelist
        val code = CodeMapper().toEntity(codeForm)
        codeRepository.createCode(code)
        return Response.created(URI.create("/api/v1/projects/$projectRef/codelists/$codelistRef/codes/" + code.ref)).build()
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, codelistRef: String, codeRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        codeRepository.deleteCode(foundCodelist.id, codeRef)
        return Response.noContent().build()
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, codelistRef: String, codeRef: String, codeForm: CodeFormUpdate): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        val foundCode = codeRepository.findByRef(foundCodelist.id, codeRef)
        val code = CodeUpdateMapper().toEntity(codeForm)
        codeRepository.updateCode(foundCode.id, code)
        return Response.ok(codeForm).build()
    }
}