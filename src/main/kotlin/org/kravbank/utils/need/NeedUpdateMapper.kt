package org.kravbank.utils.need

import org.kravbank.domain.Need
import org.kravbank.form.need.NeedFormUpdate
import org.kravbank.utils.Mapper

class NeedUpdateMapper : Mapper<NeedFormUpdate, Need> {

    // FROM ENTTY
    override fun fromEntity(entity: Need): NeedFormUpdate =
        NeedFormUpdate(
            entity.title,
            entity.description
        )

    // TO ENTITY
    override fun toEntity(domain: NeedFormUpdate): Need {
        val n = Need()
        n.title = domain.title
        n.description = domain.description
        return n
    }

}