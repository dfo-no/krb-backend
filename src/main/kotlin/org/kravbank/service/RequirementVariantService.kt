package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.exception.BackendException
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
    fun get(projectRef: String, requirementRef: String, reqVariantRef: String): Response {
        val project = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(project.id, requirementRef)
        val foundReqVariant = requirementVariantRepository.findByRef(foundRequirement.id, reqVariantRef)
        val reqVariantForm = RequirementVariantMapper().fromEntity(foundReqVariant)
        return Response.ok(reqVariantForm).build()
    }

    //@CacheResult(cacheName = "requirementvariant-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String, requirementRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        val foundReqVariant = requirementVariantRepository.listAllRequirementVariants(foundRequirement.id)
        val reqVariantForm = ArrayList<RequirementVariantForm>()
        for (c in foundReqVariant) reqVariantForm.add(RequirementVariantMapper().fromEntity(c))
        return Response.ok(reqVariantForm).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, requirementRef: String, reqVariantForm: RequirementVariantForm): Response {
        val project = projectRepository.findByRef(projectRef)
        val requirement = requirementRepository.findByRef(project.id, requirementRef)
        reqVariantForm.requirement = requirement
        val reqVariant = RequirementVariantMapper().toEntity(reqVariantForm)
        requirementVariantRepository.createRequirementVariant(reqVariant)
        return Response.created(URI.create("/api/v1/projects/$projectRef/requirements/$requirementRef/requirementvariants/" + reqVariantForm.ref))
            .build()
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, requirementRef: String, reqVariantRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        requirementVariantRepository.deleteRequirementVariant(foundRequirement.id, reqVariantRef)
        return Response.noContent().build()
    }

    @Throws(BackendException::class)
    fun update(
        projectRef: String,
        requirementRef: String,
        reqVariantRef: String,
        reqVariantForm: RequirementVariantFormUpdate
    ): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        val foundReqVariant = requirementVariantRepository.findByRef(foundRequirement.id, reqVariantRef)
        val reqVariant = RequirementVariantUpdateMapper().toEntity(reqVariantForm)
        requirementVariantRepository.updateRequirementVariant(foundReqVariant.id, reqVariant)
        return Response.ok(reqVariantForm).build()
    }
}