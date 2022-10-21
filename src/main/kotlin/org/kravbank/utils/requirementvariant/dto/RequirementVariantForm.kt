package org.kravbank.utils.requirementvariant.dto

import org.kravbank.domain.Requirement

data class RequirementVariantForm(

    var ref: String = "",
    var description: String = "",
    var requirementText: String = "",
    var instruction: String = "",
    var useProduct: Boolean = false,
    var useSpesification: Boolean = false,
    var useQualification: Boolean = false,
    var requirement: Requirement? = null

)
