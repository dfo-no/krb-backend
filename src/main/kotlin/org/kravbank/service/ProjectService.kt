package org.kravbank.service

import org.kravbank.dao.ProjectForm
import org.kravbank.domain.Project
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.Messages.RepoErrorMsg.PROJECT_BADREQUEST_CREATE
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class ProjectService(val projectRepository: ProjectRepository) {

    fun get(projectRef: String): Project {
        return projectRepository.findByRef(projectRef)
    }

    fun list(): List<Project> {
        return projectRepository.listAllProjects()
    }

    @Throws(BackendException::class)
    fun create(newProject: ProjectForm): Project {
        val project = ProjectForm().toEntity(newProject)

        projectRepository.persistAndFlush(project)
        if (!projectRepository.isPersistent(project)) throw BadRequestException(PROJECT_BADREQUEST_CREATE)

        return project
    }

    fun delete(projectRef: String): Project {
        val foundProject = projectRepository.findByRef(projectRef)

        projectRepository.deleteById(foundProject.id)

        return foundProject
    }

    fun update(projectRef: String, updatedProject: ProjectForm): Project {
        val foundProject = projectRepository.findByRef(projectRef)

        val update = ProjectForm().toEntity(updatedProject)

        projectRepository.updateProject(foundProject.id, update)

        return update.apply { ref = update.ref }
    }
}