package org.kravbank.utils.code.dto

import org.kravbank.domain.Codelist

data class CodeForm(

    var ref: String = "",
    var title: String = "",
    var description: String = "",
    var codelist: Codelist? = null

)

