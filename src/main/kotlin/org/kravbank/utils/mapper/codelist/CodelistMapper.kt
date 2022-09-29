package org.kravbank.utils.mapper.codelist

import org.kravbank.domain.Codelist
import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.mapper.Mapper

class CodelistMapper : Mapper<CodelistForm, Codelist> {

    // FROM ENTITY
    override fun fromEntity(entity: Codelist): CodelistForm =
        CodelistForm(
            entity.ref,
            entity.title,
            entity.description,
        )

    //TO ENTITY
    override fun toEntity(domain: CodelistForm): Codelist {
      val c = Codelist()
        c.title = domain.title
        c.description = domain.description
        return c
    }
}