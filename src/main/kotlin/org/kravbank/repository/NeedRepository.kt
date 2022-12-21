package org.kravbank.repository

import org.kravbank.domain.Need
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.NEED_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.NEED_NOTFOUND
import org.kravbank.utils.Messages.RepoErrorMsg.NEED_NOTFOUND_REQUIREMENT
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class NeedRepository : BackendRepository<Need>() {
    @Throws(BackendException::class)
    fun findByRef(projectId: Long, ref: String): Need {
        val need =
            find(
                "ref = ?1 and project_id_fk = ?2",
                ref,
                projectId
            ).firstResult<Need>()

        return Optional.ofNullable(need).orElseThrow { NotFoundException(NEED_NOTFOUND) }
    }

    @Throws(BackendException::class)
    fun findByRefRequirement(ref: String): Need {
        val need =
            find(
                "ref = ?1",
                ref
            ).firstResult<Need>()

        return Optional.ofNullable(need).orElseThrow { NotFoundException(NEED_NOTFOUND_REQUIREMENT) }
    }


    fun listAllNeeds(id: Long): List<Need> {
        return find("project_id_fk", id)
            .stream<Need>()
            .toList()
    }


    @Throws(BackendException::class)
    fun updateNeed(id: Long, need: Need) {
        val updated = update(
            "title = ?1, description = ?2 where id= ?3",
            need.title,
            need.description,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException(NEED_BADREQUEST_UPDATE) }
    }

}