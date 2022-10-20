package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.domain.RequirementVariant
import org.kravbank.lang.BackendException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementRepository
import org.kravbank.utils.form.requirementvariant.RequirementVariantForm
import org.kravbank.utils.form.requirementvariant.RequirementVariantFormUpdate
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.utils.mapper.requirementvariant.RequirementVariantMapper
import org.kravbank.utils.mapper.requirementvariant.RequirementVariantUpdateMapper
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class RequirementVariantService(
    val requirementVariantRepository: RequirementVariantRepository,
    val requirementRepository: RequirementRepository,
    val projectRepository: ProjectRepository
) {
    // @CacheResult(cacheName = "requirementvariant-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, requirementRef: String, reqVariantRef: String): RequirementVariant {
        val project = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(project.id, requirementRef)
        return requirementVariantRepository.findByRef(foundRequirement.id, reqVariantRef)
    }

    //@CacheResult(cacheName = "requirementvariant-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String, requirementRef: String): MutableList<RequirementVariant> {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        return requirementVariantRepository.listAllRequirementVariants(foundRequirement.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, requirementRef: String, newReqVariant: RequirementVariantForm): RequirementVariant {
        val project = projectRepository.findByRef(projectRef)
        val requirement = requirementRepository.findByRef(project.id, requirementRef)
        newReqVariant.requirement = requirement
        val reqVariant = RequirementVariantMapper().toEntity(newReqVariant)
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
        updatedReqVariant: RequirementVariantFormUpdate
    ): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        val foundReqVariant = requirementVariantRepository.findByRef(foundRequirement.id, reqVariantRef)
        val reqVariant = RequirementVariantUpdateMapper().toEntity(updatedReqVariant)
        requirementVariantRepository.updateRequirementVariant(foundReqVariant.id, reqVariant)
        return reqVariant
    }
}