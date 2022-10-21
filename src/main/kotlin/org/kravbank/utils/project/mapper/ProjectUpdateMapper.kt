package org.kravbank.utils.project.mapper

import org.kravbank.domain.Project
import org.kravbank.utils.project.dto.ProjectFormUpdate

class ProjectUpdateMapper : org.kravbank.utils.Mapper<ProjectFormUpdate, Project> {

    override fun fromEntity(entity: Project): ProjectFormUpdate =
        ProjectFormUpdate(
            entity.title,
            entity.description,
        )

    override fun toEntity(domain: ProjectFormUpdate): Project {
        val p = Project()
        p.title = domain.title
        p.description = domain.description
        return p
    }
}








