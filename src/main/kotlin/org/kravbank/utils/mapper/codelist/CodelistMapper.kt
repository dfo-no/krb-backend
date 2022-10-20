package org.kravbank.utils.mapper.codelist

import org.kravbank.domain.Codelist
import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.mapper.Mapper
import java.util.*

class CodelistMapper : Mapper<CodelistForm, Codelist> {

    // FROM ENTITY
    override fun fromEntity(entity: Codelist): CodelistForm =
        CodelistForm(
            entity.ref,
            entity.title,
            entity.description,
            entity.project,
            // entity.codes
        )

    //TO ENTITY
    override fun toEntity(domain: CodelistForm): Codelist {
        val c = Codelist()
        /**
         * todo
         * codelistFormCreate
         */
        c.ref = UUID.randomUUID().toString()
        c.title = domain.title
        c.description = domain.description
        c.project = domain.project
        //c.codes = domain.codes
        return c
    }
}