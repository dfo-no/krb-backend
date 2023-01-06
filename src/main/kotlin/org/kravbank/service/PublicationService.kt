package org.kravbank.service

import org.kravbank.dao.PublicationForm
import org.kravbank.domain.Publication
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationRepository
import org.kravbank.utils.Messages.RepoErrorMsg.PUBLICATION_BADREQUEST_CREATE
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
        publication.version = getNextVersion(foundProject.publications)

        publicationRepository.persistAndFlush(publication)
        if (!publicationRepository.isPersistent(publication)) throw BadRequestException(PUBLICATION_BADREQUEST_CREATE)

        return publication
    }

    fun delete(projectRef: String, publicationRef: String): Publication {
        val foundProject = projectRepository.findByRef(projectRef)
        val publication = publicationRepository.findByRef(foundProject.id, publicationRef)

        publicationRepository.delete(publication)

        return publication
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, publicationRef: String, updatedPublication: PublicationForm): Publication {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundPublication = publicationRepository.findByRef(foundProject.id, publicationRef)

        val updated = PublicationForm().toEntity(updatedPublication)
        publicationRepository.updatePublication(foundPublication.id, updated.comment)

        updated.ref = foundPublication.ref
        updated.version = foundPublication.version

        return updated
    }

    private fun getNextVersion(publications: List<Publication>): Long {
        if (publications.isEmpty()) return 1

        return publications.maxOf { p -> p.version }.plus(1)
    }

    companion object AddToPublication {
        fun updateAndPersistPublicationExport(
            id: Long,
            publicationRepository: PublicationRepository,
            ref: String
        ) {

            val foundPublication = publicationRepository.findById(id)

            foundPublication.publicationExportRef = ref

            foundPublication.persistAndFlush()

            if (!publicationRepository.isPersistent(foundPublication)) throw BadRequestException("Something went wrong updating $foundPublication")

        }
    }
}
