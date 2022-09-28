package org.kravbank.utils.mapper.codelist

import org.kravbank.domain.Codelist
import org.kravbank.utils.form.codelist.CodelistFormUpdate
import org.kravbank.utils.mapper.Mapper

class CodelistUpdateMapper :
    org.kravbank.utils.mapper.Mapper<CodelistFormUpdate, Codelist> {

    // FROM ENTTY
    override fun fromEntity(entity: Codelist): CodelistFormUpdate =
        CodelistFormUpdate(
            entity.title,
            entity.description
        )

    // TO ENTITY
    override fun toEntity(domain: CodelistFormUpdate): Codelist {
        val c = Codelist()
        c.title = domain.title
        c.description = domain.description
        return c
    }
}