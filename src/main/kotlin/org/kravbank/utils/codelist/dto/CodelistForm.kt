package org.kravbank.utils.codelist.dto

import org.kravbank.domain.Project


data class CodelistForm(

    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var project: Project? = null

)
