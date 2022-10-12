package org.kravbank.utils.form.requirement

import org.kravbank.domain.Project
import org.kravbank.domain.RequirementVariant

data class RequirementForm(
    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var project: Project? = null,
    var requirmentVariant: MutableList<RequirementVariant>? = null
)
