package org.kravbank.service

import org.kravbank.dao.ProjectForm
import org.kravbank.domain.Project
import org.kravbank.lang.BackendException
import org.kravbank.repository.ProjectRepository
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class ProjectService(val projectRepository: ProjectRepository) {

    @Throws(BackendException::class)
    fun get(projectRef: String): Project {
        return projectRepository.findByRef(projectRef)
    }

    fun list(): List<Project> {
        return projectRepository.listAllProjects()
    }

    @Throws(BackendException::class)
    fun create(newProject: ProjectForm): Project {
        val project = ProjectForm().toEntity(newProject)
        projectRepository.createProject(project)
        return project
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String): Boolean {
        val foundProject = projectRepository.findByRef(projectRef)
        /* //TODO denne bør oppdateres fra native query i stedenfor
        foundProject.deletedDate = LocalDateTime.now()
        projectRepository.updateProject(foundProject.id, foundProject)
         */
        return projectRepository.deleteById(foundProject.id)
        /*if (deleted) return foundProject
        throw BadRequestException("Bad request! Did not delete project")
         */
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, updatedProject: ProjectForm): Project {
        val foundProject = projectRepository.findByRef(projectRef)
        val update = ProjectForm().toEntity(updatedProject)
        projectRepository.updateProject(foundProject.id, update)

        return update.apply { ref = foundProject.ref }
    }
}