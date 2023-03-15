package org.kravbank.service

import org.kravbank.dao.RequirementForm
import org.kravbank.domain.Requirement
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementRepository
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENT_BADREQUEST_CREATE
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequirementService(
    val requirementRepository: RequirementRepository,
    val projectRepository: ProjectRepository,
    val needRepository: NeedRepository
) {

    fun get(
        projectRef: String,
        needRef: String,
        requirementRef: String
    ): Requirement {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundNeed = needRepository.findByRef(foundProject.id, needRef)

        return requirementRepository.findByRef(foundProject.id, foundNeed.id, requirementRef)
    }

    fun list(
        projectRef: String,
        needRef: String,
    ): List<Requirement> {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundNeed = needRepository.findByRef(foundProject.id, needRef)

        return requirementRepository.listAllRequirements(foundProject.id, foundNeed.id)
    }

    @Throws(BackendException::class)
    fun create(
        projectRef: String,
        needRef: String,
        newRequirement: RequirementForm
    ): Requirement {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundNeed = needRepository.findByRef(foundProject.id, needRef)

        return RequirementForm().toEntity(newRequirement).apply {
            project = foundProject
            need = foundNeed
        }.also {
            requirementRepository.persistAndFlush(it)
            if (!requirementRepository.isPersistent(it)) {
                throw BadRequestException(REQUIREMENT_BADREQUEST_CREATE)
            }
        }
    }

    @Throws(BackendException::class)
    fun delete(
        projectRef: String,
        needRef: String,
        requirementRef: String
    ): Requirement {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundNeed = needRepository.findByRef(foundProject.id, needRef)

        return try {
            requirementRepository.findByRef(
                foundProject.id,
                foundNeed.id,
                requirementRef
            )
                .also {
                    requirementRepository.deleteById(it.id)
                }
        } catch (ex: Exception) {
            throw BackendException("Failed to delete requirement")
        }
    }

    @Throws(BackendException::class)
    fun update(
        projectRef: String,
        requirementRef: String,
        needRef: String,
        updatedRequirement: RequirementForm
    ): Requirement {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundNeed = needRepository.findByRef(foundProject.id, needRef)

        val foundRequirement = requirementRepository.findByRef(
            foundProject.id,
            foundNeed.id,
            requirementRef
        )

        return RequirementForm().toEntity(updatedRequirement).apply {
            ref = foundRequirement.ref
        }.also {
            requirementRepository.updateRequirement(foundRequirement.id, it)
        }
    }
}
