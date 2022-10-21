package org.kravbank.utils.codelist.mapper

import org.kravbank.domain.Codelist
import org.kravbank.utils.codelist.dto.CodelistForm
import java.util.*

class CodelistMapper : org.kravbank.utils.Mapper<CodelistForm, Codelist> {

    override fun fromEntity(entity: Codelist): CodelistForm =
        CodelistForm(
            entity.ref,
            entity.title,
            entity.description,
            entity.project,
        )

    override fun toEntity(domain: CodelistForm): Codelist {
        val c = Codelist()
        c.ref = UUID.randomUUID().toString()
        c.title = domain.title
        c.description = domain.description
        c.project = domain.project
        return c
    }
}