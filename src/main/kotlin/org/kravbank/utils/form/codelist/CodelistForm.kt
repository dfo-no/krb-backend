package org.kravbank.utils.form.codelist

import org.kravbank.domain.Code
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project


data class CodelistForm(
    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var project: Project? = null,
    //var codes: MutableList<Code> = mutableListOf()
)
