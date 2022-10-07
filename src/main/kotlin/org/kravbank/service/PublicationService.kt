package org.kravbank.service

import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationRepository
import org.kravbank.utils.form.publication.PublicationForm
import org.kravbank.utils.form.publication.PublicationFormUpdate
import org.kravbank.utils.mapper.publication.PublicationMapper
import org.kravbank.utils.mapper.publication.PublicationUpdateMapper
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class PublicationService(val publicationRepository: PublicationRepository, val projectService: ProjectService, val projectRepository: ProjectRepository) {
    @Throws(BackendException::class)
    fun list(projectRef: String): Response {
        //list publication by project ref
        val foundProjectPublications = projectRepository.findByRef(projectRef).publications
        //convert to array of form
        val publicationList = ArrayList<PublicationForm>()
        for (publication in foundProjectPublications) publicationList.add(PublicationMapper().fromEntity(publication))
        //returns the custom publication form
        return Response.ok(publicationList).build()
    }
    @Throws(BackendException::class)
    fun get(projectRef: String, publicationRef: String): Response {
        val foundProjectPublication = projectRepository.findByRef(projectRef).publications.find { publication ->
            publication.ref == publicationRef
        }
        Optional.ofNullable(foundProjectPublication)
            .orElseThrow { NotFoundException("Publication not found by ref $publicationRef in project by ref $projectRef") }
        val publicationMapper = PublicationMapper().fromEntity(foundProjectPublication!!)
        return Response.ok(publicationMapper).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, publication: PublicationForm): Response {
        val publicationMapper = PublicationMapper().toEntity(publication)
        val project = projectRepository.findByRef(projectRef)
        project.publications.add(publicationMapper)
        projectService.updateProject(project.id, project)
        if (publicationMapper.isPersistent)
            return Response.created(URI.create("/api/v1/projects/$projectRef/publications/" + publication.ref)).build()
        else throw BadRequestException("Bad request! Did not create publication")
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, publicationRef: String): Response {
        val foundPublications = projectRepository.findByRef(projectRef).publications.find { publication ->
            publication.ref == publicationRef
        }
        Optional.ofNullable(foundPublications)
            .orElseThrow { NotFoundException("Publications not found by ref $publicationRef in project by ref $projectRef") }
        foundPublications!!.delete()
        return Response.noContent().build()
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, publicationRef: String, publication: PublicationFormUpdate): Response {
        val foundPublications = projectRepository.findByRef(projectRef).publications.find { pub ->
            pub.ref == publicationRef
        }
        Optional.ofNullable(foundPublications)
            .orElseThrow { NotFoundException("Publications not found by ref $publicationRef in project by ref $projectRef") }
        val publicationMapper = PublicationUpdateMapper().toEntity(publication)
        publicationRepository.update(
            "comment = ?1, deleteddate = ?2 where id= ?3",
            publicationMapper.comment,
            publicationMapper.deletedDate,
            foundPublications!!.id
        )
        return Response.ok(publication).build()
    }
}