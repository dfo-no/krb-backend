package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Codelist
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_BADREQUEST_DELETE
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_NOTFOUND
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodelistRepository : PanacheRepository<Codelist> {

    @Throws(BackendException::class)
    fun findByRef(projectId: Long, ref: String): Codelist {
        val codelist =
            find(
                "ref = ?1 and project_id_fk = ?2",
                ref,
                projectId
            ).firstResult<Codelist>()

        return Optional.ofNullable(codelist).orElseThrow { NotFoundException(CODELIST_NOTFOUND) }

    }

    @Throws(BackendException::class)
    fun listAllCodelists(id: Long): List<Codelist> {

        return find("project_id_fk", id).list()
    }

    @Throws(BackendException::class)
    fun createCodelist(codelist: Codelist) {
        persistAndFlush(codelist)
        if (!codelist.isPersistent) {
            throw BadRequestException(CODELIST_BADREQUEST_CREATE)
        }
    }

    @Throws(BackendException::class)
    fun deleteCodelist(projectId: Long, codelistRef: String): Codelist {
        val deleted: Boolean
        val found = findByRef(projectId, codelistRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException(CODELIST_BADREQUEST_DELETE)
        return found
    }

    @Throws(BackendException::class)
    fun updateCodelist(id: Long, codelist: Codelist) {
        val updated = update(
            "title = ?1, description = ?2 where id= ?3",
            codelist.title,
            codelist.description,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException(CODELIST_BADREQUEST_UPDATE) }
    }
}
