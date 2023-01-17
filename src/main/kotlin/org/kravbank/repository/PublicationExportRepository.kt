package org.kravbank.repository

import org.kravbank.domain.PublicationExport
import org.kravbank.lang.BackendException
import org.kravbank.lang.NotFoundException
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PublicationExportRepository : BackendRepository<PublicationExport>() {
    @Throws(BackendException::class)
    fun findByRef(publicationRef: String, publicationExportRef: String): PublicationExport {
        val entity =
            find(
                "publicationRef = ?1 and ref = ?2",
                publicationRef,
                publicationExportRef
            ).firstResult<PublicationExport>()

        return Optional.ofNullable(entity).orElseThrow { NotFoundException("$publicationExportRef not found") }
    }


    fun list(id: Long): List<PublicationExport> {
        return find("publicationRef = ?1", id)
            .stream<PublicationExport>()
            .toList()
    }
}