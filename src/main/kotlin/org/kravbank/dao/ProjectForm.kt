package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Project

class ProjectForm() {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var ref: String

    lateinit var title: String

    lateinit var description: String

    fun toEntity(domain: ProjectForm): Project = Project().apply {
        title = domain.title
        description = domain.description
    }

    fun fromEntity(entity: Project): ProjectForm = ProjectForm().apply {
        ref = entity.ref
        title = entity.title
        description = entity.description
    }
}
