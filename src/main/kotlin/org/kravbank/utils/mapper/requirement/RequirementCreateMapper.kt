package org.kravbank.utils.mapper.requirement

import org.kravbank.domain.Requirement
import org.kravbank.repository.NeedRepository
import org.kravbank.utils.form.requirement.RequirementFormCreate
import org.kravbank.utils.mapper.Mapper

class RequirementCreateMapper() : Mapper<RequirementFormCreate, Requirement> {

    // FROM ENTTY
    override fun fromEntity(entity: Requirement): RequirementFormCreate =
        RequirementFormCreate(
            entity.ref,
            entity.title,
            entity.description,
            entity.project,
           // entity.requirementvariants,
            //entity.need
        )

    // TO ENTITY
    override fun toEntity(domain: RequirementFormCreate): Requirement {
        val r = Requirement()
        r.title = domain.title
        r.description = domain.description
        r.project = domain.project
       // r.
       // val needRepo = NeedRepository()
      //  val foundNeed = needRepo.findByRefRequirement(ref)
       // println("need ref from create mapper $ref")
      //  r.need = foundNeed
        return r
    }
}