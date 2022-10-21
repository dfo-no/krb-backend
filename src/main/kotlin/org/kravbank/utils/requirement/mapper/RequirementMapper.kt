package org.kravbank.utils.requirement.mapper

import org.kravbank.domain.Requirement
import org.kravbank.utils.requirement.dto.RequirementForm

class RequirementMapper : org.kravbank.utils.Mapper<RequirementForm, Requirement> {

    override fun fromEntity(entity: Requirement): RequirementForm =
        RequirementForm(
            entity.ref,
            entity.title,
            entity.description,
            entity.project,
        )

    override fun toEntity(domain: RequirementForm): Requirement {
        val r = Requirement()
        r.title = domain.title
        r.description = domain.description
        r.project = domain.project
        return r
    }
}