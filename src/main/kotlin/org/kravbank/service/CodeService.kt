package org.kravbank.service

import org.kravbank.domain.Code
import org.kravbank.lang.BackendException
import org.kravbank.repository.CodeRepository
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.code.dto.CodeForm
import org.kravbank.utils.code.dto.CodeFormUpdate
import org.kravbank.utils.code.mapper.CodeMapper
import org.kravbank.utils.code.mapper.CodeUpdateMapper
import javax.enterprise.context.ApplicationScoped

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
    fun create(projectRef: String, codelistRef: String, newCode: CodeForm): Code {
        val project = projectRepository.findByRef(projectRef)
        val codelist = codelistRepository.findByRef(project.id, codelistRef)
        newCode.codelist = codelist
        val code = CodeMapper().toEntity(newCode)
        codeRepository.createCode(code)
        return code
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, codelistRef: String, codeRef: String): Code {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        return codeRepository.deleteCode(foundCodelist.id, codeRef)
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, codelistRef: String, codeRef: String, updatedCode: CodeFormUpdate): Code {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        val foundCode = codeRepository.findByRef(foundCodelist.id, codeRef)
        val code = CodeUpdateMapper().toEntity(updatedCode)
        codeRepository.updateCode(foundCode.id, code)
        return code
    }
}