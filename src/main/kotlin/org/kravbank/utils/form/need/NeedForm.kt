package org.kravbank.utils.form.need

import org.kravbank.domain.Project

data class NeedForm(
    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var project: Project? = null
)