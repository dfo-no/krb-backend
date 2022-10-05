package org.kravbank.service

import org.kravbank.domain.Project
import org.kravbank.exception.project.CustomProjectException
import org.kravbank.repository.ProjectRepository
import org.kravbank.response.project.ResponseProject
import org.kravbank.utils.form.project.ProjectForm
import org.kravbank.utils.form.project.ProjectFormUpdate
import org.kravbank.utils.mapper.project.ProjectMapper
import org.kravbank.utils.mapper.project.ProjectUpdateMapper
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class ProjectService(val projectRepository: ProjectRepository) {
    fun getProjcetFromService(projcetRef: String): Response {
        return try {
            if (refExists(projcetRef)) {
                val project = getProjectByRefCustomRepo(projcetRef)
                val mappedProjectToEntity = ProjectMapper().fromEntity(project!!)

               //Response.ok(mappedProjectToEntity).build()
               ResponseProject().ok(mappedProjectToEntity)
            } else {
                ResponseProject().not_found()
            }
        }catch (e : CustomProjectException){
           throw CustomProjectException("GET one project failed. Message: $e")
        }
    }

    fun listProjectsFromService(): Response {
       return try {
            val projectFormList = ArrayList<ProjectForm>()
            for (p in listProjects()) projectFormList.add(ProjectMapper().fromEntity(p))
            ResponseProject().ok_list(projectFormList)
        } catch (e: CustomProjectException) {
            throw CustomProjectException("GET all projects failed. Message: $e")
        }
    }

    fun createProjectFromService(newProject: ProjectForm): Response {
        try {
            val mappedProjectToEntity = ProjectMapper().toEntity(newProject)
            createProject(mappedProjectToEntity)
            return if (mappedProjectToEntity.isPersistent) {

                ResponseProject().create_ok("/projects" + newProject.ref)
                //Response.created(URI.create("/projects" + newProject.ref)).build();
            } else {
                ResponseProject().bad_req()
                //Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: CustomProjectException) {
            throw CustomProjectException("POST project failed. Message: $e")
        }
    }

    fun deleteProjectFromService(projcetRef: String): Response{
        try {
            return if (refExists(projcetRef)) {
                val project = getProjectByRefCustomRepo(projcetRef)
                val deleted = deleteProject(project!!.id)
                if (deleted) {
                    ResponseProject().delete_ok()
                } else ResponseProject().bad_req()
                    //Response.status(Response.Status.BAD_REQUEST).build()
            } else {
                ResponseProject().not_found()
                //Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: CustomProjectException) {
            throw CustomProjectException("Delete project failed. Message: $e")
        }
    }

    fun updateProjectFromService(projcetRef: String, project: ProjectFormUpdate): Response {
        try {
            return if (refExists(projcetRef)) {
                val projectMapper = ProjectUpdateMapper().toEntity(project)
                val foundProject = getProjectByRefCustomRepo(projcetRef)
                updateProject(foundProject!!.id, projectMapper)
                ResponseProject().ok_update(project)

            // Response.ok(project).build()
            } else {
                ResponseProject().not_found()
                //return Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: CustomProjectException) {
            throw CustomProjectException("Delete project failed. Message:  $e")
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