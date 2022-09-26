package org.kravbank.utils.publication

import org.kravbank.domain.Publication
import org.kravbank.form.publication.PublicationFormUpdate
import org.kravbank.utils.Mapper

class PublicationUpdateMapper : Mapper<PublicationFormUpdate, Publication> {

    // FROM ENTITY
    override fun fromEntity(entity: Publication): PublicationFormUpdate =
        PublicationFormUpdate(
            //entity.ref,
            entity.comment,
            entity.deletedDate
        )

    //TO ENTITY
    override fun toEntity(domain: PublicationFormUpdate): Publication {
        val p = Publication()
        p.comment = domain.comment
        p.deletedDate = domain.deletedDate
        return p
    }

}