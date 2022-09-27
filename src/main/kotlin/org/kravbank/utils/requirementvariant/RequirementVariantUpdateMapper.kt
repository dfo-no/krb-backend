package org.kravbank.utils.requirementvariant

import org.kravbank.domain.RequirementVariant
import org.kravbank.form.requirementvariant.RequirementVariantFormUpdate
import org.kravbank.utils.Mapper

class RequirementVariantUpdateMapper : Mapper<RequirementVariantFormUpdate, RequirementVariant> {

    // FROM ENTTY
    override fun fromEntity(entity: RequirementVariant): RequirementVariantFormUpdate =
        RequirementVariantFormUpdate(
            entity.description,
            entity.requirementText,
            entity.instruction,
            entity.useProduct,
            entity.useSpesification,
            entity.useQualification
        )

    // TO ENTITY
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