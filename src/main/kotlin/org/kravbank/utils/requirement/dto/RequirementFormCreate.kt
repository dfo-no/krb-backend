package org.kravbank.utils.requirement.dto

import org.kravbank.domain.Project

data class RequirementFormCreate(

    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var project: Project? = null,
    var need: String = ""

)
