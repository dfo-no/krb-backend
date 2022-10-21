package org.kravbank.utils.code.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import org.kravbank.domain.Codelist

data class CodeForm(

    var ref: String = "",
    var title: String = "",
    var description: String = "",
    @JsonIgnore
    var codelist: Codelist? = null

)

