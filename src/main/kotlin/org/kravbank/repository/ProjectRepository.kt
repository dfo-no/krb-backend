package org.kravbank.repository

import org.kravbank.domain.Project
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.PROJECT_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.PROJECT_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.PROJECT_NOTFOUND
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProjectRepository : BackendRepository<Project>() {
    @Throws(BackendException::class)
    fun findByRef(ref: String): Project {
        val project = find("ref", ref).firstResult<Project>()
        if (project != null) {
            return project
        } else throw NotFoundException(PROJECT_NOTFOUND)
    }

    fun listAllProjects(): List<Project> {
        return findAll()
            .stream<Project>()
            .toList()
    }

    @Throws(BackendException::class)
    fun createProject(project: Project) {
        persistAndFlush(project)
        if (!project.isPersistent) throw BadRequestException(PROJECT_BADREQUEST_CREATE)
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