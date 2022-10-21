package org.kravbank.utils.requirementvariant.mapper

import org.kravbank.domain.RequirementVariant
import org.kravbank.utils.requirementvariant.dto.RequirementVariantForm

class RequirementVariantMapper :
    org.kravbank.utils.Mapper<RequirementVariantForm, RequirementVariant> {

    override fun fromEntity(entity: RequirementVariant): RequirementVariantForm =
        RequirementVariantForm(
            entity.ref,
            entity.description,
            entity.requirementText,
            entity.instruction,
            entity.useProduct,
            entity.useSpesification,
            entity.useQualification,
            entity.requirement
        )

    override fun toEntity(domain: RequirementVariantForm): RequirementVariant {
        val rv = RequirementVariant()
        rv.description = domain.description
        rv.requirementText = domain.requirementText
        rv.instruction = domain.instruction
        rv.useProduct = domain.useProduct
        rv.useSpesification = domain.useSpesification
        rv.useQualification = domain.useQualification
        rv.requirement = domain.requirement
        return rv
    }
}