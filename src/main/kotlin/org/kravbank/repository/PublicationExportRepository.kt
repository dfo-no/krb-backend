package org.kravbank.repository

import org.kravbank.domain.PublicationExport
import org.kravbank.lang.BackendException
import org.kravbank.lang.NotFoundException
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PublicationExportRepository : BackendRepository<PublicationExport>() {
    @Throws(BackendException::class)
    fun findByRef(publicationId: Long, publicationExportRef: String): PublicationExport {
        val entity =
            find(
                "publication_id_fk = ?1 and ref = ?2",
                publicationId,
                publicationExportRef
            ).firstResult<PublicationExport>()

        return Optional.ofNullable(entity).orElseThrow { NotFoundException("$publicationExportRef not found") }
    }


    fun list(id: Long): List<PublicationExport> {
        return find("publication_id_fk = ?1", id)
            .stream<PublicationExport>()
            .toList()
    }
}