package org.kravbank.service

import org.kravbank.domain.Codelist
import org.kravbank.repository.CodelistRepository
import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.form.codelist.CodelistFormUpdate
import org.kravbank.utils.mapper.codelist.CodelistMapper
import org.kravbank.utils.mapper.codelist.CodelistUpdateMapper
import java.lang.IllegalArgumentException
import java.net.URI
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response


@ApplicationScoped
class CodelistService(val codelistRepository: CodelistRepository, val projectService: ProjectService) {
    fun getCodelistByRefFromService(
        projectRef: String,
        codelistRef: String,
    ): Response {
        try {
            //hvis prosjektet eksisterer finn kodeliste i angitt prosjekt
            if (projectService.refExists(projectRef) && refExists(codelistRef)) {
                //funnet prosjekt
                val foundProject = projectService.getProjectByRefCustomRepo(projectRef)!!
                // finner kodeliste i funnet prosjekt
                val foundCodelist = foundProject.codelist.find { codelist ->
                    codelist.ref == codelistRef
                }
                //mapper fra entity som returneres med response
                val mappedCodelistFromEntity = CodelistMapper().fromEntity(foundCodelist!!)
                return Response.ok(mappedCodelistFromEntity).build()
            } else return Response.status(Response.Status.NOT_FOUND).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("GET one codelist failed")
        }
    }

    fun listCodelistFromService(projectRef: String): Response {
        return try {
            if (projectService.refExists(projectRef)) {
                val projectCodelist = projectService.getProjectByRefCustomRepo(projectRef)!!.codelist
                // ny instans av CodelistForm som skal returneres med Response
                val mappedListOfCodelistForm = ArrayList<CodelistForm>()
                // map fra entity til codelist form
                for (codelist in projectCodelist) mappedListOfCodelistForm.add(CodelistMapper().fromEntity(codelist))
                Response.ok(mappedListOfCodelistForm).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            //Response.status(Response.Status.BAD_REQUEST).build()
            throw IllegalArgumentException("GET all codelists failed")
        }

    }

    fun createCodelistFromService(projectref: String, newCodelist: CodelistForm): Response {
        //lager codeliste tilhørende prosjektet
        // legger til i prosjekt med kobling til prosjektet ved opprettelse
        try {
            val mappedCodelistToEntity = CodelistMapper().toEntity(newCodelist)
            if (projectService.refExists(projectref)) {
                val project = projectService.getProjectByRefCustomRepo(projectref)!!
                //codelistService.createCodelist(codelist) //codelist.persist
                // legger til kodelisten i prosjekt
                project.codelist.add(mappedCodelistToEntity)
                //oppdaterer prosjekt med oppdatert prosjekt
                projectService.updateProject(project.id, project)
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
            return if (mappedCodelistToEntity.isPersistent) {
                Response.created(URI.create("/api/v1/projects/$projectref/codelists" + newCodelist.ref)).build();
            } else {
                Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("POST codelist failed")
        }
    }

    fun deleteCodelistFromService(
        projectRef: String,
        codelistRef: String,
    ): Response {
        return try {
            if (projectService.refExists(projectRef) &&
                refExists(codelistRef)
            ) {
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                //  val codelist = codelistService.getCodelistByRef(ref)!!
                val foundCodelist = project.codelist.find { codelist -> codelist.ref == codelistRef }
                //val deleted = deleteCodelist(foundCodelist!!.id)
                //if (deleted) {
                if (foundCodelist != null) {
                    project.codelist.remove(foundCodelist)
                    deleteCodelist(foundCodelist.id)
                    //projectService.updateProject(project.id, project)
                    Response.noContent().build()
                } else Response.status(Response.Status.BAD_REQUEST).build()
            } else Response.status(Response.Status.NOT_FOUND).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("Delete codelist failed")
        }
    }

    fun updateCodelistFromService(
        projectRef: String,
        codelistRef: String,
        updateCodelist: CodelistFormUpdate
    ): Response {
        return try {
            if (projectService.refExists(projectRef) &&
                refExists(codelistRef)
            ) {
                val foundProject = projectService.getProjectByRefCustomRepo(projectRef)!!
                //val foundCodelist = getCodelistByRefCustomRepo(codelistRef)!!
                val mappedCodelistToEntity = CodelistUpdateMapper().toEntity(updateCodelist)
                //val codelistExistInProject = foundCodelist.project.ref.compareTo(projectRef)
                val foundCodelist = foundProject.codelist.find { codelist -> codelist.ref == codelistRef }
                if (foundCodelist != null) {
                    updateCodelist(foundCodelist.id, mappedCodelistToEntity)
                    Response.ok(updateCodelist).build()
                } else {
                    Response.status(Response.Status.BAD_REQUEST).build()
                }
            } else Response.status(Response.Status.NOT_FOUND).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("Put codelist failed")
        }
    }

    fun listCodelists(): MutableList<Codelist> = codelistRepository.listAll()

    fun listCodelistsByRef(ref: String): MutableList<Codelist> = codelistRepository.listAllRefs(ref)

    //fun listCodelistsByProjectId(id: Long) : MutableList<Codelist>? = codelistRepository.listAllByProjectId(id)

    fun getCodelist(id: Long): Codelist = codelistRepository.findById(id)

    fun getCodelistByRef(ref: String): Codelist? {
        return listCodelists().find { codelist ->
            codelist.ref == ref
        }
    }

    fun getCodelistByRefCustomRepo(ref: String): Codelist? = codelistRepository.findByRef(ref)


    fun createCodelist(codelist: Codelist) = codelistRepository.persistAndFlush(codelist)

    fun exists(id: Long?): Boolean = codelistRepository.count("id", id) == 1L

    fun refExists(ref: String): Boolean = codelistRepository.count("ref", ref) == 1L

    fun deleteCodelist(id: Long) = codelistRepository.deleteById(id)

    fun updateCodelist(id: Long, updateCodelist: Codelist) {
        codelistRepository.update(
            "title = ?1, description = ?2 where id= ?3",
            updateCodelist.title,
            updateCodelist.description,
            id
        )
    }
}