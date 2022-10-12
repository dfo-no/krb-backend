package org.kravbank.service

import org.kravbank.domain.Code
import org.kravbank.exception.BackendException
import org.kravbank.repository.CodeRepository
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.code.CodeForm
import org.kravbank.utils.form.code.CodeFormUpdate
import org.kravbank.utils.mapper.code.CodeMapper
import org.kravbank.utils.mapper.code.CodeUpdateMapper
import java.net.URI
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class CodeService(
    val codeRepository: CodeRepository,
    val codelistRepository: CodelistRepository,
    val projectRepository: ProjectRepository
) {
    // @CacheResult(cacheName = "code-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, codelistRef: String, codeRef: String): Code {
        val project = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(project.id, codelistRef)
        return codeRepository.findByRef(foundCodelist.id, codeRef)
    }

    // @CacheResult(cacheName = "code-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String, codelistRef: String): MutableList<Code> {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        return codeRepository.listAllCodes(foundCodelist.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, codelistRef: String, codeForm: CodeForm): Code {
        val project = projectRepository.findByRef(projectRef)
        val codelist = codelistRepository.findByRef(project.id, codelistRef)
        codeForm.codelist = codelist
        val code = CodeMapper().toEntity(codeForm)
        codeRepository.createCode(code)
        return code
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, codelistRef: String, codeRef: String) {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        codeRepository.deleteCode(foundCodelist.id, codeRef)
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, codelistRef: String, codeRef: String, codeForm: CodeFormUpdate): Code {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        val foundCode = codeRepository.findByRef(foundCodelist.id, codeRef)
        val code = CodeUpdateMapper().toEntity(codeForm)
        codeRepository.updateCode(foundCode.id, code)
        return code
    }
}