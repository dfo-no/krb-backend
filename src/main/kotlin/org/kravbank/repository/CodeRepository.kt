package org.kravbank.repository

import org.kravbank.domain.Code
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.CODE_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.CODE_BADREQUEST_DELETE
import org.kravbank.utils.Messages.RepoErrorMsg.CODE_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.CODE_NOTFOUND
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodeRepository : BackendRepository<Code>() {
    @Throws(BackendException::class)
    fun findByRef(codelistId: Long, ref: String): Code {
        val code =
            find(
                "ref = ?1 and codelist_id_fk = ?2",
                ref,
                codelistId
            ).firstResult<Code>()
        return Optional.ofNullable(code).orElseThrow { NotFoundException(CODE_NOTFOUND) }
    }

    @Throws(BackendException::class)
    fun listAllCodes(id: Long): List<Code> {
        return find("codelist_id_fk", id).stream<Code>().toList()
    }

    @Throws(BackendException::class)
    fun createCode(code: Code) {
        persistAndFlush(code)
        if (!code.isPersistent) {
            throw BadRequestException(CODE_BADREQUEST_CREATE)
        }
    }

    @Throws(BackendException::class)
    fun deleteCode(codelistId: Long, codeRef: String): Code {
        val deleted: Boolean
        val found = findByRef(codelistId, codeRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException(CODE_BADREQUEST_DELETE)
        return found
    }

    @Throws(BackendException::class)
    fun updateCode(id: Long, code: Code) {
        val updated = update(
            "title = ?1, description = ?2 where id= ?3",
            code.title,
            code.description,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException(CODE_BADREQUEST_UPDATE) }
    }

}