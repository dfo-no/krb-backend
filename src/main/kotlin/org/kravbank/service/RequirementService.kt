package org.kravbank.service

import org.kravbank.domain.Requirement
import org.kravbank.repository.RequirementRepository
import java.util.*
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class RequirementService(val requirementRepository: RequirementRepository) {
    fun listRequirements(): MutableList<Requirement> = requirementRepository.listAll()

    fun getRequirement(id: Long): Requirement = requirementRepository.findById(id)

    fun createRequirement(requirement: Requirement) = requirementRepository.persistAndFlush(requirement)

    fun exists(id: Long): Boolean = requirementRepository.count("id", id) == 1L

    fun deleteRequirement(id: Long) = requirementRepository.deleteById(id)

    fun updateRequirement(id: Long, requirement: Requirement) {
       // requirementRepository.update(
          //  "comment = ?1, version = ?2, bankid = ?3, date = ?4 where id= ?5",
            // requirement.comment, requirement.version, requirement.bankId, requirement.date, id
        //)

    }

}