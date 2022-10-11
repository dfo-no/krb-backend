package org.kravbank.utils.mapper.code

import org.kravbank.domain.Code
import org.kravbank.utils.form.code.CodeForm
import org.kravbank.utils.mapper.Mapper

class CodeMapper: Mapper<CodeForm, Code> {
    // FROM ENTITY
    override fun fromEntity(entity: Code): CodeForm =
        CodeForm(
            entity.ref,
            entity.title,
            entity.description,
            entity.codelist
        )

    //TO ENTITY
    override fun toEntity(domain: CodeForm): Code {
        val c = Code()
        c.title = domain.title
        c.description = domain.description
        c.codelist = domain.codelist
        return c
    }
}