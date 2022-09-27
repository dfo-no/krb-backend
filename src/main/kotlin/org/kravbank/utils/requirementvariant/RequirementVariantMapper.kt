package org.kravbank.utils.requirementvariant

import org.kravbank.domain.Requirement
import org.kravbank.domain.RequirementVariant
import org.kravbank.form.requirementvariant.RequirementVariantForm
import org.kravbank.utils.Mapper

class RequirementVariantMapper : Mapper<RequirementVariantForm, RequirementVariant> {

    // FROM ENTTY
    override fun fromEntity(entity: RequirementVariant): RequirementVariantForm =
        RequirementVariantForm(
            entity.ref,
            entity.description,
            entity.requirementText,
            entity.instruction,
            entity.useProduct,
            entity.useSpesification,
            entity.useQualification
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
        return rv
    }
}