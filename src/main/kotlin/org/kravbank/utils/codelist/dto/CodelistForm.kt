package org.kravbank.utils.codelist.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import org.kravbank.domain.Project


data class CodelistForm(

    var ref: String = "",
    var title: String = "",
    var description: String = "",
    @JsonIgnore
    var project: Project? = null

)
