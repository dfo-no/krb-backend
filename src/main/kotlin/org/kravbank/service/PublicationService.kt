package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.domain.Publication
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
    val projectRepository: ProjectRepository
) {
    //@CacheResult(cacheName = "publication-cache-get")
    fun get(projectRef: String, publicationRef: String): Publication {
        val project = projectRepository.findByRef(projectRef)
        return publicationRepository.findByRef(project.id,publicationRef)
    }

    //@CacheResult(cacheName = "publication-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): MutableList<Publication> {
        val foundProject = projectRepository.findByRef(projectRef)
        return publicationRepository.listAllPublications(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, publicationForm: PublicationForm): Publication {
        val project = projectRepository.findByRef(projectRef)
        publicationForm.project = project
        val publication = PublicationMapper().toEntity(publicationForm)
        publicationRepository.createPublication(publication)
        return publication
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, publicationRef: String): Publication {
        val foundProject = projectRepository.findByRef(projectRef)
        return publicationRepository.deletePublication(foundProject.id, publicationRef)
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, publicationRef: String, publicationForm: PublicationFormUpdate): Publication {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundPublication = publicationRepository.findByRef(foundProject.id, publicationRef)
        val publication = PublicationUpdateMapper().toEntity(publicationForm)
        publicationRepository.updatePublication(foundPublication.id, publication)
        return publication
    }
}