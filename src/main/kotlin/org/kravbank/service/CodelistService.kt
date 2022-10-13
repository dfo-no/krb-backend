package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import org.kravbank.exception.BackendException
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.form.codelist.CodelistFormUpdate
import org.kravbank.utils.mapper.codelist.CodelistMapper
import org.kravbank.utils.mapper.codelist.CodelistUpdateMapper
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodelistService(
    val codelistRepository: CodelistRepository,
    val projectRepository: ProjectRepository
) {
    //@CacheResult(cacheName = "codelist-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, codelistRef: String): Codelist {
        val project = projectRepository.findByRef(projectRef)
        return codelistRepository.findByRef(project.id, codelistRef)
    }

    //@CacheResult(cacheName = "codelist-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): MutableList<Codelist> {
        val foundProject = projectRepository.findByRef(projectRef)
        return codelistRepository.listAllCodelists(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, newCodelist: CodelistForm): Codelist {
        val project = projectRepository.findByRef(projectRef)
        newCodelist.project = project
        //mapper til entity
        val codelist = CodelistMapper().toEntity(newCodelist)
        codelistRepository.createCodelist(codelist)
        return codelist
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, codelistRef: String): Codelist {
        val foundProject = projectRepository.findByRef(projectRef)
        return codelistRepository.deleteCodelist(foundProject.id, codelistRef)
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, codelistRef: String, updatedCodelist: CodelistFormUpdate): Codelist {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        //mapper til entity
        val codelist = CodelistUpdateMapper().toEntity(updatedCodelist)
        codelistRepository.updateCodelist(foundCodelist.id, codelist)
        return codelist
    }
}