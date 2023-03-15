package org.kravbank.repository

import org.kravbank.domain.Requirement
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENT_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENT_NOTFOUND
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequirementRepository : BackendRepository<Requirement>() {
    @Throws(BackendException::class)
    fun findByRef(
        projectId: Long,
        needId: Long,
        ref: String
    ): Requirement {
        val requirement =
            find(
                "ref = ?1 and need_id = ?2 and project_id = ?3",
                ref,
                needId,
                projectId
            ).firstResult<Requirement>()

        return Optional.ofNullable(requirement).orElseThrow { NotFoundException(REQUIREMENT_NOTFOUND) }
    }

    fun listAllRequirements(projectId: Long, needId: Long): List<Requirement> {
        return find(
            "project_id = ?1 and need_id = ?2",
            projectId,
            needId
        )
            .stream<Requirement>()
            .toList()
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