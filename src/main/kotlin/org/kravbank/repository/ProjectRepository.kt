package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Project
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.PROJECT_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.PROJECT_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.PROJECT_NOTFOUND
import java.time.LocalDateTime
import java.util.*
import javax.enterprise.context.ApplicationScoped
import kotlin.streams.toList

@ApplicationScoped
class ProjectRepository : PanacheRepository<Project> {
    @Throws(BackendException::class)
    fun findByRef(ref: String): Project {
        val project = find("ref", ref).firstResult<Project>()
        if (project?.deletedDate == null) {
            return project
        } else throw NotFoundException(PROJECT_NOTFOUND)
    }

    fun listAllProjects(): List<Project> {
        return findAll()
            .stream<Project>()
            .filter { p -> p.deletedDate == null }
            .toList()
    }

    @Throws(BackendException::class)
    fun createProject(project: Project) {
        persistAndFlush(project)
        if (!project.isPersistent) throw BadRequestException(PROJECT_BADREQUEST_CREATE)
    }

    fun deleteProject(id: Long): Boolean {
        val deletedDate = LocalDateTime.now()
        //soft delete
        val updated = update("deleteddate = ?1 where id = ?2", deletedDate, id)
        if (updated > 0) return true
        return false
    }

    @Throws(BackendException::class)
    fun updateProject(id: Long, project: Project) {
        val updated = update(
            "title = ?1, description = ?2 where id = ?3",
            project.title,
            project.description,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException(PROJECT_BADREQUEST_UPDATE) }
    }
}