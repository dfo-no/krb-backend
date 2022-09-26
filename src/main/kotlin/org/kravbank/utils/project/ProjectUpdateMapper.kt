package org.kravbank.utils.project

import org.kravbank.domain.Project
import org.kravbank.form.project.ProjectFormUpdate
import org.kravbank.utils.Mapper

class ProjectUpdateMapper : Mapper<ProjectFormUpdate, Project> {

    // FROM ENTTY
    override fun fromEntity(entity: Project): ProjectFormUpdate =
        ProjectFormUpdate(
            entity.title,
            entity.description
        )

    // TO ENTITY
    override fun toEntity(domain: ProjectFormUpdate): Project {
        val p = Project()
        p.title = domain.title
        p.description = domain.description
        return p
    }

}