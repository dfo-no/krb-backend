package org.kravbank.service

import org.kravbank.dao.CodeForm
import org.kravbank.domain.Code
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.CodeRepository
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.Messages.RepoErrorMsg.CODE_BADREQUEST_CREATE
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodeService(
    val codeRepository: CodeRepository,
    val codelistRepository: CodelistRepository,
    val projectRepository: ProjectRepository
) {

    fun get(projectRef: String, codelistRef: String, codeRef: String): Code {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        return codeRepository.findByRef(foundCodelist.id, codeRef)
    }

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

        codeRepository.persistAndFlush(code)
        if (!codeRepository.isPersistent(code)) throw BadRequestException(CODE_BADREQUEST_CREATE)

        return code
    }

    fun delete(projectRef: String, codelistRef: String, codeRef: String): Boolean {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        val foundCode = codeRepository.findByRef(foundCodelist.id, codeRef)

        return codeRepository.deleteById(foundCode.id)
    }

    fun update(projectRef: String, codelistRef: String, codeRef: String, updatedCode: CodeForm): Code {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        val foundCode = codeRepository.findByRef(foundCodelist.id, codeRef)

        val update = CodeForm().toEntity(updatedCode)
        codeRepository.updateCode(foundCode.id, update)
        return update.apply { ref = foundCode.ref }
    }
}