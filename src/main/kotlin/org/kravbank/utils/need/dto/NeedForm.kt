package org.kravbank.utils.need.dto

import org.kravbank.domain.Project

data class NeedForm(

    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var project: Project? = null

)