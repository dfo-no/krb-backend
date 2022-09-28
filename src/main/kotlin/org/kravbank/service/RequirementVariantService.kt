package org.kravbank.service

import org.kravbank.domain.RequirementVariant
import org.kravbank.form.requirementvariant.RequirementVariantForm
import org.kravbank.form.requirementvariant.RequirementVariantFormUpdate
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.utils.requirementvariant.RequirementVariantMapper
import org.kravbank.utils.requirementvariant.RequirementVariantUpdateMapper
import java.lang.IllegalArgumentException
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class RequirementVariantService(val requirementVariantRepository: RequirementVariantRepository, val projectService: ProjectService, val requirementService: RequirementService) {

    fun getRequirementVariantByRefFromService(projectRef: String, requirementRef: String, requirementVariantRef: String): Response {
        if (projectService.refExists(projectRef) && refExists(requirementVariantRef)) {
            val project = projectService.getProjectByRefCustomRepo(projectRef)!!

            /**
             * todo
             * fix here
             *
             */
            val requirement = project.requirements.find {
                    requirement -> requirement.ref == requirementRef
            }
            val reqVariant = requirement!!.requirementvariants.find {
                    variant -> variant.ref ==  requirementVariantRef
            }

            val requirementVariantMapper = RequirementVariantMapper().fromEntity(reqVariant!!)
            return Response.ok(requirementVariantMapper).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    fun listRequirementVariantsFromService(projectRef: String, requirementRef: String): Response {
        return try {
            if (projectService.refExists(projectRef)) {
                //list requirementVariants by project ref
               val projectFromRef = projectService.getProjectByRefCustomRepo(projectRef)!!

               // val requirement = requirementService.getRequirementByRefFromService(projectRef, requirementRef)!!.
                val requirementVariantList = projectFromRef.requirements.find { requirement -> requirement.ref == requirementRef }!!.requirementvariants

                //val reqVariant = requirement!!.requirementvariants.find { variant -> variant.ref ==  }
               // if (requirement!!.isPersistent) {

                //}
                //convert to array of form
                val requirementVariantsFormList = ArrayList<RequirementVariantForm>()
                for (rv in requirementVariantList) requirementVariantsFormList.add(RequirementVariantMapper().fromEntity(rv))

                //returns the custom requirementVariant form
                Response.ok(requirementVariantsFormList).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("GET list of requirementVariants failed")
        }
    }

    fun createRequirementVariantFromService(projectRef: String, requirementRef: String, requirementVariant: RequirementVariantForm): Response {
        //adds a requirementVariant to relevant project
        try {
            val requirementVariantMapper = RequirementVariantMapper().toEntity(requirementVariant)
            if (projectService.refExists(projectRef)) {
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                val requirementVariantList = project.requirements.find { requirement -> requirement.ref == requirementRef }!!.requirementvariants
                requirementVariantList.add(requirementVariantMapper)
                projectService.updateProject(project.id, project)
            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
            return if (requirementVariantMapper.isPersistent) {
                Response.created(URI.create("/api/v1/projects/$projectRef/requirements/$requirementRef/requirementvariants/" + requirementVariant.ref)).build();
            } else {
                Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("POST requirementVariant failed")
        }
    }

    fun deleteRequirementVariantFromService(projectRef: String, requirementVariantRef: String): Response {
        return try {
            //val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            //val requirementVariant = getRequirementVariantByRefFromService(projectRef, requirementVariantRef)

            if (projectService.refExists(projectRef) && refExists(requirementVariantRef)) {
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
              //  val requirementVariant = project.requirementVariants.find { requirementVariant -> requirementVariant.ref == requirementVariantRef }
               // val deleted = deleteRequirementVariant(requirementVariant!!.id)
              //  if (deleted) {
                  //  project.requirementVariants.remove(requirementVariant)
                    Response.noContent().build()
               // } else {
                   Response.status(Response.Status.BAD_REQUEST).build()
                //}
            } else
                Response.status(Response.Status.NOT_FOUND).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("DELETE requirementVariant failed!")
        }
    }

    fun updateRequirementVariantFromService(
        projectRef: String,
        requirementVariantRef: String,
        requirementVariant: RequirementVariantFormUpdate
    ): Response {
        if (projectService.refExists(projectRef) && refExists(requirementVariantRef)) {
            // if requirementVariant exists in this project
            val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            //val foundProduct = getProductByRefCustomRepo(requirementVariantRef)
           // val requirementVariantInProject = project.requirementVariants.find { pub -> pub.ref == requirementVariantRef }
            val requirementVariantMapper = RequirementVariantUpdateMapper().toEntity(requirementVariant)

            //if (requirementVariant.project.ref == project.ref)
/*
            return if (requirementVariantInProject != null) {
                requirementVariantRepository.update(
                    "title = ?1, description = ?2 where id= ?3",
                    requirementVariantMapper.title,
                    requirementVariantMapper.description,
                    requirementVariantInProject.id
                )

 */
                Response.ok(requirementVariant).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
       // } else {

         //   return Response.status(Response.Status.NOT_FOUND).build()
       // }

        return Response.ok().build()
    }




    fun listRequirementVariants(): MutableList<RequirementVariant> = requirementVariantRepository.listAll()

    fun getRequirementVariant(id: Long): RequirementVariant = requirementVariantRepository.findById(id)

    fun createRequirementVariant(requirementVariant: RequirementVariant) = requirementVariantRepository.persistAndFlush(requirementVariant)

    fun exists(id: Long): Boolean = requirementVariantRepository.count("id", id) == 1L

    fun refExists(ref: String): Boolean = requirementVariantRepository.count("ref", ref) == 1L


    fun deleteRequirementVariant(id: Long) = requirementVariantRepository.deleteById(id)

    fun updateRequirementVariant(id: Long, requirementVariant: RequirementVariant) {
       // requirementVariantRepository.update(
          //  "comment = ?1, version = ?2, bankid = ?3, date = ?4 where id= ?5",
            // requirementVariant.comment, requirementVariant.version, requirementVariant.bankId, requirementVariant.date, id
        //)

    }

}