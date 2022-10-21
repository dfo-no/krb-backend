package org.kravbank.utils.need.mapper

import org.kravbank.domain.Need
import org.kravbank.utils.need.dto.NeedFormUpdate

class NeedUpdateMapper : org.kravbank.utils.Mapper<NeedFormUpdate, Need> {

    override fun fromEntity(entity: Need): NeedFormUpdate =
        NeedFormUpdate(
            entity.title,
            entity.description
        )

    override fun toEntity(domain: NeedFormUpdate): Need {
        val n = Need()
        n.title = domain.title
        n.description = domain.description
        return n
    }
}