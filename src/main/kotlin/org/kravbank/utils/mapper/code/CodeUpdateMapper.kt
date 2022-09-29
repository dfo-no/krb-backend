package org.kravbank.utils.mapper.code

import org.kravbank.domain.Code
import org.kravbank.utils.form.code.CodeFormUpdate
import org.kravbank.utils.mapper.Mapper

class CodeUpdateMapper : Mapper<CodeFormUpdate, Code> {
    // FROM ENTITY
    override fun fromEntity(entity: Code): CodeFormUpdate =
        CodeFormUpdate(
            entity.title,
            entity.description,
        )

    //TO ENTITY
    override fun toEntity(domain: CodeFormUpdate): Code {
        val c = Code()
        c.title = domain.title
        c.description = domain.description
        return c
    }
}