package org.kravbank.utils.mapper.need

import org.kravbank.domain.Need
import org.kravbank.utils.form.need.NeedForm
import org.kravbank.utils.mapper.Mapper

class NeedMapper : Mapper<NeedForm, Need> {
    // FROM ENTITY
    override fun fromEntity(entity: Need): NeedForm =
        NeedForm(
            entity.ref,
            entity.title,
            entity.description,
            entity.project
        )

    //TO ENTITY
    override fun toEntity(domain: NeedForm): Need {
        val n = Need()
        n.title = domain.title
        n.description = domain.description
        n.project = domain.project
        return n
    }

}