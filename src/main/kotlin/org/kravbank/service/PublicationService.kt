package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.exception.BackendException
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
        publicationRepository.deletePublication(foundProject.id, publicationRef)
        return Response.noContent().build()
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, publicationRef: String, publicationForm: PublicationFormUpdate): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundPublication = publicationRepository.findByRef(foundProject.id, publicationRef)
        val publication = PublicationUpdateMapper().toEntity(publicationForm)
        publicationRepository.updatePublication(foundPublication.id, publication)
        return Response.ok(publicationForm).build()
    }
}