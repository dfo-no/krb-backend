package org.kravbank.utils.requirement

import org.kravbank.domain.Requirement
import org.kravbank.form.requirement.RequirementForm
import org.kravbank.utils.Mapper

class RequirementMapper : Mapper<RequirementForm, Requirement> {

    // FROM ENTTY
    override fun fromEntity(entity: Requirement): RequirementForm =
        RequirementForm(
            entity.ref,
            entity.title,
            entity.description
        )

    // TO ENTITY
    override fun toEntity(domain: RequirementForm): Requirement {
        val r = Requirement()
        r.title = domain.title
        r.description = domain.description
        return r
    }
}