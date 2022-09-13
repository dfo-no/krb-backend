package org.kravbank.service

import org.kravbank.domain.Project
import org.kravbank.domain.Publication
import org.kravbank.repository.ProjectRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProjectService(val projectRepository: ProjectRepository) {
    fun listProjects(): List<Project> = projectRepository.listAll()

    fun getProject(id: Long): Project = projectRepository.findById(id)

    fun createProject(project: Project) = projectRepository.persistAndFlush(project)

    fun exists(id: Long): Boolean = projectRepository.count("id", id) == 1L

    fun deleteProject(id: Long) = projectRepository.deleteById(id)

    fun updateProject(id: Long, project: Project) {
       projectRepository.update("title = ?1 where id= ?2", project.title, id)

        /*
           project.description,
           project.publishedDate,
           project.projectId,
           //project.version,

            //project.products,
           //project.publications,
                  "products = ?5, " +
               "publications = ?6 " +

                          "description = ?2, " +
               "publishedDate = ?3," +
               "projectId = ?4, " +
         */
    }
}