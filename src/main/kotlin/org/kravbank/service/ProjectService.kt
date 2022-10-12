package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.domain.Project
import org.kravbank.exception.BackendException
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.project.ProjectForm
import org.kravbank.utils.form.project.ProjectFormUpdate
import org.kravbank.utils.mapper.project.ProjectMapper
import org.kravbank.utils.mapper.project.ProjectUpdateMapper
import java.net.URI
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response


@ApplicationScoped
class ProjectService(val projectRepository: ProjectRepository) {

    // @CacheResult(cacheName = "project-cache-get")
    @Throws(BackendException::class)
    fun get(projcetRef: String): Response {
        val project = projectRepository.findByRef(projcetRef)
        val projectMapped = ProjectMapper().fromEntity(project)
        return Response.ok(projectMapped).build()
    }


    //    @CacheResult(cacheName = "project-cache-list")
    @Throws(BackendException::class)
    fun list(): Response {
        val projectFormList = ArrayList<ProjectForm>()
        val projects = projectRepository.listAllProjects()
        for (p in projects) projectFormList.add(ProjectMapper().fromEntity(p))
        return Response.ok(projectFormList).build()
    }

    @Throws(BackendException::class)
    fun create(newProject: ProjectForm): Response {
        val mappedProjectToEntity = ProjectMapper().toEntity(newProject)
        projectRepository.createProject(mappedProjectToEntity)
        return Response.created(URI.create("/projects/" + newProject.ref)).build();
    }

    fun delete(projcetRef: String): Response {
        projectRepository.deleteProject(projcetRef)
        return Response.noContent().build()
    }

    fun update(projcetRef: String, project: ProjectFormUpdate): Response {
        val projectMapper = ProjectUpdateMapper().toEntity(project)
        projectRepository.updateProject(projcetRef, projectMapper)
        return Response.ok(project).build()
    }

}