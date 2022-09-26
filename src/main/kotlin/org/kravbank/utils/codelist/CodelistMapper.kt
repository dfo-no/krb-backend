package org.kravbank.utils.codelist

import org.kravbank.domain.Codelist
import org.kravbank.form.codelist.CodelistForm
import org.kravbank.utils.Mapper

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