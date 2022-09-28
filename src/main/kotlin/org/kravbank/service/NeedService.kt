package org.kravbank.service

import org.kravbank.domain.Need
import org.kravbank.utils.form.need.NeedForm
import org.kravbank.utils.form.need.NeedFormUpdate
import org.kravbank.repository.NeedRepository
import org.kravbank.utils.mapper.need.NeedMapper
import org.kravbank.utils.mapper.need.NeedUpdateMapper
import java.lang.IllegalArgumentException
import java.net.URI
import java.util.ArrayList
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class NeedService(val needRepository: NeedRepository, val projectService: ProjectService) {
    fun getNeedByRefFromService(projectRef: String, needRef: String): Response {
        if (projectService.refExists(projectRef) && refExists(needRef)) {
            val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            val need = project.needs.find { need -> need.ref == needRef }
            val needMapper = org.kravbank.utils.mapper.need.NeedMapper().fromEntity(need!!)
            return Response.ok(needMapper).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    fun listNeedsFromService(projectRef: String): Response {
        return try {
            if (projectService.refExists(projectRef)) {
                //list needs by project ref
                val projectNeedsList = projectService.getProjectByRefCustomRepo(projectRef)!!.needs
                //convert to array of form
                val needsFormList = ArrayList<NeedForm>()
                for (n in projectNeedsList) needsFormList.add(org.kravbank.utils.mapper.need.NeedMapper().fromEntity(n))
                //returns the custom need form
                Response.ok(needsFormList).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("GET list of needs failed")
        }
    }

    fun createNeedFromService(projectRef: String, need: NeedForm): Response {
        //adds a need to relevant project
        try {
            val needMapper = org.kravbank.utils.mapper.need.NeedMapper().toEntity(need)
            if (projectService.refExists(projectRef)) {
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                project.needs.add(needMapper)
                projectService.updateProject(project.id, project)
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
            return if (needMapper.isPersistent) {
                Response.created(URI.create("/api/v1/projects/$projectRef/needs/" + need.ref)).build();
            } else {
                Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("POST need failed")
        }
    }

    fun deleteNeedFromService(projectRef: String, needRef: String): Response {
        return try {
            //val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            //val need = getNeedByRefFromService(projectRef, needRef)

            if (projectService.refExists(projectRef) && refExists(needRef)) {
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                val need = project.needs.find { need -> need.ref == needRef }
                val deleted = deleteNeed(need!!.id)
                if (deleted) {
                    project.needs.remove(need)
                    Response.noContent().build()
                } else {
                    Response.status(Response.Status.BAD_REQUEST).build()
                }
            } else
                Response.status(Response.Status.NOT_FOUND).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("DELETE need failed!")
        }
    }

    fun updateNeedFromService(
        projectRef: String,
        needRef: String,
        need: NeedFormUpdate
    ): Response {
        if (projectService.refExists(projectRef) && refExists(needRef)) {
            // if need exists in this project
            val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            //val foundProduct = getProductByRefCustomRepo(needRef)
            val needInProject = project.needs.find { pub -> pub.ref == needRef }
            val needMapper = org.kravbank.utils.mapper.need.NeedUpdateMapper().toEntity(need)

            //if (need.project.ref == project.ref)

            return if (needInProject != null) {
                needRepository.update(
                    "title = ?1, description = ?2 where id= ?3",
                    needMapper.title,
                    needMapper.description,
                    needInProject.id
                )
                Response.ok(need).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } else {

            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }



    fun listNeeds(): MutableList<Need> = needRepository.listAll()
    fun getNeed(id: Long): Need = needRepository.findById(id)
    fun createNeed(need: Need) = needRepository.persistAndFlush(need)
    fun exists(id: Long): Boolean = needRepository.count("id", id) == 1L
    fun refExists(ref: String): Boolean = needRepository.count("ref", ref) == 1L
    fun deleteNeed(id: Long) = needRepository.deleteById(id)
    fun updateNeed(id: Long, requirementVariant: Need) {
       // needRepository.update(
          //  "comment = ?1, version = ?2, bankid = ?3, date = ?4 where id= ?5",
            // requirementVariant.comment, requirementVariant.version, requirementVariant.bankId, requirementVariant.date, id
        //)

    }

}