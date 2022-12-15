package org.kravbank.service

import org.kravbank.dao.PublicationForm
import org.kravbank.domain.Publication
import org.kravbank.lang.BackendException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationRepository
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PublicationService(
    val publicationRepository: PublicationRepository,
    val projectRepository: ProjectRepository
) {
    fun get(projectRef: String, publicationRef: String): Publication {
        val foundProject = projectRepository.findByRef(projectRef)

        return publicationRepository.findByRef(foundProject.id, publicationRef)
    }

    @Throws(BackendException::class)
    fun list(projectRef: String): List<Publication> {
        val foundProject = projectRepository.findByRef(projectRef)

        return publicationRepository.listAllPublications(foundProject.id)
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, newPublication: PublicationForm): Publication {
        val foundProject = projectRepository.findByRef(projectRef)
        val publication = PublicationForm().toEntity(newPublication)
        publication.project = foundProject
        publication.date = LocalDateTime.now()
        publicationRepository.createPublication(publication)

        return publication
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, publicationRef: String): Publication {
        val foundProject = projectRepository.findByRef(projectRef)
        val publication = publicationRepository.findByRef(foundProject.id, publicationRef)
        publicationRepository.delete(publication)

        return publication
        // throw BadRequestException("Bad request! Did not delete publication") TODO: Put somewhere else further up in chain
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, publicationRef: String, updatedPublication: PublicationForm): Publication {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundPublication = publicationRepository.findByRef(foundProject.id, publicationRef)
        val update = PublicationForm().toEntity(updatedPublication)
        publicationRepository.updatePublication(foundPublication.id, update)

        return update.apply { ref = foundPublication.ref }
    }
}
