package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.domain.Need
import org.kravbank.exception.BackendException
import org.kravbank.utils.form.need.NeedForm
import org.kravbank.utils.form.need.NeedFormUpdate
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.mapper.need.NeedMapper
import org.kravbank.utils.mapper.need.NeedUpdateMapper
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class NeedService(
    val needRepository: NeedRepository,
    val projectRepository: ProjectRepository
) {
    //  @CacheResult(cacheName = "need-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, needRef: String): Need {
        val project = projectRepository.findByRef(projectRef)
        return needRepository.findByRef(project.id, needRef)
    }

    //@CacheResult(cacheName = "need-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): MutableList<Need> {
        val foundProject = projectRepository.findByRef(projectRef)
        return needRepository.listAllNeeds(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, needForm: NeedForm): Need {
        val project = projectRepository.findByRef(projectRef)
        needForm.project = project
        val need = NeedMapper().toEntity(needForm)
        needRepository.createNeed(need)
        return need
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, needRef: String): Need{
        val foundProject = projectRepository.findByRef(projectRef)
        return needRepository.deleteNeed(foundProject.id, needRef)
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, needRef: String, needForm: NeedFormUpdate): Need {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundNeed = needRepository.findByRef(foundProject.id, needRef)
        val need = NeedUpdateMapper().toEntity(needForm)
        needRepository.updateNeed(foundNeed.id, need)
        return need
    }
}