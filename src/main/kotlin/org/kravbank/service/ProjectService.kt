package org.kravbank.service

import org.kravbank.domain.ProjectKtl
import org.kravbank.java.model.Project
import org.kravbank.repository.ProjectRepository
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class ProjectService(val projectRepository: ProjectRepository) {
    fun listProjects(): List<ProjectKtl> = projectRepository.listAll()
    fun getProject(id: Long): ProjectKtl = projectRepository.findById(id)
    fun createProject(projectKtl: ProjectKtl) = projectRepository.persist(projectKtl)
    fun exists(id: Long): Boolean = projectRepository.count("id", id) == 1L

    fun deleteProject(id: Long) = projectRepository.deleteById(id)


}