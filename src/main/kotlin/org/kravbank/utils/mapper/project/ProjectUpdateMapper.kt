package org.kravbank.utils.mapper.project

import org.kravbank.domain.Project
import org.kravbank.utils.form.project.ProjectFormUpdate
import org.kravbank.utils.mapper.Mapper

class ProjectUpdateMapper : Mapper<ProjectFormUpdate, Project> {

    // FROM ENTTY
    override fun fromEntity(entity: Project): ProjectFormUpdate =
        ProjectFormUpdate(
            entity.title,
            entity.description,
           // entity.version,
            //entity.publishedDate,
            //entity.deletedDate
        )

    // TO ENTITY
    override fun toEntity(domain: ProjectFormUpdate): Project {
        val p = Project()
        p.title = domain.title
        p.description = domain.description
     //   p.publishedDate = domain.publishedDate
       // p.version = domain.version
       // p.deletedDate = domain.deletedDate
        return p
    }
}








