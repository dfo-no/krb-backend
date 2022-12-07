package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.RequirementVariant
import org.kravbank.utils.Mapper

class RequirementVariantForm: Mapper<RequirementVariantForm, RequirementVariant> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var ref: String = ""

    var description: String = ""

    var requirementText: String = ""

    var instruction: String = ""

    var useProduct: Boolean = false

    var useSpecification: Boolean = false

    var useQualification: Boolean = false


    override fun toEntity(domain: RequirementVariantForm): RequirementVariant =
        RequirementVariant().apply {
        description = domain.description
        requirementText = domain.requirementText
        instruction = domain.instruction
        useProduct = domain.useProduct
        useSpecification = domain.useSpecification
        useQualification = domain.useQualification
    }

    override fun fromEntity(entity: RequirementVariant): RequirementVariantForm = RequirementVariantForm().apply {
        ref = entity.ref
        description = entity.description
        requirementText = entity.requirementText
        instruction = entity.instruction
        useProduct = entity.useProduct
        useSpecification = entity.useSpecification
        useQualification = entity.useQualification
    }
}
