package org.kravbank.dao.code

import org.kravbank.domain.Code

/** TODO:
 * Alternativ måte til CodeForm og Mapping å håndtere responses
 * Kotlin extension functions
 * Istedenfor å arve fra klasse eller bruke design patterns
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
