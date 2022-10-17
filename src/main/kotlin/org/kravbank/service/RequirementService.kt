package org.kravbank.service

import org.kravbank.domain.Requirement
import org.kravbank.exception.BackendException
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.requirement.RequirementForm
import org.kravbank.utils.form.requirement.RequirementFormUpdate
import org.kravbank.repository.RequirementRepository
import org.kravbank.utils.form.requirement.RequirementFormCreate
import org.kravbank.utils.mapper.requirement.RequirementCreateMapper
import org.kravbank.utils.mapper.requirement.RequirementMapper
import org.kravbank.utils.mapper.requirement.RequirementUpdateMapper
import java.net.URI
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class RequirementService(
    val requirementRepository: RequirementRepository,
    val projectRepository: ProjectRepository,
    val needRepository: NeedRepository
) {
    // @CacheResult(cacheName = "requirement-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, requirementRef: String): Requirement {
        val project = projectRepository.findByRef(projectRef)
        return requirementRepository.findByRef(project.id, requirementRef)
    }

    // @CacheResult(cacheName = "requirement-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): MutableList<Requirement> {
        val foundProject = projectRepository.findByRef(projectRef)
        return requirementRepository.listAllRequirements(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, newRequirement: RequirementFormCreate): Requirement {
        val project = projectRepository.findByRef(projectRef)
        newRequirement.project = project
        val need = newRequirement.need
        val foundNeed = needRepository.findByRefRequirement(need)
        val requirement = RequirementCreateMapper().toEntity(newRequirement)
        requirement.need = foundNeed
        /**
         * todo
         * debug need test
         */
        print("TO STRING NEED FROM CREATE REQUIREMENT: $foundNeed")
        requirementRepository.createRequirement(requirement)
        return requirement
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, requirementRef: String): Requirement {
        val foundProject = projectRepository.findByRef(projectRef)
        return requirementRepository.deleteRequirement(foundProject.id, requirementRef)
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, requirementRef: String, updatedRequirement: RequirementFormUpdate): Requirement {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        val requirement = RequirementUpdateMapper().toEntity(updatedRequirement)
        requirementRepository.updateRequirement(foundRequirement.id, requirement)
        return requirement
    }
}
