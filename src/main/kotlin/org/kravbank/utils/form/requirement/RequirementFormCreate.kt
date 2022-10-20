package org.kravbank.utils.form.requirement

import org.kravbank.domain.Need
import org.kravbank.domain.Project
import org.kravbank.domain.RequirementVariant

data class RequirementFormCreate(
    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var project: Project? = null,
    //var requirmentVariant: MutableList<RequirementVariant>? = null,
    var need: String = ""
    //var need: Need? = null,

)
