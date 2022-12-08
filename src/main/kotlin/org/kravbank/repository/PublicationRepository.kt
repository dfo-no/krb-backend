package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Publication
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.PUBLICATION_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.PUBLICATION_BADREQUEST_DELETE
import org.kravbank.utils.Messages.RepoErrorMsg.PUBLICATION_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.PUBLICATION_NOTFOUND
import java.time.LocalDateTime
import java.util.*
import javax.enterprise.context.ApplicationScoped
import kotlin.streams.toList

@ApplicationScoped
class PublicationRepository : PanacheRepository<Publication> {

    @Throws(BackendException::class)
    fun findByRef(projectId: Long, ref: String): Publication {
        val publication =
            find(
                "ref = ?1 and project_id_fk = ?2",
                ref,
                projectId
            )
                .firstResult<Publication>()

        if (publication?.deletedDate == null) {
            return publication

        } else throw NotFoundException(PUBLICATION_NOTFOUND)
    }

    fun listAllPublications(id: Long): List<Publication> {
        return find("project_id_fk", id)
            .stream<Publication>()
            .filter { p -> p.deletedDate == null }
            .toList()
    }

    @Throws(BackendException::class)
    fun createPublication(publication: Publication) {
        persistAndFlush(publication)

        if (!publication.isPersistent) {

            throw BadRequestException(PUBLICATION_BADREQUEST_CREATE)
        }
    }

    @Throws(BackendException::class)
    fun deletePublication(projectId: Long, publicationRef: String): Publication {
        val found = findByRef(projectId, publicationRef)

        val isSoftDeleted = softDelete(found.id)

        if (!isSoftDeleted) throw BadRequestException(PUBLICATION_BADREQUEST_DELETE)

        return found
    }

    @Throws(BackendException::class)
    fun updatePublication(id: Long, publication: Publication) {
        val updated = update(
            "comment = ?1, version =?2 where id= ?3",
            publication.comment,
            publication.version,
            id
        )

        Optional.of(updated).orElseThrow { BadRequestException(PUBLICATION_BADREQUEST_UPDATE) }
    }

    fun softDelete(publicationId: Long): Boolean {
        val deletedDate = LocalDateTime.now()

        val softDelete = update("deleteddate = ?1 where id = ?2", deletedDate, publicationId)

        if (softDelete != 1) return false

        return true

    }

}