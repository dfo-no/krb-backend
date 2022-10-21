package org.kravbank.utils.codelist.mapper

import org.kravbank.domain.Codelist
import org.kravbank.utils.codelist.dto.CodelistFormUpdate

class CodelistUpdateMapper : org.kravbank.utils.Mapper<CodelistFormUpdate, Codelist> {

    override fun fromEntity(entity: Codelist): CodelistFormUpdate =
        CodelistFormUpdate(
            entity.title,
            entity.description
        )
    override fun toEntity(domain: CodelistFormUpdate): Codelist {
        val c = Codelist()
        c.title = domain.title
        c.description = domain.description
        return c
    }
}