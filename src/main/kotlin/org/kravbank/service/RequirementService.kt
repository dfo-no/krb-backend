package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.exception.BackendException
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.requirement.RequirementForm
import org.kravbank.utils.form.requirement.RequirementFormUpdate
import org.kravbank.repository.RequirementRepository
import org.kravbank.utils.mapper.requirement.RequirementMapper
import org.kravbank.utils.mapper.requirement.RequirementUpdateMapper
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class RequirementService(
    val requirementRepository: RequirementRepository,
    val projectService: ProjectService,
    val projectRepository: ProjectRepository
) {
    // @CacheResult(cacheName = "requirement-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, requirementRef: String): Response {
        val project = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(project.id, requirementRef)
        val requirementForm = RequirementMapper().fromEntity(foundRequirement)
        return Response.ok(requirementForm).build()
    }

    // @CacheResult(cacheName = "requirement-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.listAllRequirements(foundProject.id)
        val requirementsForm = ArrayList<RequirementForm>()
        for (n in foundRequirement) requirementsForm.add(RequirementMapper().fromEntity(n))
        return Response.ok(requirementsForm).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, requirementForm: RequirementForm): Response {
        val project = projectRepository.findByRef(projectRef)
        requirementForm.project = project
        val requirement = RequirementMapper().toEntity(requirementForm)
        requirementRepository.createRequirement(requirement)
        return Response.created(URI.create("/api/v1/projects/$projectRef/requirements/" + requirement.ref)).build()
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, requirementRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        requirementRepository.deleteRequirement(foundProject.id, requirementRef)
        return Response.noContent().build()
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, requirementRef: String, requirementForm: RequirementFormUpdate): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        val requirement = RequirementUpdateMapper().toEntity(requirementForm)
        requirementRepository.updateRequirement(foundRequirement.id, requirement)
        return Response.ok(requirementForm).build()
    }
}
