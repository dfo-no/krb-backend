package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.domain.Project
import org.kravbank.lang.exception.BackendException
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.project.ProjectForm
import org.kravbank.utils.form.project.ProjectFormUpdate
import org.kravbank.utils.mapper.project.ProjectMapper
import org.kravbank.utils.mapper.project.ProjectUpdateMapper
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class ProjectService(val projectRepository: ProjectRepository) {

    // @CacheResult(cacheName = "project-cache-get")
    @Throws(BackendException::class)
    fun get(projcetRef: String): Project {
        return projectRepository.findByRef(projcetRef)
    }

    //    @CacheResult(cacheName = "project-cache-list")
    //@Throws(BackendException::class)
    fun list(): List<Project> {
        return projectRepository.listAllProjects()
    }

    @Throws(BackendException::class)
    fun create(newProject: ProjectForm): Project {
        val project = ProjectMapper().toEntity(newProject)
        projectRepository.createProject(project)
        return project
    }

    fun delete(projcetRef: String): Project {
        val foundProject = projectRepository.findByRef(projcetRef)
        projectRepository.deleteProject(foundProject.id)
        return foundProject
    }

    @Throws(BackendException::class)
    fun update(projcetRef: String, updatedProject: ProjectFormUpdate): Project {
        val foundProject = projectRepository.findByRef(projcetRef)
        val project = ProjectUpdateMapper().toEntity(updatedProject)
        projectRepository.updateProject(foundProject.id, project)
        return project
    }
}