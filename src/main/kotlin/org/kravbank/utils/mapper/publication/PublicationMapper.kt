package org.kravbank.utils.mapper.publication

import org.kravbank.domain.Publication
import org.kravbank.utils.form.publication.PublicationForm
import org.kravbank.utils.mapper.Mapper

class PublicationMapper :
    org.kravbank.utils.mapper.Mapper<PublicationForm, Publication> {

    // FROM ENTITY
    override fun fromEntity(entity: Publication): PublicationForm =
        PublicationForm(
            entity.ref,
            entity.comment,
            entity.deletedDate
        )

    //TO ENTITY
    override fun toEntity(domain: PublicationForm): Publication {
      val p = Publication()
        p.comment = domain.comment
        p.deletedDate = domain.deletedDate
        return p
    }
}