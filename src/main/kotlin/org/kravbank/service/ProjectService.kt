package org.kravbank.service

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

    @Throws(BackendException::class)
    fun get(projcetRef: String): Response {

        val project = ProjectMapper().fromEntity(projectRepository.findByRef(projcetRef))

        return Response.ok(project).build()
    }

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
        return Response.created(URI.create("/projects" + newProject.ref)).build();
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

    fun getProjectByRefCustomRepo(ref: String): Project? = projectRepository.findByRef(ref)

    fun exists(id: Long): Boolean = projectRepository.count("id", id) == 1L

    fun refExists(ref: String): Boolean = projectRepository.count("ref", ref) == 1L

    fun updateProject(id: Long, project: Project) {
        projectRepository.update(
            "title = ?1, description= ?2 where id = ?3",
            project.title,
            project.description,
            id
        )
    }
}