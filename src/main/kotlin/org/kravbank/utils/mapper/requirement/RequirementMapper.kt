package org.kravbank.utils.mapper.requirement

import org.kravbank.domain.Requirement
import org.kravbank.utils.form.requirement.RequirementForm
import org.kravbank.utils.mapper.Mapper

class RequirementMapper : Mapper<RequirementForm, Requirement> {

    // FROM ENTTY
    override fun fromEntity(entity: Requirement): RequirementForm =
        RequirementForm(
            entity.ref,
            entity.title,
            entity.description,
            entity.project,
           // entity.requirementvariants
        )

    // TO ENTITY
    override fun toEntity(domain: RequirementForm): Requirement {
        val r = Requirement()
        r.title = domain.title
        r.description = domain.description
        r.project = domain.project
        return r
    }
}