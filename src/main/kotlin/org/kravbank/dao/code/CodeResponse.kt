package org.kravbank.dao.code

import org.kravbank.domain.Code

/**
 * Alternativ måte til CodeForm og Mapping med:
 * Kotlin extension funksjoner
 */
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
