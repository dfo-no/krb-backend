package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.RequirementVariant
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequirementVariantRepository : PanacheRepository<RequirementVariant> {
    @Throws(BackendException::class)
    fun findByRef(requirementId: Long, ref: String): RequirementVariant {
        val reqVariant =
            find(
                "ref = ?1 and requirement_id_fk = ?2",
                ref,
                requirementId
            ).firstResult<RequirementVariant>()
        return Optional.ofNullable(reqVariant).orElseThrow { NotFoundException("RequirementVariant was not found!") }
    }

    @Throws(BackendException::class)
    fun findByRefProduct(ref: String): RequirementVariant {
        val reqVariant =
            find(
                "ref = ?1",
                ref
            ).firstResult<RequirementVariant>()
        return Optional.ofNullable(reqVariant).orElseThrow { NotFoundException("RequirementVariant was not found!") }
    }


    @Throws(BackendException::class)
    fun listAllRequirementVariants(id: Long): MutableList<RequirementVariant> {
        return find("requirement_id_fk", id).list()
    }

    @Throws(BackendException::class)
    fun createRequirementVariant(reqVariant: RequirementVariant) {
        persistAndFlush(reqVariant)
        if (!reqVariant.isPersistent) {
            throw BadRequestException("Bad request! RequirementVariant was not created")
        }
    }

    @Throws(BackendException::class)
    fun deleteRequirementVariant(codelistId: Long, codeRef: String): RequirementVariant {
        val deleted: Boolean
        val found = findByRef(codelistId, codeRef)
        deleted = deleteById(found.id)
        if (!deleted) throw BadRequestException("Bad request! RequirementVariant was not deleted")
        return found
    }

    @Throws(BackendException::class)
    fun updateRequirementVariant(id: Long, reqVariant: RequirementVariant) {
        val updated = update(
            "description = ?1, requirementtext = ?2, instruction = ?3, useproduct = ?4, usespesification = ?5, usequalification = ?6 where id = ?7",
            reqVariant.description,
            reqVariant.requirementText,
            reqVariant.instruction,
            reqVariant.useProduct,
            reqVariant.useSpesification,
            reqVariant.useQualification,
            id
        )
        Optional.of(updated).orElseThrow { BadRequestException("Bad request! RequirementVariants did not update") }
    }
}