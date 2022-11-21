package org.kravbank.dao.code

import org.kravbank.domain.Code

data class CodeResponse(
    val ref: String,
    val title: String,
    val description: String
)

fun Code.toResponse() = CodeResponse(
    ref = ref,
    title = title,
    description = description
)
