package org.kravbank.service

import org.kravbank.domain.RequirementVariant
import org.kravbank.repository.RequirementVariantRepository
import java.util.*
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class RequirementVariantService(val requirementVariantRepository: RequirementVariantRepository) {
    fun listRequirementVariants(): MutableList<RequirementVariant> = requirementVariantRepository.listAll()

    fun getRequirementVariant(id: Long): RequirementVariant = requirementVariantRepository.findById(id)

    fun createRequirementVariant(requirementVariant: RequirementVariant) = requirementVariantRepository.persistAndFlush(requirementVariant)

    fun exists(id: Long): Boolean = requirementVariantRepository.count("id", id) == 1L

    fun deleteRequirementVariant(id: Long) = requirementVariantRepository.deleteById(id)

    fun updateRequirementVariant(id: Long, requirementVariant: RequirementVariant) {
       // requirementVariantRepository.update(
          //  "comment = ?1, version = ?2, bankid = ?3, date = ?4 where id= ?5",
            // requirementVariant.comment, requirementVariant.version, requirementVariant.bankId, requirementVariant.date, id
        //)

    }

}