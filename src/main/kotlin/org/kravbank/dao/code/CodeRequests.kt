package org.kravbank.dao.code

import org.kravbank.domain.Code

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