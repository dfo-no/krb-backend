package org.kravbank.repository

import org.kravbank.domain.Code
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
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
                "ref = ?1 and codelist.id = ?2",
                ref,
                codelistId
            ).firstResult<Code>()
        return Optional.ofNullable(code).orElseThrow { NotFoundException(CODE_NOTFOUND) }
    }

    fun listAllCodes(id: Long): List<Code> {
        return find("codelist.id", id).stream<Code>().toList()
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