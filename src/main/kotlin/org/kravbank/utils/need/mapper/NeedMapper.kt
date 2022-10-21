package org.kravbank.utils.need.mapper

import org.kravbank.domain.Need
import org.kravbank.utils.need.dto.NeedForm

class NeedMapper : org.kravbank.utils.Mapper<NeedForm, Need> {

    override fun fromEntity(entity: Need): NeedForm =
        NeedForm(
            entity.ref,
            entity.title,
            entity.description,
            entity.project
        )

    override fun toEntity(domain: NeedForm): Need {
        val n = Need()
        n.title = domain.title
        n.description = domain.description
        n.project = domain.project
        return n
    }
}