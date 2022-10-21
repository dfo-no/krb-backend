package org.kravbank.utils.requirement.mapper

import org.kravbank.domain.Requirement
import org.kravbank.utils.requirement.dto.RequirementFormCreate

class RequirementCreateMapper() :
    org.kravbank.utils.Mapper<RequirementFormCreate, Requirement> {

    override fun fromEntity(entity: Requirement): RequirementFormCreate =
        RequirementFormCreate(
            entity.ref,
            entity.title,
            entity.description,
            entity.project,
        )

    override fun toEntity(domain: RequirementFormCreate): Requirement {
        val r = Requirement()
        r.title = domain.title
        r.description = domain.description
        r.project = domain.project
        return r
    }
}