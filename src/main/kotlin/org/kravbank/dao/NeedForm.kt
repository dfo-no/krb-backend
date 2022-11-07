package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Need
import org.kravbank.utils.Mapper

class NeedForm(): Mapper<NeedForm, Need> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var ref: String

    lateinit var title: String

    lateinit var description: String

    override fun toEntity(domain: NeedForm): Need = Need().apply {
        title = domain.title
        description = domain.description
    }

    override fun fromEntity(entity: Need): NeedForm = NeedForm().apply {
        ref = entity.ref
        title = entity.title
        description = entity.description
    }
}