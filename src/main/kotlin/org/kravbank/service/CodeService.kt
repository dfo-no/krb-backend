package org.kravbank.service

import org.kravbank.dao.CodeForm
import org.kravbank.domain.Code
import org.kravbank.lang.BackendException
import org.kravbank.repository.CodeRepository
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodeService(
    val codeRepository: CodeRepository,
    val codelistRepository: CodelistRepository,
    val projectRepository: ProjectRepository
) {
    @Throws(BackendException::class)
    fun get(projectRef: String, codelistRef: String, codeRef: String): Code {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        return codeRepository.findByRef(foundCodelist.id, codeRef)
    }

    @Throws(BackendException::class)
    fun list(projectRef: String, codelistRef: String): List<Code> {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        return codeRepository.listAllCodes(foundCodelist.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, codelistRef: String, newCode: CodeForm): Code {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        val code = CodeForm().toEntity(newCode)
        code.codelist = foundCodelist
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
    fun update(projectRef: String, codelistRef: String, codeRef: String, updatedCode: CodeForm): Code {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        val foundCode = codeRepository.findByRef(foundCodelist.id, codeRef)
        val update = CodeForm().toEntity(updatedCode)
        codeRepository.updateCode(foundCode.id, update)
        return update.apply { ref = foundCode.ref }
    }
}