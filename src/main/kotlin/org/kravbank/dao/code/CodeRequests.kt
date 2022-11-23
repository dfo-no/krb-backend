package org.kravbank.dao.code

import org.kravbank.domain.Code

/** TODO:
 * Alternativ måte til CodeForm og Mapping å håndtere requests
 * Kotlin extension functions
 * Istedenfor å arve fra klasse eller bruke design patterns
 */
data class CodeCreateRequest(
    val title: String,
    val description: String
)

fun CodeCreateRequest.toEntity() = Code(
    title = title,
    description = description
)

data class CodeUpdateRequest(
    val title: String,
    val description: String
)

fun CodeUpdateRequest.toEntity(ref: String) = Code(
    ref = ref,
    title = title,
    description = description
)