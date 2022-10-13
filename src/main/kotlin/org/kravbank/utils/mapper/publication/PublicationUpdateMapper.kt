package org.kravbank.utils.mapper.publication

import org.kravbank.domain.Publication
import org.kravbank.utils.form.publication.PublicationFormUpdate
import org.kravbank.utils.mapper.Mapper

class PublicationUpdateMapper : Mapper<PublicationFormUpdate, Publication> {

    // FROM ENTITY
    override fun fromEntity(entity: Publication): PublicationFormUpdate =
        PublicationFormUpdate(
            //entity.ref,
            entity.comment,
            entity.version,
            //entity.deletedDate
        )

    //TO ENTITY
    override fun toEntity(domain: PublicationFormUpdate): Publication {
        val p = Publication()
        p.comment = domain.comment
        p.version = domain.version
       // p.deletedDate = domain.deletedDate
        return p
    }

}