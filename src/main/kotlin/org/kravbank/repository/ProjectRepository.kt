package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Project
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import java.time.LocalDateTime
import java.util.Optional
import java.util.stream.Stream
import javax.enterprise.context.ApplicationScoped
import kotlin.streams.toList

@ApplicationScoped
class ProjectRepository : PanacheRepository<Project> {
    @Throws(BackendException::class)
    fun findByRef(ref: String): Project {
        val project = find("ref", ref).firstResult<Project>()
        if (project?.deletedDate == null) {
            return project
        } else throw NotFoundException("Project not found")
    }

    //@Throws(BackendException::class)
    fun listAllProjects(): List<Project> {

       return findAll().stream<Project?>().filter {p -> p.deletedDate == null }.toList()
                // findAll().list<Project>().filter { }
    }

    @Throws(BackendException::class)
    fun createProject(project: Project) {
        persistAndFlush(project)
        if (!project.isPersistent) throw BadRequestException("Bad request! Project was not created")
    }

    //@Throws(BackendException::class)
    fun deleteProject(id: Long) {
        val deletedDate = LocalDateTime.now()
        //soft delete
        update("deleteddate = ?1 where id = ?2", deletedDate, id)

        //true
        //false

    }

    @Throws(BackendException::class)
    fun updateProject(id: Long, project: Project) {
        val updated = update(
            "title = ?1, description = ?2 where id = ?3", //publisheddate = ?3, version = ?4 where id = ?5",
            project.title,
            project.description,
           // project.publishedDate,
            // project.version,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException("Bad request! Project did not update") }
    }
}