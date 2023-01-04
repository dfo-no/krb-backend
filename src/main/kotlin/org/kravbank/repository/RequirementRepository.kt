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
    fun findByRef(projectId: Long, ref: String): Requirement {
        val requirement =
            find(
                "ref = ?1 and project.id = ?2",
                ref,
                projectId
            ).firstResult<Requirement>()

        return Optional.ofNullable(requirement).orElseThrow { NotFoundException(REQUIREMENT_NOTFOUND) }
    }

    fun listAllRequirements(id: Long): List<Requirement> {
        return find("project.id", id)
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