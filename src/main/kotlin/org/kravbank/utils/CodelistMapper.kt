package org.kravbank.utils

import org.kravbank.domain.Codelist
import org.kravbank.form.CodelistForm

class CodelistMapper : Mapper<CodelistForm, Codelist>{

    override fun fromEntity(entity: Codelist): CodelistForm =
         CodelistForm(
            entity.title,
            entity.description
        )


    override fun toEntity(domain: CodelistForm): Codelist {
        TODO("Not yet implemented")
    }
}