package org.kravbank.service

import org.kravbank.dao.RequirementVariantForm
import org.kravbank.domain.RequirementVariant
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementRepository
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENTVARIANT_BADREQUEST_CREATE
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequirementVariantService(
    val requirementVariantRepository: RequirementVariantRepository,
    val requirementRepository: RequirementRepository,
    val projectRepository: ProjectRepository
) {
    @Throws(BackendException::class)
    fun get(projectRef: String, requirementRef: String, requirementVariantRef: String): RequirementVariant {
        val project = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(project.id, requirementRef)

        return requirementVariantRepository.findByRef(foundRequirement.id, requirementVariantRef)
    }

    fun list(projectRef: String, requirementRef: String): List<RequirementVariant> {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)

        return requirementVariantRepository.listAllRequirementVariants(foundRequirement.id)
    }

    @Throws(BackendException::class)
    fun create(
        projectRef: String,
        requirementRef: String,
        newRequirementVariant: RequirementVariantForm
    ): RequirementVariant {
        val project = projectRepository.findByRef(projectRef)
        val requirement = requirementRepository.findByRef(project.id, requirementRef)

        val requirementVariant = RequirementVariantForm().toEntity(newRequirementVariant)
        requirementVariant.requirement = requirement

        requirementVariantRepository.persistAndFlush(requirementVariant)
        if (!requirementVariantRepository.isPersistent(requirementVariant))
            throw BadRequestException(
                REQUIREMENTVARIANT_BADREQUEST_CREATE
            )

        return requirementVariant
    }

    fun delete(projectRef: String, requirementRef: String, requirementVariantRef: String): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        val foundRequirementVariant = requirementVariantRepository.findByRef(foundRequirement.id, requirementVariantRef)

        requirementVariantRepository.deleteById(foundRequirementVariant.id)
        return foundRequirementVariant
    }

    @Throws(BackendException::class)
    fun update(
        projectRef: String,
        requirementRef: String,
        requirementVariantRef: String,
        updatedReqVariant: RequirementVariantForm
    ): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        val foundReqVariant = requirementVariantRepository.findByRef(foundRequirement.id, requirementVariantRef)

        val update = RequirementVariantForm().toEntity(updatedReqVariant)
        requirementVariantRepository.updateRequirementVariant(foundReqVariant.id, update)
        return update.apply { ref = foundReqVariant.ref }
    }
}