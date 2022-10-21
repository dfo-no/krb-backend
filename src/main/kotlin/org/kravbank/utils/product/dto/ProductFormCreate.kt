package org.kravbank.utils.product.dto

import org.kravbank.domain.Project


data class ProductFormCreate(

    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var project: Project? = null,
    var requirementvariant: String = ""

)
