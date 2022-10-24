package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Codelist

class CodelistForm() {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var ref: String

    lateinit var title: String

    lateinit var description: String

    fun toEntity(domain: CodelistForm): Codelist = Codelist().apply {
        title = domain.title
        description = domain.description
    }

    fun fromEntity(entity: Codelist): CodelistForm = CodelistForm().apply {
        ref = entity.ref
        title = entity.title
        description = entity.description
    }
}