package org.kravbank.utils.project.mapper

import org.kravbank.domain.Project
import org.kravbank.utils.project.dto.ProjectForm

class ProjectMapper : org.kravbank.utils.Mapper<ProjectForm, Project> {
    override fun fromEntity(entity: Project): ProjectForm =
        ProjectForm(
            entity.ref,
            entity.title,
            entity.description,
        )

    override fun toEntity(domain: ProjectForm): Project {
        val p = Project()
        p.title = domain.title
        p.description = domain.description
        return p
    }
}