package org.kravbank.utils.mapper.publication

import org.kravbank.domain.Publication
import org.kravbank.utils.form.publication.PublicationForm
import org.kravbank.utils.mapper.Mapper

class PublicationMapper : Mapper<PublicationForm, Publication> {

    // FROM ENTITY
    override fun fromEntity(entity: Publication): PublicationForm =
        PublicationForm(
            entity.ref,
            entity.comment,
            entity.date,
            entity.version,
            //entity.deletedDate,
            //entity.project
        )

    //TO ENTITY
    override fun toEntity(domain: PublicationForm): Publication {
        val p = Publication()
        p.comment = domain.comment
        p.date = domain.date
        p.version = domain.version
        //p.deletedDate = domain.deletedDate
        p.project = domain.project
        return p
    }
}