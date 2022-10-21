package org.kravbank.utils.code.mapper

import org.kravbank.domain.Code
import org.kravbank.utils.code.dto.CodeForm

class CodeMapper : org.kravbank.utils.Mapper<CodeForm, Code> {
    override fun fromEntity(entity: Code): CodeForm =
        CodeForm(
            entity.ref,
            entity.title,
            entity.description,
            entity.codelist
        )

    override fun toEntity(domain: CodeForm): Code {
        val c = Code()
        c.title = domain.title
        c.description = domain.description
        c.codelist = domain.codelist
        return c
    }
}