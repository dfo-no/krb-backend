package org.kravbank.utils.requirementvariant.mapper

import org.kravbank.domain.RequirementVariant
import org.kravbank.utils.requirementvariant.dto.RequirementVariantFormUpdate

class RequirementVariantUpdateMapper :
    org.kravbank.utils.Mapper<RequirementVariantFormUpdate, RequirementVariant> {

    override fun fromEntity(entity: RequirementVariant): RequirementVariantFormUpdate =
        RequirementVariantFormUpdate(
            entity.description,
            entity.requirementText,
            entity.instruction,
            entity.useProduct,
            entity.useSpesification,
            entity.useQualification
        )

    override fun toEntity(domain: RequirementVariantFormUpdate): RequirementVariant {
        val rv = RequirementVariant()
        rv.description = domain.description
        rv.requirementText = domain.requirementText
        rv.instruction = domain.instruction
        rv.useProduct = domain.useProduct
        rv.useSpesification = domain.useSpesification
        rv.useQualification = domain.useQualification
        return rv
    }
}