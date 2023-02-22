package org.kravbank.repository

import org.kravbank.domain.Codelist
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_NOTFOUND
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodelistRepository : BackendRepository<Codelist>() {

    @Throws(BackendException::class)
    fun findByRef(projectId: Long, ref: String): Codelist {
        val codelist =
            find(
                "ref = ?1 and project.id = ?2",
                ref,
                projectId
            ).firstResult<Codelist>()

        return Optional.ofNullable(codelist).orElseThrow { NotFoundException(CODELIST_NOTFOUND) }
    }

    @Throws(BackendException::class)
    fun listAllCodelists(id: Long): List<Codelist> {
        return find("project.id", id).list()
    }

    @Throws(BackendException::class)
    fun updateCodelist(id: Long, codelist: Codelist) {
        val updated = update(
            "title = ?1, description = ?2, serializedcodes = ?3 where id= ?4",
            codelist.title,
            codelist.description,
            codelist.serializedCodes,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException(CODELIST_BADREQUEST_UPDATE) }
    }
}