package org.kravbank.utils.need

import org.kravbank.domain.Need
import org.kravbank.form.need.NeedForm
import org.kravbank.utils.Mapper

class NeedMapper : Mapper<NeedForm, Need> {
    // FROM ENTITY
    override fun fromEntity(entity: Need): NeedForm =
        NeedForm(
            entity.ref,
            entity.title,
            entity.description,
        )

    //TO ENTITY
    override fun toEntity(domain: NeedForm): Need {
        val n = Need()
        n.title = domain.title
        n.description = domain.description
        return n
    }

}