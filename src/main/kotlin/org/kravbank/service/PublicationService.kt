package org.kravbank.service

import org.kravbank.domain.Publication
import org.kravbank.form.publication.PublicationFormUpdate
import org.kravbank.form.publication.PublicationForm
import org.kravbank.repository.PublicationRepository
import org.kravbank.utils.publication.PublicationMapper
import org.kravbank.utils.publication.PublicationUpdateMapper
import java.lang.IllegalArgumentException
import java.net.URI
import java.util.ArrayList
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class PublicationService(val publicationRepository: PublicationRepository, val projectService: ProjectService) {
    fun getPublicationByRefFromService(projectRef: String, publicationRef: String): Response {
        if (projectService.refExists(projectRef) && refExists(publicationRef)) {
            val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            val publication = project.publications.find { pub -> pub.ref == publicationRef }
            val publicationMapper = PublicationMapper().fromEntity(publication!!)
            return Response.ok(publicationMapper).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    fun listPublicationsFromService(projectRef: String): Response {
        return try {
            if (projectService.refExists(projectRef)) {
                //list proudcts by project ref
                val projectPublicationsList = projectService.getProjectByRefCustomRepo(projectRef)!!.publications
                //convert to array of form
                val publicationsFormList = ArrayList<PublicationForm>()
                for (p in projectPublicationsList) publicationsFormList.add(PublicationMapper().fromEntity(p))
                //returns the custom publication form
                Response.ok(publicationsFormList).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("GET list of publications failed")
        }
    }

    fun createPublicationFromService(projectRef: String, publication: PublicationForm): Response {
        //adds a publication to relevant project
        try {
            val publicationMapper = PublicationMapper().toEntity(publication)
            if (projectService.refExists(projectRef)) {
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                project.publications.add(publicationMapper)
                projectService.updateProject(project.id, project)
                /** {{{{ Fix }}}} **/
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
            return if (publicationMapper.isPersistent) {
                Response.created(URI.create("/api/v1/projects/$projectRef/publications/" + publication.ref)).build();
                /** ENDRE **/
            } else {
                Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            //return Response.status(Response.Status.BAD_REQUEST).build()
            throw IllegalArgumentException("POST publication failed")
        }
    }

    fun deletePublicationFromService(projectRef: String, publicationRef: String): Response {
        return try {
            //val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            //val publication = getPublicationByRefFromService(projectRef, publicationRef)

            if (projectService.refExists(projectRef) && refExists(publicationRef)) {
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                val publication = project.publications.find { publication -> publication.ref == publicationRef }
                val deleted = publicationRepository.deleteById(publication!!.id)
                if (deleted) {
                    project.publications.remove(publication)
                    Response.noContent().build()
                } else {
                    Response.status(Response.Status.BAD_REQUEST).build()
                }
            } else
                Response.status(Response.Status.NOT_FOUND).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("DELETE publication failed!")
        }
    }

    fun updatePublicationFromService(
        projectRef: String,
        publicationRef: String,
        publication: PublicationFormUpdate
    ): Response {
        if (projectService.refExists(projectRef) && refExists(publicationRef)) {
            // if publication exists in this project
            val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            //val foundProduct = getProductByRefCustomRepo(publicationRef)
            val publicationInProject = project.publications.find { pub -> pub.ref == publicationRef }
            val publicationMapper = PublicationUpdateMapper().toEntity(publication)

            //if (publication.project.ref == project.ref)

            return if (publicationInProject != null) {
                publicationRepository.update(
                    "comment = ?1, deleteddate = ?2 where id= ?3",
                    publicationMapper.comment,
                    publicationMapper.deletedDate,
                    publicationInProject.id
                )
                Response.ok(publication).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } else {

            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    fun listPublications(): List<Publication> = publicationRepository.listAll()

    fun getPublication(id: Long): Publication = publicationRepository.findById(id)

    fun createPublication(publication: Publication) = publicationRepository.persistAndFlush(publication)

    fun exists(id: Long): Boolean = publicationRepository.count("id", id) == 1L
    fun refExists(ref: String): Boolean = publicationRepository.count("ref", ref) == 1L

    fun deletePublication(id: Long) = publicationRepository.deleteById(id)

}