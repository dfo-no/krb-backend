package org.kravbank.service

import org.kravbank.domain.Publication
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationRepository
import org.kravbank.utils.publication.dto.PublicationForm
import org.kravbank.utils.publication.dto.PublicationFormUpdate
import org.kravbank.utils.publication.mapper.PublicationMapper
import org.kravbank.utils.publication.mapper.PublicationUpdateMapper
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PublicationService(
    val publicationRepository: PublicationRepository,
    val projectRepository: ProjectRepository
) {
    //@CacheResult(cacheName = "publication-cache-get")
    fun get(projectRef: String, publicationRef: String): Publication {
        val foundProject = projectRepository.findByRef(projectRef)
        return publicationRepository.findByRef(foundProject.id,publicationRef)
    }

    //@CacheResult(cacheName = "publication-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): List<Publication> {
        val foundProject = projectRepository.findByRef(projectRef)
        return publicationRepository.listAllPublications(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, newPublication: PublicationForm): Publication {
        val foundProject = projectRepository.findByRef(projectRef)
        newPublication.project = foundProject
        val publication = PublicationMapper().toEntity(newPublication)
        publicationRepository.createPublication(publication)
        return publication
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, publicationRef: String): Publication {
        val foundProject = projectRepository.findByRef(projectRef)
        val publication = publicationRepository.findByRef(foundProject.id,publicationRef)
       publicationRepository.deletePublication(publication.id)
        return publication
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, publicationRef: String, updatedPublication: PublicationForm): Publication {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundPublication = publicationRepository.findByRef(foundProject.id, publicationRef)
        val publication = PublicationMapper().toEntity(updatedPublication)
        publicationRepository.updatePublication(foundPublication.id, publication)
        return publication
    }
}
