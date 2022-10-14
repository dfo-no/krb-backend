package org.kravbank.utils.form.product

import org.kravbank.domain.Project
import org.kravbank.domain.RequirementVariant


data class ProductFormCreate(
    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var project: Project? = null,
    var requirementvariant: String = ""
)
