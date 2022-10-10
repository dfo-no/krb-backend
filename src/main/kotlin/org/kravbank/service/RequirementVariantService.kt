package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.requirementvariant.RequirementVariantForm
import org.kravbank.utils.form.requirementvariant.RequirementVariantFormUpdate
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.utils.mapper.requirementvariant.RequirementVariantMapper
import org.kravbank.utils.mapper.requirementvariant.RequirementVariantUpdateMapper
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class RequirementVariantService(
    val requirementVariantRepository: RequirementVariantRepository,
    val projectService: ProjectService,
    val requirementService: RequirementService,
    val projectRepository: ProjectRepository
) {
    @CacheResult(cacheName = "requirementvariant-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, requirementRef: String, reqVariantRef: String): Response {
        val foundReqVariants =
            projectRepository.findByRef(projectRef).requirements.find { req -> req.ref == requirementRef }
        Optional.ofNullable(foundReqVariants)
            .orElseThrow { NotFoundException("Requirement not found by ref $requirementRef in project by ref $projectRef") }
        val foundRequirementCode =
            foundReqVariants!!.requirementvariants.find { reqVariant -> reqVariant.ref == reqVariantRef }
        Optional.ofNullable(foundRequirementCode)
            .orElseThrow { NotFoundException("Requirement variant not found by ref $reqVariantRef in requirements by ref $requirementRef") }
        val reqVariantFormMapper = RequirementVariantMapper().fromEntity(foundRequirementCode!!)
        return Response.ok(reqVariantFormMapper).build()
    }

    @CacheResult(cacheName = "requirementvariant-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String, requirementRef: String): Response {
        //list reqVariants by requirements ref
        val foundReqVariants =
            projectRepository.findByRef(projectRef).requirements.find { req -> req.ref == requirementRef }
        Optional.ofNullable(foundReqVariants)
            .orElseThrow { NotFoundException("Requirement not found by ref $requirementRef in project by ref $projectRef") }
        val reqVariants = foundReqVariants!!.requirementvariants
        //convert to array of form
        val reqVariantsFormList = ArrayList<RequirementVariantForm>()
        for (reqVariant in reqVariants) reqVariantsFormList.add(RequirementVariantMapper().fromEntity(reqVariant))
        //returns the custom reqVariantForm form
        return Response.ok(reqVariantsFormList).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, requirementRef: String, reqVariantForm: RequirementVariantForm): Response {
        val reqVariantFormMapper = RequirementVariantMapper().toEntity(reqVariantForm)
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = foundProject.requirements.find { requirements -> requirements.ref == requirementRef }!!
        Optional.ofNullable(foundRequirement)
            .orElseThrow { NotFoundException("Did not find requirements by ref $requirementRef in project by ref $projectRef") }
        val added = foundRequirement.requirementvariants.add(reqVariantFormMapper)
        if (added) {
            projectService.updateProject(foundRequirement.id, foundProject)
            return Response.created(URI.create("/api/v1/projects/$projectRef/requirements/$requirementRef/requirementvariants/" + reqVariantForm.ref))
                .build()
        } else throw BadRequestException("Bad request! Did not create requirement variant")
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, requirementRef: String, reqVariantRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = foundProject.requirements.find { requirements -> requirements.ref == requirementRef }!!
        Optional.ofNullable(foundRequirement)
            .orElseThrow { NotFoundException("Did not find requirements by ref $requirementRef in project by ref $projectRef") }
        val foundReqVariant =
            foundRequirement.requirementvariants.find { reqVariant -> reqVariant.ref == reqVariantRef }!!
        Optional.ofNullable(foundReqVariant)
            .orElseThrow { NotFoundException("Did not find requirement variant by ref $reqVariantRef in requirements by ref $requirementRef") }
        val deleted = foundRequirement.requirementvariants.remove(foundReqVariant)
        if (deleted) {
            projectService.updateProject(foundProject.id, foundProject)
            Response.noContent().build()
            return Response.noContent().build()
        } else throw NotFoundException("Bad request! Requirement variant not deleted")
    }

    @Throws(BackendException::class)
    fun update(
        projectRef: String,
        requirementRef: String,
        reqVariantRef: String,
        reqVariantForm: RequirementVariantFormUpdate
    ): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = foundProject.requirements.find { requirements -> requirements.ref == requirementRef }!!
        Optional.ofNullable(foundRequirement)
            .orElseThrow { NotFoundException("Did not find requirements by ref $requirementRef in project by ref $projectRef") }
        val foundReqVariant =
            foundRequirement.requirementvariants.find { reqVariant -> reqVariant.ref == reqVariantRef }!!
        Optional.ofNullable(foundReqVariant)
            .orElseThrow { NotFoundException("Did not find reqVariantForm by ref $reqVariantRef in requirements by ref $requirementRef") }
        val reqVariantFormMapper = RequirementVariantUpdateMapper().toEntity(reqVariantForm)
        requirementVariantRepository.update(
            "description = ?1, requirementtext = ?2, instruction = ?3, useproduct = ?4, usespesification = ?5, usequalification = ?6 where id = ?7",
            reqVariantFormMapper.description,
            reqVariantFormMapper.requirementText,
            reqVariantFormMapper.instruction,
            reqVariantFormMapper.useProduct,
            reqVariantFormMapper.useSpesification,
            reqVariantFormMapper.useQualification,
            foundReqVariant.id
        )
        return Response.ok(reqVariantForm).build()
    }
}