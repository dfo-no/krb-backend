package org.kravbank.service

import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
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
    @Throws(BackendException::class)
    fun list(projectRef: String): Response {
        //list requirement by project ref
        val foundProjectRequirement = projectRepository.findByRef(projectRef).requirements
        //convert to array of form
        val requirementFormList = ArrayList<RequirementForm>()
        for (p in foundProjectRequirement) requirementFormList.add(RequirementMapper().fromEntity(p))
        //returns the custom requirement form
        return Response.ok(requirementFormList).build()
    }

    @Throws(BackendException::class)
    fun get(projectRef: String, requirementRef: String): Response {
        val foundProjectRequirement = projectRepository.findByRef(projectRef).requirements.find { requirement ->
            requirement.ref == requirementRef
        }
        Optional.ofNullable(foundProjectRequirement)
            .orElseThrow { NotFoundException("Requirement not found by ref $requirementRef in project by ref $projectRef") }
        val requirementMapper = RequirementMapper().fromEntity(foundProjectRequirement!!)
        return Response.ok(requirementMapper).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, requirement: RequirementForm): Response {
        val requirementMapper = RequirementMapper().toEntity(requirement)
        val project = projectRepository.findByRef(projectRef)
        project.requirements.add(requirementMapper)
        projectService.updateProject(project.id, project)
        if (requirementMapper.isPersistent)
            return Response.created(URI.create("/api/v1/projects/$projectRef/requirements/" + requirement.ref)).build()
        else throw BadRequestException("Bad request! Did not create requirement")
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, requirementRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = foundProject.requirements.find { requirement -> requirement.ref == requirementRef }
        Optional.ofNullable(foundRequirement)
            .orElseThrow { NotFoundException("Requirement not found by ref $requirementRef in project by ref $projectRef") }
        val deleted = foundProject.requirements.remove(foundRequirement)
        if (deleted) {
            projectService.updateProject(foundProject.id, foundProject)
            return Response.noContent().build()
        } else throw BadRequestException("Bad request! Requirement not deleted")
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, requirementRef: String, requirement: RequirementFormUpdate): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = foundProject.requirements.find { req -> req.ref == requirementRef }
        Optional.ofNullable(foundRequirement)
            .orElseThrow { NotFoundException("Requirement not found by ref $requirementRef in project by ref $projectRef") }
        val requirementMapper = RequirementUpdateMapper().toEntity(requirement)
        requirementRepository.update(
            "title = ?1, description = ?2 where id= ?3",
            requirementMapper.title,
            requirementMapper.description,
            foundRequirement!!.id
        )
        return Response.ok(requirement).build()
    }
}