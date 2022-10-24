package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Code

class CodeForm() {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var ref: String

    lateinit var title: String

    lateinit var description: String

    fun toEntity(domain: CodeForm): Code = Code().apply {
        title = domain.title
        description = domain.description
    }

    fun fromEntity(entity: Code): CodeForm = CodeForm().apply {
        ref = entity.ref
        title = entity.title
        description = entity.description
    }
}
