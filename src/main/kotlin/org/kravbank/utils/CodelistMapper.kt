package org.kravbank.utils

import org.kravbank.domain.Code
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import org.kravbank.form.CodelistForm

class CodelistMapper : Mapper<CodelistForm, Codelist> {

    override fun fromEntity(entity: Codelist): CodelistForm =
        CodelistForm(
            entity.ref,
            entity.title,
            entity.description,
        )



    override fun toEntity(domain: CodelistForm): Codelist {
      val c = Codelist()
        c.title = domain.title
        c.description = domain.description
        // ref ?
        return c
    }

}