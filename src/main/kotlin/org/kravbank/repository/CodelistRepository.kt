package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Codelist
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import java.util.*
import javax.enterprise.context.ApplicationScoped
import kotlin.streams.toList

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
        return Optional.ofNullable(codelist).orElseThrow { NotFoundException("Codelist was not found!") }
    }

    @Throws(BackendException::class)
    fun listAllCodelists(id: Long): List<Codelist> {
        return find("project_id_fk", id).stream<Codelist?>().toList()
    }

    @Throws(BackendException::class)
    fun createCodelist(codelist: Codelist) {
        persistAndFlush(codelist)
        if (!codelist.isPersistent) {
            throw BadRequestException("Bad request! Codelist was not created")
        }
    }

    @Throws(BackendException::class)
    fun deleteCodelist(projectId: Long, codelistRef: String): Codelist {
        val deleted: Boolean
        val found = findByRef(projectId, codelistRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException("Bad request! Codelist was not deleted")
        return found
    }

    @Throws(BackendException::class)
    fun updateCodelist(id: Long, codelist: Codelist) {
        val updated = update(
            "title = ?1, description = ?2 where id= ?3",
            codelist.title,
            codelist.description,
            //codelist.deletedDate,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException("Bad request! Codelist did not update") }
    }
}
