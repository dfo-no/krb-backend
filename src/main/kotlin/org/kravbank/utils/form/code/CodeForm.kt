package org.kravbank.utils.form.code

import org.kravbank.domain.Codelist

data class CodeForm(
    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var codelist: Codelist? = null
)