package org.kravbank.service

import org.kravbank.dao.NeedForm
import org.kravbank.domain.Need
import org.kravbank.lang.BackendException
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
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
    fun list(projectRef: String): List<Need> {
        val foundProject = projectRepository.findByRef(projectRef)
        return needRepository.listAllNeeds(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, newNeed: NeedForm): Need {
        val project = projectRepository.findByRef(projectRef)
        val need = NeedForm().toEntity(newNeed)
        need.project = project
        needRepository.createNeed(need)
        return need
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, needRef: String): Need{
        val foundProject = projectRepository.findByRef(projectRef)
        return needRepository.deleteNeed(foundProject.id, needRef)
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, needRef: String, updatedNeed: NeedForm): Need {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundNeed = needRepository.findByRef(foundProject.id, needRef)
        val update = NeedForm().toEntity(updatedNeed)
        needRepository.updateNeed(foundNeed.id, update)
        return update.apply { ref = foundNeed.ref }
    }
}