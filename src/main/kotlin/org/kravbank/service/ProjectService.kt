package org.kravbank.service

import org.kravbank.domain.Project
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.project.ProjectForm
import org.kravbank.utils.form.project.ProjectFormUpdate
import org.kravbank.utils.mapper.project.ProjectMapper
import org.kravbank.utils.mapper.project.ProjectUpdateMapper
import java.lang.IllegalArgumentException
import java.net.URI
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class ProjectService(val projectRepository: ProjectRepository) {
    fun getProjcetFromService(projcetRef: String): Response {
        return try {
            if (refExists(projcetRef)) {
                val project = getProjectByRefCustomRepo(projcetRef)
                val mappedProjectToEntity = ProjectMapper().fromEntity(project!!)
                Response.ok(mappedProjectToEntity).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("GET one project failed")
        }
    }

    fun listProjectsFromService(): Response {
        try {
            val projectFormList = ArrayList<ProjectForm>()
            for (p in listProjects()) projectFormList.add(ProjectMapper().fromEntity(p))
            return Response.ok(projectFormList).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("GET all projects failed")
        }
    }

    fun createProjectFromService(newProject: ProjectForm): Response {
        try {
            val mappedProjectToEntity = ProjectMapper().toEntity(newProject)
            createProject(mappedProjectToEntity)
            return if (mappedProjectToEntity.isPersistent) {
                Response.created(URI.create("/projects" + newProject.ref)).build();
            } else {
                Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("POST project failed")
        }
    }

    fun deleteProjectFromService(projcetRef: String): Response{
        try {
            return if (refExists(projcetRef)) {
                val project = getProjectByRefCustomRepo(projcetRef)
                val deleted = deleteProject(project!!.id)
                if (deleted) {
                    Response.noContent().build()
                } else Response.status(Response.Status.BAD_REQUEST).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Delete project FAILED. Message: $e")
        }
    }

    fun updateProjectFromService(projcetRef: String, project: ProjectFormUpdate): Response {
        try {
            return if (refExists(projcetRef)) {
                val projectMapper = ProjectUpdateMapper().toEntity(project)
                val foundProject = getProjectByRefCustomRepo(projcetRef)
                updateProject(foundProject!!.id, projectMapper)
                Response.ok(project).build()
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Delete project failed")
        }
    }

    fun listProjects(): List<Project> = projectRepository.listAll()

    fun getProject(id: Long): Project = projectRepository.findById(id)

    fun getProjectByRef(ref: String): Project? {
        return listProjects().find { project ->
            project.ref == ref
        }
    }

    fun listProjectsByRef(ref: String): List<Project> = projectRepository.listAllByProjectRef(ref)

    fun getProjectByRefCustomRepo(ref: String): Project? = projectRepository.findByRef(ref)

    fun createProject(project: Project) = projectRepository.persist(project)

    fun exists(id: Long): Boolean = projectRepository.count("id", id) == 1L

    fun refExists(ref: String): Boolean = projectRepository.count("ref", ref) == 1L

    fun deleteProject(id: Long) = projectRepository.deleteById(id)


    fun updateProject(id: Long, project: Project) {
        projectRepository.update(
            "title = ?1, description= ?2 where id = ?3",
            project.title,
            project.description,
            id
        )
    }
}