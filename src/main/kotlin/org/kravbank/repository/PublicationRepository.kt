package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Publication
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import java.time.LocalDateTime
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PublicationRepository : PanacheRepository<Publication> {
    @Throws(BackendException::class)
    fun findByRef(projectId: Long, ref: String): Publication {
        val publication =
            find(
                "ref = ?1 and project_id_fk = ?2",
                ref,
                projectId
            ).firstResult<Publication>()
        if (publication?.deletedDate == null) {
            return publication
        } else throw NotFoundException("Publication not found")
    }

    fun listAllPublications(id: Long): List<Publication> {
        return find("project_id_fk", id).list<Publication>().filter { p -> p.deletedDate == null }
    }

    @Throws(BackendException::class)
    fun createPublication(publication: Publication) {
        persistAndFlush(publication)
        if (!publication.isPersistent) {
            throw BadRequestException("Bad request! Publication was not created")
        }
    }

    fun deletePublication(id: Long){
        val deletedDate = LocalDateTime.now()
        update("deleteddate = ?1 where id = ?2", deletedDate,id)
    }

    @Throws(BackendException::class)
    fun updatePublication(id: Long, publication: Publication) {
        val updated = update(
            "comment = ?1, version =?2 where id= ?3",
            publication.comment,
            publication.version,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException("Bad request! Requirement did not update") }
    }

}