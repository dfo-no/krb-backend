package org.kravbank.utils.mapper.project

import org.kravbank.domain.Project
import org.kravbank.utils.form.project.ProjectForm
import org.kravbank.utils.mapper.Mapper

class ProjectMapper :
    org.kravbank.utils.mapper.Mapper<ProjectForm, Project> {

    // FROM ENTITY
    override fun fromEntity(entity: Project): ProjectForm =
        ProjectForm(
            entity.ref,
            entity.title,
            entity.description,
            entity.version,
            entity.publishedDate,
            entity.deletedDate
        )


    //TO ENTITY
    override fun toEntity(domain: ProjectForm): Project {
      val p = Project()
        p.title = domain.title
        p.description = domain.description
        p.version = domain.version
        p.publishedDate = domain.publishedDate
        p.deletedDate = domain.deletedDate
        return p
    }



}