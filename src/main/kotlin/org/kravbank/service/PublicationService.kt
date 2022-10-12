package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationRepository
import org.kravbank.utils.form.need.NeedForm
import org.kravbank.utils.form.publication.PublicationForm
import org.kravbank.utils.form.publication.PublicationFormUpdate
import org.kravbank.utils.mapper.need.NeedMapper
import org.kravbank.utils.mapper.product.ProductMapper
import org.kravbank.utils.mapper.publication.PublicationMapper
import org.kravbank.utils.mapper.publication.PublicationUpdateMapper
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class PublicationService(
    val publicationRepository: PublicationRepository,
    val projectService: ProjectService,
    val projectRepository: ProjectRepository
) {
    //@CacheResult(cacheName = "publication-cache-get")
    fun get(projectRef: String, publicationRef: String): Response {
            val project = projectRepository.findByRef(projectRef)
            val foundPublication = publicationRepository.findByRef(project.id, publicationRef)
            val publicationForm = PublicationMapper().fromEntity(foundPublication)
            return Response.ok(publicationForm).build()
    }

    //@CacheResult(cacheName = "publication-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundPublications = publicationRepository.listAllPublications(foundProject.id)
        val publicationsForm = ArrayList<PublicationForm>()
        for (n in foundPublications) publicationsForm.add(PublicationMapper().fromEntity(n))
        return Response.ok(publicationsForm).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, publicationForm: PublicationForm): Response {
        val project = projectRepository.findByRef(projectRef)
        publicationForm.project = project
        val publication = PublicationMapper().toEntity(publicationForm)
        publicationRepository.createPublication(publication)
        return Response.created(URI.create("/api/v1/projects/$projectRef/publications/" + publication.ref)).build()
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, publicationRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundPublications = foundProject.publications.find { publication -> publication.ref == publicationRef }
        Optional.ofNullable(foundPublications)
            .orElseThrow { NotFoundException("Publications not found by ref $publicationRef in project by ref $projectRef") }
        val deleted = foundProject.publications.remove(foundPublications)
        if (deleted) {
            projectService.updateProject(foundProject.id, foundProject)
            return Response.noContent().build()
        } else throw BadRequestException("Bad request! Publication not deleted")
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