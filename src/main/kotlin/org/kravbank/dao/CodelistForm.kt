package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Codelist
import org.kravbank.utils.Mapper

class CodelistForm() : Mapper<CodelistForm, Codelist> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var ref: String

    lateinit var title: String

    lateinit var description: String

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var codes: List<CodeForm>

    override fun toEntity(domain: CodelistForm): Codelist = Codelist().apply {
        title = domain.title
        description = domain.description
    }

    override fun fromEntity(entity: Codelist): CodelistForm = CodelistForm().apply {
        ref = entity.ref
        title = entity.title
        description = entity.description
        codes = entity.codes.map ( CodeForm()::fromEntity ).toList()
    }
}