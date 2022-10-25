package org.kravbank.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.kravbank.domain.Project
import org.kravbank.utils.Mapper

class ProjectForm() : Mapper<ProjectForm, Project> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    lateinit var ref: String

    lateinit var title: String

    lateinit var description: String

    override fun toEntity(domain: ProjectForm): Project = Project().apply {
        title = domain.title
        description = domain.description
    }

    override fun fromEntity(entity: Project): ProjectForm = ProjectForm().apply {
        ref = entity.ref
        title = entity.title
        description = entity.description
    }
}
