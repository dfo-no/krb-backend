package org.kravbank.service

import org.kravbank.dao.CodelistForm
import org.kravbank.domain.Codelist
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_BADREQUEST_CREATE
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodelistService(
    val codelistRepository: CodelistRepository,
    val projectRepository: ProjectRepository
) {
    @Throws(BackendException::class)
    fun get(projectRef: String, codelistRef: String): Codelist {
        val project = projectRepository.findByRef(projectRef)
        return codelistRepository.findByRef(project.id, codelistRef)
    }

    @Throws(BackendException::class)
    fun list(projectRef: String): List<Codelist> {
        val foundProject = projectRepository.findByRef(projectRef)
        return codelistRepository.listAllCodelists(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, newCodelist: CodelistForm): Codelist {
        val project = projectRepository.findByRef(projectRef)
        val codelist = CodelistForm().toEntity(newCodelist)
        codelist.project = project
        codelistRepository.persistAndFlush(codelist)

        if (!codelistRepository.isPersistent(codelist)) throw BadRequestException(CODELIST_BADREQUEST_CREATE)

        return codelist

    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, codelistRef: String): Boolean {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        return codelistRepository.deleteById(foundCodelist.id)
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, codelistRef: String, updatedCodelist: CodelistForm): Codelist {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        val update = CodelistForm().toEntity(updatedCodelist)
        codelistRepository.updateCodelist(foundCodelist.id, update)
        return update.apply { ref = foundCodelist.ref }
    }
}