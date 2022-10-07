package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import org.kravbank.utils.mapper.project.ProjectMapper
import java.util.Optional
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProjectRepository : PanacheRepository<Project> { //project, long

    //fun findByTitle(name: String) = find("name", name).firstResult()

    @Throws(BackendException::class)
    fun findByRef(ref: String): Project {
        val found = find("ref", ref).firstResult<Project>()
        return Optional.ofNullable(found).orElseThrow { NotFoundException("Project not found by ref: $ref") }
    }

    @Throws(BackendException::class)
    fun listAllProjects(): MutableList<Project> {
        val all = findAll().list<Project>()
        if (all.isNotEmpty()) return all else throw NotFoundException("No projects found")
    }

    @Throws(BackendException::class)
    fun createProject(project: Project) {
        persistAndFlush(project)
        if (!project.isPersistent) throw BadRequestException("Fail! Project not created")
    }

    @Throws(BackendException::class)
    fun deleteProject(projectRef: String) {
        val deleted: Boolean
        val found = findByRef(projectRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException("Fail! Project not deleted")
    }

    @Throws(BackendException::class)
    fun updateProject(projectRef: String, project: Project) {
        val foundProject = findByRef(projectRef)
        val updated = update(
            "title = ?1, description= ?2 where id = ?3",
            project.title,
            project.description,
            foundProject.id
        )
        Optional.of(updated).orElseThrow { BadRequestException("Fail! Project did not update") }
    }
}