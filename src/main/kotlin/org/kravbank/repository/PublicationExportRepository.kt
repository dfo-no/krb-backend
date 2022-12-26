package org.kravbank.repository

import org.kravbank.domain.PublicationExport
import org.kravbank.lang.BackendException
import org.kravbank.lang.NotFoundException
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PublicationExportRepository : BackendRepository<PublicationExport>() {
    @Throws(BackendException::class)
    fun findByRef(ref: String): PublicationExport {
        val entity =
            find(
                "ref = ?1",
                ref
            ).firstResult<PublicationExport>()

        return Optional.ofNullable(entity).orElseThrow { NotFoundException("$ref not found") }
    }


}