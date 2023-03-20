package org.kravbank.service

import org.kravbank.dao.NeedForm
import org.kravbank.domain.Need
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.Messages.RepoErrorMsg.NEED_BADREQUEST_CREATE
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class NeedService(
    val needRepository: NeedRepository,
    val projectRepository: ProjectRepository
) {
    fun get(projectRef: String, needRef: String): Need {
        val project = projectRepository.findByRef(projectRef)
        return needRepository.findByRef(project.id, needRef)
    }

    fun list(projectRef: String): List<Need> {
        val foundProject = projectRepository.findByRef(projectRef)
        return needRepository.listAllNeeds(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, newNeed: NeedForm): Need {
        val foundProject = projectRepository.findByRef(projectRef)

        return NeedForm().toEntity(newNeed).apply {
            project = foundProject

        }.also {
            needRepository.persistAndFlush(it)
            if (!needRepository.isPersistent(it))
                throw BadRequestException(NEED_BADREQUEST_CREATE)
        }
    }

    fun delete(projectRef: String, needRef: String): Boolean {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundNeed = needRepository.findByRef(foundProject.id, needRef)

        return try {
            needRepository.deleteById(foundNeed.id)
        } catch (e: Exception) {
            throw BackendException("Failed to delete need")
        }
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