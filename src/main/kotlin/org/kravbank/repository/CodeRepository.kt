package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Code
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodeRepository : PanacheRepository<Code> {
    @Throws(BackendException::class)
    fun findByRef(codelistId: Long, ref: String): Code {
        val code =
            find(
                "ref = ?1 and codelist_id_fk = ?2",
                ref,
                codelistId
            ).firstResult<Code>()
        return Optional.ofNullable(code).orElseThrow { NotFoundException("Code was not found!") }
    }

    @Throws(BackendException::class)
    fun listAllCodes(id: Long): MutableList<Code> {
        return find("codelist_id_fk", id).list()
    }

    @Throws(BackendException::class)
    fun createCode(code: Code) {
        persistAndFlush(code)
        if (!code.isPersistent) {
            throw BadRequestException("Bad request! Code was not created")
        }
    }

    @Throws(BackendException::class)
    fun deleteCode(codelistId: Long, codeRef: String): Code {
        val deleted: Boolean
        val found = findByRef(codelistId, codeRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException("Bad request! Code was not deleted")
        return found
    }

    @Throws(BackendException::class)
    fun updateCode(id: Long, code: Code) {
        val updated = update(
            "title = ?1, description = ?2 where id= ?3",
            code.title,
            code.description,
            //code.deletedDate,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException("Bad request! Code did not update") }
    }

}