package org.kravbank.service

import org.kravbank.dao.ProjectForm
import org.kravbank.domain.Project
import org.kravbank.lang.BackendException
import org.kravbank.repository.ProjectRepository
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class ProjectService(val projectRepository: ProjectRepository) {

    // @CacheResult(cacheName = "project-cache-get")
    @Throws(BackendException::class)
    fun get(projcetRef: String): Project {
        return projectRepository.findByRef(projcetRef)
    }

    //@CacheResult(cacheName = "project-cache-list")
    fun list(): List<Project> {
        return projectRepository.listAllProjects()
    }

    @Throws(BackendException::class)
    fun create(newProject: org.kravbank.dao.ProjectForm): Project {
        val project = org.kravbank.dao.ProjectForm().toEntity(newProject)
        projectRepository.createProject(project)
        return project
    }

    fun delete(projcetRef: String): Project {
        val foundProject = projectRepository.findByRef(projcetRef)
        projectRepository.deleteProject(foundProject.id)
        return foundProject
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, updatedProject: ProjectForm): Project {
        val foundProject = projectRepository.findByRef(projectRef)
        val update = ProjectForm().toEntity(updatedProject)
        projectRepository.updateProject(foundProject.id, update)
        return update.apply { ref = update.ref }
    }
}