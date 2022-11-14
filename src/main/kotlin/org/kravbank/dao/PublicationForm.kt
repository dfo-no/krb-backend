package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Publication
import org.kravbank.utils.Mapper
import java.time.LocalDateTime

class PublicationForm() : Mapper<PublicationForm, Publication> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var ref: String

    lateinit var comment: String

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var date: LocalDateTime

    var version: Long = 0

    override fun toEntity(domain: PublicationForm): Publication = Publication().apply {
        comment = domain.comment
        version = domain.version
    }

    override fun fromEntity(entity: Publication): PublicationForm = PublicationForm().apply {
        ref = entity.ref
        comment = entity.comment
        date = entity.date
        version = entity.version
    }
}
