package org.kravbank.form.requirementvariant

data class RequirementVariantFormUpdate(
    var description: String = "",
    var requirementText: String = "",
    var instruction: String = "",
    var useProduct: Boolean = false,
    var useSpesification: Boolean = false,
    var useQualification: Boolean = false,
)
