package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Requirement
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENT_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENT_BADREQUEST_DELETE
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENT_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENT_NOTFOUND
import java.util.*
import javax.enterprise.context.ApplicationScoped
import kotlin.streams.toList

@ApplicationScoped
class RequirementRepository : PanacheRepository<Requirement> {
    @Throws(BackendException::class)
    fun findByRef(projectId: Long, ref: String): Requirement {
        val requirement =
            find(
                "ref = ?1 and project_id_fk = ?2",
                ref,
                projectId
            ).firstResult<Requirement>()
        return Optional.ofNullable(requirement).orElseThrow { NotFoundException(REQUIREMENT_NOTFOUND) }
    }

    @Throws(BackendException::class)
    fun listAllRequirements(id: Long): List<Requirement> {
        return find("project_id_fk", id)
            .stream<Requirement>()
            .toList()
    }

    @Throws(BackendException::class)
    fun createRequirement(requirement: Requirement) {
        persistAndFlush(requirement)
        if (!requirement.isPersistent) {
            throw BadRequestException(REQUIREMENT_BADREQUEST_CREATE)
        }
    }

    @Throws(BackendException::class)
    fun deleteRequirement(projectId: Long, requirementRef: String): Requirement {
        val deleted: Boolean
        val found = findByRef(projectId, requirementRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException(REQUIREMENT_BADREQUEST_DELETE)
        return found
    }

    @Throws(BackendException::class)
    fun updateRequirement(id: Long, requirement: Requirement) {
        val updated = update(
            "title = ?1, description = ?2 where id= ?3",
            requirement.title,
            requirement.description,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException(REQUIREMENT_BADREQUEST_UPDATE) }
    }
}