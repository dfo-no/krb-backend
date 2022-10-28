package org.kravbank.service

import org.kravbank.dao.RequirementVariantForm
import org.kravbank.domain.RequirementVariant
import org.kravbank.lang.BackendException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementRepository
import org.kravbank.repository.RequirementVariantRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequirementVariantService(
    val requirementVariantRepository: RequirementVariantRepository,
    val requirementRepository: RequirementRepository,
    val projectRepository: ProjectRepository
) {
    @Throws(BackendException::class)
    fun get(projectRef: String, requirementRef: String, reqVariantRef: String): RequirementVariant {
        val project = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(project.id, requirementRef)
        return requirementVariantRepository.findByRef(foundRequirement.id, reqVariantRef)
    }

    @Throws(BackendException::class)
    fun list(projectRef: String, requirementRef: String): List<RequirementVariant> {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        return requirementVariantRepository.listAllRequirementVariants(foundRequirement.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, requirementRef: String, newReqVariant: RequirementVariantForm): RequirementVariant {
        val project = projectRepository.findByRef(projectRef)
        val requirement = requirementRepository.findByRef(project.id, requirementRef)
        val reqVariant = RequirementVariantForm().toEntity(newReqVariant)
        reqVariant.requirement = requirement
        requirementVariantRepository.createRequirementVariant(reqVariant)
        return reqVariant
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, requirementRef: String, reqVariantRef: String): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        return requirementVariantRepository.deleteRequirementVariant(foundRequirement.id, reqVariantRef)
    }

    @Throws(BackendException::class)
    fun update(
        projectRef: String,
        requirementRef: String,
        reqVariantRef: String,
        updatedReqVariant: RequirementVariantForm
    ): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        val foundReqVariant = requirementVariantRepository.findByRef(foundRequirement.id, reqVariantRef)
        val update = RequirementVariantForm().toEntity(updatedReqVariant)
        requirementVariantRepository.updateRequirementVariant(foundReqVariant.id, update)
        return update.apply { ref = foundReqVariant.ref }
    }
}