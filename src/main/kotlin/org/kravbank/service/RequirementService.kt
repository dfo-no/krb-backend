package org.kravbank.service

import org.kravbank.domain.Requirement
import org.kravbank.utils.form.requirement.RequirementForm
import org.kravbank.utils.form.requirement.RequirementFormUpdate
import org.kravbank.repository.RequirementRepository
import org.kravbank.utils.mapper.requirement.RequirementMapper
import org.kravbank.utils.mapper.requirement.RequirementUpdateMapper
import java.lang.IllegalArgumentException
import java.net.URI
import java.util.ArrayList
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class RequirementService(val requirementRepository: RequirementRepository, val projectService: ProjectService) {
    fun getRequirementByRefFromService(projectRef: String, requirementRef: String): Response {
        if (projectService.refExists(projectRef) && refExists(requirementRef)) {
            val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            val requirement = project.requirements.find { requirement -> requirement.ref == requirementRef }
            val requirementMapper = org.kravbank.utils.mapper.requirement.RequirementMapper().fromEntity(requirement!!)
            return Response.ok(requirementMapper).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    fun listRequirementsFromService(projectRef: String): Response {
        return try {
            if (projectService.refExists(projectRef)) {
                //list requirements by project ref
                val projectRequirementsList = projectService.getProjectByRefCustomRepo(projectRef)!!.requirements
                //convert to array of form
                val requirementsFormList = ArrayList<RequirementForm>()
                for (n in projectRequirementsList) requirementsFormList.add(
                    org.kravbank.utils.mapper.requirement.RequirementMapper().fromEntity(n))
                //returns the custom requirement form
                Response.ok(requirementsFormList).build()
                //
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("GET list of requirements failed")
        }
    }

    fun createRequirementFromService(projectRef: String, requirement: RequirementForm): Response {
        //adds a requirement to relevant project
        try {
            val requirementMapper = org.kravbank.utils.mapper.requirement.RequirementMapper().toEntity(requirement)
            if (projectService.refExists(projectRef)) {
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                project.requirements.add(requirementMapper)
                projectService.updateProject(project.id, project)
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
            return if (requirementMapper.isPersistent) {
                Response.created(URI.create("/api/v1/projects/$projectRef/requirements/" + requirement.ref)).build();
            } else {
                Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("POST requirement failed")
        }
    }

    fun deleteRequirementFromService(projectRef: String, requirementRef: String): Response {
        return try {
            //val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            //val requirement = getRequirementByRefFromService(projectRef, requirementRef)

            if (projectService.refExists(projectRef) && refExists(requirementRef)) {
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                val requirement = project.requirements.find { requirement -> requirement.ref == requirementRef }
                val deleted = deleteRequirement(requirement!!.id)
                if (deleted) {
                    project.requirements.remove(requirement)
                    Response.noContent().build()
                } else {
                    Response.status(Response.Status.BAD_REQUEST).build()
                }
            } else
                Response.status(Response.Status.NOT_FOUND).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("DELETE requirement failed!")
        }
    }

    fun updateRequirementFromService(
        projectRef: String,
        requirementRef: String,
        requirement: RequirementFormUpdate
    ): Response {
        if (projectService.refExists(projectRef) && refExists(requirementRef)) {
            // if requirement exists in this project
            val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            //val foundProduct = getProductByRefCustomRepo(requirementRef)
            val requirementInProject = project.requirements.find { pub -> pub.ref == requirementRef }
            val requirementMapper = org.kravbank.utils.mapper.requirement.RequirementUpdateMapper().toEntity(requirement)

            //if (requirement.project.ref == project.ref)

            return if (requirementInProject != null) {
                requirementRepository.update(
                    "title = ?1, description = ?2 where id= ?3",
                    requirementMapper.title,
                    requirementMapper.description,
                    requirementInProject.id
                )
                Response.ok(requirement).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } else {

            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }


    fun listRequirements(): MutableList<Requirement> = requirementRepository.listAll()

    fun getRequirement(id: Long): Requirement = requirementRepository.findById(id)

    fun createRequirement(requirement: Requirement) = requirementRepository.persistAndFlush(requirement)

    fun exists(id: Long): Boolean = requirementRepository.count("id", id) == 1L

    fun refExists(ref: String): Boolean = requirementRepository.count("ref", ref) == 1L


    fun deleteRequirement(id: Long) = requirementRepository.deleteById(id)

    fun updateRequirement(id: Long, requirement: Requirement) {
       // requirementRepository.update(
          //  "comment = ?1, version = ?2, bankid = ?3, date = ?4 where id= ?5",
            // requirement.comment, requirement.version, requirement.bankId, requirement.date, id
        //)

    }

}