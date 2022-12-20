package org.kravbank.repository

import org.kravbank.domain.RequirementVariant
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENTVARIANT_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENTVARIANT_BADREQUEST_DELETE
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENTVARIANT_BADREQUEST_UPDATE
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENTVARIANT_NOTFOUND
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENTVARIANT_NOTFOUND_PRODUCT
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequirementVariantRepository : BackendRepository<RequirementVariant>() {
    @Throws(BackendException::class)
    fun findByRef(requirementId: Long, ref: String): RequirementVariant {
        val reqVariant =
            find(
                "ref = ?1 and requirement_id_fk = ?2",
                ref,
                requirementId
            ).firstResult<RequirementVariant>()
        return Optional.ofNullable(reqVariant).orElseThrow { NotFoundException(REQUIREMENTVARIANT_NOTFOUND) }
    }

    @Throws(BackendException::class)
    fun findByRefProduct(ref: String): RequirementVariant {
        val reqVariant =
            find(
                "ref = ?1",
                ref
            ).firstResult<RequirementVariant>()
        return Optional.ofNullable(reqVariant).orElseThrow { NotFoundException(REQUIREMENTVARIANT_NOTFOUND_PRODUCT) }
    }


    @Throws(BackendException::class)
    fun listAllRequirementVariants(id: Long): List<RequirementVariant> {
        return find("requirement_id_fk", id)
            .stream<RequirementVariant>()
            .toList()
    }

    @Throws(BackendException::class)
    fun createRequirementVariant(reqVariant: RequirementVariant) {
        persistAndFlush(reqVariant)
        if (!reqVariant.isPersistent) {
            throw BadRequestException(REQUIREMENTVARIANT_BADREQUEST_CREATE)
        }
    }

    @Throws(BackendException::class)
    fun deleteRequirementVariant(requirementId: Long, requirementVariantRef: String): RequirementVariant {
        val deleted: Boolean
        val found = findByRef(requirementId, requirementVariantRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException(REQUIREMENTVARIANT_BADREQUEST_DELETE)
        return found
    }

    @Throws(BackendException::class)
    fun updateRequirementVariant(id: Long, reqVariant: RequirementVariant) {
        val updated = update(
            "description = ?1, requirementtext = ?2, instruction = ?3, useproduct = ?4, usespecification = ?5, usequalification = ?6 where id = ?7",
            reqVariant.description,
            reqVariant.requirementText,
            reqVariant.instruction,
            reqVariant.useProduct,
            reqVariant.useSpecification,
            reqVariant.useQualification,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException(REQUIREMENTVARIANT_BADREQUEST_UPDATE) }
    }
}