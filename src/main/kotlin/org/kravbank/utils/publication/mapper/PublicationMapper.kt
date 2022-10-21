package org.kravbank.utils.publication.mapper

import org.kravbank.domain.Publication
import org.kravbank.utils.publication.dto.PublicationForm
import java.time.LocalDateTime

class PublicationMapper : org.kravbank.utils.Mapper<PublicationForm, Publication> {

    override fun fromEntity(entity: Publication): PublicationForm =
        PublicationForm(
            entity.ref,
            entity.comment,
            entity.date,
            entity.version,
        )

    override fun toEntity(domain: PublicationForm): Publication {
        val p = Publication()
        p.comment = domain.comment
        p.date = LocalDateTime.now()
        p.version = domain.version
        p.project = domain.project
        return p
    }
}