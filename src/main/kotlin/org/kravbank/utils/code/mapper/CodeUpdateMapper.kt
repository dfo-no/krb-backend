package org.kravbank.utils.code.mapper

import org.kravbank.domain.Code
import org.kravbank.utils.code.dto.CodeFormUpdate

class CodeUpdateMapper : org.kravbank.utils.Mapper<CodeFormUpdate, Code> {

    override fun fromEntity(entity: Code): CodeFormUpdate =
        CodeFormUpdate(
            entity.title,
            entity.description,
        )
    override fun toEntity(domain: CodeFormUpdate): Code {
        val c = Code()
        c.title = domain.title
        c.description = domain.description
        return c
    }
}