package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.exception.BackendException
import org.kravbank.utils.form.need.NeedForm
import org.kravbank.utils.form.need.NeedFormUpdate
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.mapper.need.NeedMapper
import org.kravbank.utils.mapper.need.NeedUpdateMapper
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class NeedService(
    val needRepository: NeedRepository,
    val projectRepository: ProjectRepository
) {
  //  @CacheResult(cacheName = "need-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, needRef: String): Response {
      val project = projectRepository.findByRef(projectRef)
      val foundNeed = needRepository.findByRef(project.id, needRef)
      val needForm = NeedMapper().fromEntity(foundNeed)
      return Response.ok(needForm).build()
    }

    //@CacheResult(cacheName = "need-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundNeeds = needRepository.listAllNeeds(foundProject.id)
        val needForm = ArrayList<NeedForm>()
        for (n in foundNeeds) needForm.add(NeedMapper().fromEntity(n))
        return Response.ok(needForm).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, needForm: NeedForm): Response {
        val project = projectRepository.findByRef(projectRef)
        needForm.project = project
        val need = NeedMapper().toEntity(needForm)
        needRepository.createNeed(need)
        return Response.created(URI.create("/api/v1/projects/$projectRef/needs/" + need.ref)).build()
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, needRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        needRepository.deleteNeed(foundProject.id, needRef)
        return Response.noContent().build()
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, needRef: String, needForm: NeedFormUpdate): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundNeed = needRepository.findByRef(foundProject.id, needRef)
        val need = NeedUpdateMapper().toEntity(needForm)
        needRepository.updateNeed(foundNeed.id, need)
        return Response.ok(needForm).build()
    }
}