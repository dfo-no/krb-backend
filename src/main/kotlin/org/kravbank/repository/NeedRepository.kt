package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Need
import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class NeedRepository : PanacheRepository<Need> {
    @Throws(BackendException::class)
    fun findByRef(projectId: Long, ref: String): Need {
        val need =
            find(
                "ref = ?1 and project_id_fk = ?2",
                ref,
                projectId
            ).firstResult<Need>()
        return Optional.ofNullable(need).orElseThrow { NotFoundException("Need not found") }
    }

    @Throws(BackendException::class)
    fun listAllNeeds(id: Long): MutableList<Need> {
        return find("project_id_fk", id).list()
    }

    @Throws(BackendException::class)
    fun createNeed(need: Need) {
        persistAndFlush(need)
        if (!need.isPersistent) {
            throw BadRequestException("Bad request! Need was not created")
        }
    }

    @Throws(BackendException::class)
    fun deleteNeed(projectId: Long, needRef: String) {
        val deleted: Boolean
        val found = findByRef(projectId, needRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException("Bad request! Need was not deleted")
    }

    @Throws(BackendException::class)
    fun updateNeed(id: Long, need: Need) {
        val updated = update(
            "title = ?1, description = ?2 where id= ?3",
            need.title,
            need.description,
            //need.deletedDate,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException("Fail! Need did not update") }
    }

}