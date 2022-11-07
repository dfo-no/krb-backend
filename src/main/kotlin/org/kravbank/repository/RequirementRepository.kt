package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Requirement
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
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
        return Optional.ofNullable(requirement).orElseThrow { NotFoundException("Requirement not found") }
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
            throw BadRequestException("Bad request! Requirement was not created")
        }
    }

    @Throws(BackendException::class)
    fun deleteRequirement(projectId: Long, requirementRef: String): Requirement{
        val deleted: Boolean
        val found = findByRef(projectId, requirementRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException("Bad request! Requirement was not deleted")
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
        Optional.of(updated).orElseThrow { BadRequestException("Bad request! Requirement did not update") }
    }
}