package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Requirement
import org.kravbank.utils.Mapper

class RequirementForm() : Mapper<RequirementForm, Requirement> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
     var ref: String = ""

    lateinit var title: String

    lateinit var description: String

   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var needRef: String = ""


    override fun toEntity(domain: RequirementForm): Requirement = Requirement().apply {
        title = domain.title
        description = domain.description
    }

    override fun fromEntity(entity: Requirement): RequirementForm = RequirementForm().apply {
        ref = entity.ref
        title = entity.title
        description = entity.description
    }
}
