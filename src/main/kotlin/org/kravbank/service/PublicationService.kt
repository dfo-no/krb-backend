package org.kravbank.service

import org.kravbank.domain.Publication
import org.kravbank.repository.PublicationRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PublicationService(val publicationRepository: PublicationRepository) {
    fun listPublications(): List<Publication> = publicationRepository.listAll()

    fun getPublication(id: Long): Publication = publicationRepository.findById(id)

    fun createPublication(publication: Publication) = publicationRepository.persist(publication)

    fun exists(id: Long): Boolean = publicationRepository.count("id", id) == 1L

    fun deletePublication(id: Long) = publicationRepository.deleteById(id)

    fun updatePublication(id: Long, publication: Publication) {
         publicationRepository.update("comment = ?1, version = ?2, bankid = ?3, date = ?4 where id= ?5",
                publication.comment, publication.version, publication.bankId, publication.date, id)

    }
}