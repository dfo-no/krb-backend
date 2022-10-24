package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Publication
import java.time.LocalDateTime

class PublicationForm() {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var ref: String

    lateinit var comment: String //private

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var date: LocalDateTime

    var version: Long = 0

    fun toEntity(domain: PublicationForm): Publication = Publication().apply {
        comment = domain.comment
        version = domain.version
    }

    fun fromEntity(entity: Publication): PublicationForm = PublicationForm().apply {
        ref = entity.ref
        comment = entity.comment
        date = entity.date
        version = entity.version
    }
}
