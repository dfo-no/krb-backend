package org.kravbank.utils.publication.mapper

import org.kravbank.domain.Publication
import org.kravbank.utils.publication.dto.PublicationFormUpdate

class PublicationUpdateMapper :
    org.kravbank.utils.Mapper<PublicationFormUpdate, Publication> {

    override fun fromEntity(entity: Publication): PublicationFormUpdate =
        PublicationFormUpdate(
            entity.comment,
        )
    override fun toEntity(domain: PublicationFormUpdate): Publication {
        val p = Publication()
        p.comment = domain.comment
        return p
    }
}