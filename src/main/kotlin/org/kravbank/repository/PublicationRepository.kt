package org.kravbank.repository

import org.kravbank.domain.Publication
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.PUBLICATION_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.PUBLICATION_NOTFOUND
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PublicationRepository : BackendRepository<Publication>() {
    @Throws(BackendException::class)
    fun findByRef(projectId: Long, ref: String): Publication {
        val publication =
            find(
                "ref = ?1 and project_id = ?2",
                ref,
                projectId
            ).firstResult<Publication>()

        if (publication != null) {
            return publication
        } else throw NotFoundException(PUBLICATION_NOTFOUND)
    }

    fun listAllPublications(id: Long): List<Publication> {
        return find("project_id = ?1", id)
            .stream<Publication>()
            .toList()
    }

    @Throws(BackendException::class)
    fun updatePublication(id: Long, comment: String) {
        val updated = update(
            "comment = ?1 where id = ?2",
            comment,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException(PUBLICATION_BADREQUEST_UPDATE) }
    }

}