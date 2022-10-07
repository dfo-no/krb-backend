package org.kravbank.service

import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import org.kravbank.utils.form.need.NeedForm
import org.kravbank.utils.form.need.NeedFormUpdate
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.mapper.need.NeedMapper
import org.kravbank.utils.mapper.need.NeedUpdateMapper
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class NeedService(
    val needRepository: NeedRepository,
    val projectService: ProjectService,
    val projectRepository: ProjectRepository
) {
    @Throws(BackendException::class)
    fun get(projectRef: String, needRef: String): Response {
        val project = projectRepository.findByRef(projectRef).needs.find { needs ->
            needs.ref == needRef
        }
        Optional.ofNullable(project)
            .orElseThrow { NotFoundException("Need not found by ref $needRef in project by ref $projectRef") }
        val needMapper = NeedMapper().fromEntity(project!!)
        return Response.ok(needMapper).build()
    }

    @Throws(BackendException::class)
    fun list(projectRef: String): Response {
        //list needs by project ref
        val foundProjectNeeds = projectRepository.findByRef(projectRef).needs
        //convert to array of form
        val needsFormList = ArrayList<NeedForm>()
        for (p in foundProjectNeeds) needsFormList.add(NeedMapper().fromEntity(p))
        //returns the custom need form
        return Response.ok(needsFormList).build()
    }


    @Throws(BackendException::class)
    fun create(projectRef: String, need: NeedForm): Response {
        val needMapper = NeedMapper().toEntity(need)
        val project = projectRepository.findByRef(projectRef)
        project.needs.add(needMapper)
        projectService.updateProject(project.id, project)
        if (needMapper.isPersistent)
            return Response.created(URI.create("/api/v1/projects/$projectRef/needs" + need.ref)).build()
        else throw BadRequestException("Bad request! Did not create need")
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, needRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundNeed = foundProject.needs.find { needs -> needs.ref == needRef }
        Optional.ofNullable(foundNeed)
            .orElseThrow { NotFoundException("Need not found by ref $needRef in project by ref $projectRef") }
        val deleted = foundProject.needs.remove(foundNeed)
        if (deleted) {
            projectService.updateProject(foundProject.id, foundProject)
            return Response.noContent().build()
        } else throw BadRequestException("Bad request! Need not deleted")
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, needRef: String, need: NeedFormUpdate): Response {
        val foundNeed = projectRepository.findByRef(projectRef).needs.find { needs ->
            needs.ref == needRef
        }
        Optional.ofNullable(foundNeed)
            .orElseThrow { NotFoundException("Need not found by ref $needRef in project by ref $projectRef") }
        val needMapper = NeedUpdateMapper().toEntity(need)
        needRepository.update(
            "title = ?1, description = ?2 where id= ?3",
            needMapper.title,
            needMapper.description,
            //need.deletedDate,
            foundNeed!!.id
        )
        return Response.ok(need).build()
    }
    //fun exists(id: Long): Boolean = needRepository.count("id", id) == 1L
    // fun refExists(ref: String): Boolean = needRepository.count("ref", ref) == 1L
}