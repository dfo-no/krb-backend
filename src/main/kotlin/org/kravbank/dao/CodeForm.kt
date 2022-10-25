package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Code
import org.kravbank.utils.Mapper

class CodeForm() : Mapper<CodeForm, Code> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var ref: String

    lateinit var title: String

    lateinit var description: String

    override fun toEntity(domain: CodeForm): Code = Code().apply {
        title = domain.title
        description = domain.description
    }

    override fun fromEntity(entity: Code): CodeForm = CodeForm().apply {
        ref = entity.ref
        title = entity.title
        description = entity.description
    }
}
