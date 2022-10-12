package org.kravbank.utils.mapper.requirement

import org.kravbank.domain.Requirement
import org.kravbank.utils.form.requirement.RequirementFormUpdate
import org.kravbank.utils.mapper.Mapper

class RequirementUpdateMapper : Mapper<RequirementFormUpdate, Requirement> {

    // FROM ENTTY
    override fun fromEntity(entity: Requirement): RequirementFormUpdate =
        RequirementFormUpdate(
            entity.title,
            entity.description
        )

    // TO ENTITY
    override fun toEntity(domain: RequirementFormUpdate): Requirement {
        val r = Requirement()
        r.title = domain.title
        r.description = domain.description
        return r
    }
}