package org.kravbank.service

import org.kravbank.dao.RequirementForm
import org.kravbank.domain.Requirement
import org.kravbank.lang.BackendException
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequirementService(
    val requirementRepository: RequirementRepository,
    val projectRepository: ProjectRepository,
    val needRepository: NeedRepository
) {
    @Throws(BackendException::class)
    fun get(projectRef: String, requirementRef: String): Requirement {
        val project = projectRepository.findByRef(projectRef)
        return requirementRepository.findByRef(project.id, requirementRef)
    }

    @Throws(BackendException::class)
    fun list(projectRef: String): List<Requirement> {
        val foundProject = projectRepository.findByRef(projectRef)
        return requirementRepository.listAllRequirements(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, newRequirement: RequirementForm): Requirement {
        val project = projectRepository.findByRef(projectRef)
        val foundNeed = needRepository.findByRefRequirement(newRequirement.needRef)
        val requirement = RequirementForm().toEntity(newRequirement)
        requirement.project = project
        requirement.need = foundNeed
        requirementRepository.createRequirement(requirement)
        return requirement
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, requirementRef: String): Requirement {
        val foundProject = projectRepository.findByRef(projectRef)
        return requirementRepository.deleteRequirement(foundProject.id, requirementRef)
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, requirementRef: String, updatedRequirement: RequirementForm): Requirement {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        val update = RequirementForm().toEntity(updatedRequirement)
        requirementRepository.updateRequirement(foundRequirement.id, update)
        return update.apply { ref = foundRequirement.ref }
    }
}
