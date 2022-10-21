package org.kravbank.utils.requirement.mapper

import org.kravbank.domain.Requirement
import org.kravbank.utils.requirement.dto.RequirementFormUpdate

class RequirementUpdateMapper :
    org.kravbank.utils.Mapper<RequirementFormUpdate, Requirement> {

    override fun fromEntity(entity: Requirement): RequirementFormUpdate =
        RequirementFormUpdate(
            entity.title,
            entity.description
        )

    override fun toEntity(domain: RequirementFormUpdate): Requirement {
        val r = Requirement()
        r.title = domain.title
        r.description = domain.description
        return r
    }
}