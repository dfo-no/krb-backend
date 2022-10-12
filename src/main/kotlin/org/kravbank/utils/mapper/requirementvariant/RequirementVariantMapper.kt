package org.kravbank.utils.mapper.requirementvariant

import org.kravbank.domain.RequirementVariant
import org.kravbank.utils.form.requirementvariant.RequirementVariantForm
import org.kravbank.utils.mapper.Mapper

class RequirementVariantMapper: Mapper<RequirementVariantForm, RequirementVariant> {

    // FROM ENTTY
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

    // TO ENTITY
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