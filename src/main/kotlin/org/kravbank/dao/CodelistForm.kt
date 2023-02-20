package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Code2
import org.kravbank.domain.Codelist
import org.kravbank.utils.Mapper

class CodelistForm(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var ref: String = "",

    var title: String = "",

    var description: String = "",

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var serializedCodes: String = "",

    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var codes: List<Code2>? = null

) : Mapper<CodelistForm, Codelist> {

    override fun toEntity(domain: CodelistForm): Codelist = Codelist()
        .apply {
            title = domain.title
            description = domain.description
        }


    override fun fromEntity(entity: Codelist): CodelistForm = CodelistForm()
        .apply {
            ref = entity.ref
            title = entity.title
            description = entity.description
        }

    override fun toString(): String {
        return "CodelistForm(title='$title', description='$description', codes='$codes', listCodes=$serializedCodes)"
    }
}