package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.exception.BackendException
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.form.codelist.CodelistFormUpdate
import org.kravbank.utils.mapper.codelist.CodelistMapper
import org.kravbank.utils.mapper.codelist.CodelistUpdateMapper
import java.net.URI
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response
import kotlin.collections.ArrayList


@ApplicationScoped
class CodelistService(
    val codelistRepository: CodelistRepository,
    val projectRepository: ProjectRepository
) {
    //@CacheResult(cacheName = "codelist-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, codelistRef: String): Response {
        val project = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(project.id, codelistRef)
        val codelistForm = CodelistMapper().fromEntity(foundCodelist)
        return Response.ok(codelistForm).build()
    }

    //@CacheResult(cacheName = "codelist-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelists = codelistRepository.listAllCodelists(foundProject.id)
        //println("FOUND CODELIST: ${foundCodelists.size}")
        val codelistForm = ArrayList<CodelistForm>()
        for (n in foundCodelists) codelistForm.add(CodelistMapper().fromEntity(n))
        return Response.ok(codelistForm).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, codelistForm: CodelistForm): Response {
        val project = projectRepository.findByRef(projectRef)
        codelistForm.project = project
        val codelist = CodelistMapper().toEntity(codelistForm)
        codelistRepository.createCodelist(codelist)
        return Response.created(URI.create("/api/v1/projects/$projectRef/codelists/" + codelist.ref)).build()
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, codelistRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        codelistRepository.deleteCodelist(foundProject.id, codelistRef)
        return Response.noContent().build()
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, codelistRef: String, codelistForm: CodelistFormUpdate): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundCodelist = codelistRepository.findByRef(foundProject.id, codelistRef)
        val codelist = CodelistUpdateMapper().toEntity(codelistForm)
        codelistRepository.updateCodelist(foundCodelist.id, codelist)
        return Response.ok(codelistForm).build()
    }
}