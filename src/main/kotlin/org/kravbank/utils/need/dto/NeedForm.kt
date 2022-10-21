package org.kravbank.utils.need.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import org.kravbank.domain.Project

data class NeedForm(

    var ref: String = "",
    var title: String = "",
    var description: String = "",
    @JsonIgnore
    var project: Project? = null

)