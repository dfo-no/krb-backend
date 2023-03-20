package org.kravbank.resource.mock

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.RequirementVariantForm
import org.kravbank.domain.Need
import org.kravbank.domain.Project
import org.kravbank.domain.Requirement
import org.kravbank.domain.RequirementVariant
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.*
import org.kravbank.resource.RequirementVariantResource
import org.kravbank.service.RequirementVariantService
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENTVARIANT_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENTVARIANT_NOTFOUND
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.ws.rs.core.Response


class RequirementVariantResourceMockTest {


    private val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private val requirementRepository: RequirementRepository = mock(RequirementRepository::class.java)
    private val requirementVariantRepository: RequirementVariantRepository =
        mock(RequirementVariantRepository::class.java)
    private val productRepository: ProductRepository =
        mock(ProductRepository::class.java)
    private val needRepository: NeedRepository =
        mock(NeedRepository::class.java)

    private val requirementVariantService = RequirementVariantService(
        projectRepository = projectRepository,
        requirementRepository = requirementRepository,
        requirementVariantRepository = requirementVariantRepository,
        productRepository = productRepository,
        needRepository = needRepository
    )

    private val requirementVariantResource = RequirementVariantResource(requirementVariantService)


    private val arrangeSetup = TestSetup()

    private lateinit var requirementVariants: List<RequirementVariant>
    private lateinit var requirementVariant: RequirementVariant
    private lateinit var requirement: Requirement
    private lateinit var project: Project
    private lateinit var need: Need
    private lateinit var updateForm: RequirementVariantForm
    private lateinit var createForm: RequirementVariantForm


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()


        requirementVariants = arrangeSetup.requirementVariants
        requirementVariant = arrangeSetup.requirementVariant
        project = arrangeSetup.project
        need = arrangeSetup.need
        requirement = arrangeSetup.requirement
        updateForm = arrangeSetup.updatedRequirementVariantForm
        createForm = RequirementVariantForm().fromEntity(requirementVariant)



        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(requirementVariantRepository.findByRef(requirement.id, requirementVariant.ref))
            .thenReturn(requirementVariant)
        `when`(requirementRepository.findByRef(project.id, need.id, requirement.ref)).thenReturn(requirement)
        `when`(requirementVariantRepository.listAllRequirementVariants(requirement.id)).thenReturn(requirementVariants)
        `when`(needRepository.findByRef(project.id, need.ref)).thenReturn(need)


    }

    @Test
    fun getRequirementVariant_OK() {
        val response =
            requirementVariantResource.getRequirementVariant(
                project.ref,
                need.ref,
                requirement.ref,
                requirementVariant.ref
            )

        val entity: RequirementVariant = RequirementVariantForm().toEntity(response)

        assertEquals(requirementVariant.instruction, entity.instruction)
        assertEquals(requirementVariant.description, entity.description)
        assertEquals(requirementVariant.useProduct, entity.useProduct)
        assertEquals(
            requirementVariant.useSpecification,
            entity.useSpecification
        )
        assertEquals(
            requirementVariant.useQualification,
            entity.useQualification
        )
    }

    @Test
    fun listRequirementVariant_OK() {
        val response =
            requirementVariantResource.listRequirementVariants(
                project.ref,
                need.ref,
                requirement.ref
            )

        val entity: List<RequirementVariantForm> = response

        assertNotNull(response)
        assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(requirementVariant.instruction, firstObjectInList.instruction)
        assertEquals(requirementVariant.description, firstObjectInList.description)
        assertEquals(requirementVariant.useProduct, firstObjectInList.useProduct)
        assertEquals(
            requirementVariant.useSpecification,
            firstObjectInList.useSpecification
        )
        assertEquals(
            requirementVariant.useQualification,
            firstObjectInList.useQualification
        )
    }

    @Test
    fun createRequirementVariant_OK() {
        doNothing()
            .`when`(requirementVariantRepository)
            .persist(ArgumentMatchers.any(RequirementVariant::class.java))

        `when`(requirementVariantRepository.isPersistent(ArgumentMatchers.any(RequirementVariant::class.java)))
            .thenReturn(true)


        val createForm = RequirementVariantForm().fromEntity(requirementVariant)

        val response = requirementVariantResource.createRequirementVariant(
            project.ref,
            need.ref,
            requirement.ref,
            createForm
        )

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)


    }

    @Test
    fun deleteRequirementVariant_OK() {
        val response: Response = requirementVariantResource.deleteRequirementVariant(
            project.ref,
            need.ref,
            requirement.ref,
            requirementVariant.ref
        )

        assertNotNull(response)
        assertEquals(204, response.status)
        verify(requirementVariantRepository).deleteById(requirementVariant.id)
    }


    @Test
    fun updateRequirementVariant_OK() {
        val response =
            requirementVariantService.update(
                project.ref,
                need.ref,
                requirement.ref,
                requirementVariant.ref,
                updateForm
            )

        val entity: RequirementVariant = response


        assertNotNull(entity)
        assertEquals(
            updateForm.instruction,
            entity.instruction
        )
        assertEquals(
            updateForm.description,
            entity.description
        )
        assertEquals(
            updateForm.useProduct,
            entity.useProduct
        )
        assertEquals(
            updateForm.useSpecification,
            entity.useSpecification
        )
        assertEquals(
            updateForm.useQualification,
            entity.useQualification
        )
    }


    /**
     *
     * KO's of relevant exceptions.
     *
     */


    @Test
    fun getRequirementVariant_KO() {
        `when`(requirementVariantRepository.findByRef(requirement.id, requirementVariant.ref))
            .thenThrow(NotFoundException(REQUIREMENTVARIANT_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            requirementVariantResource.getRequirementVariant(
                project.ref,
                need.ref,
                requirement.ref,
                requirementVariant.ref

            )
        }
        assertEquals(REQUIREMENTVARIANT_NOTFOUND, exception.message)
    }


    @Test
    fun createRequirementVariant_KO() {
        val exception = assertThrows(BadRequestException::class.java) {
            requirementVariantResource.createRequirementVariant(
                project.ref,
                need.ref,
                requirement.ref,
                createForm,
            )
        }

        assertEquals(REQUIREMENTVARIANT_BADREQUEST_CREATE, exception.message)
    }


    @Test
    fun updateRequirementVariant_KO() {
        `when`(requirementVariantRepository.findByRef(requirement.id, requirementVariant.ref))
            .thenThrow(NotFoundException(REQUIREMENTVARIANT_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            requirementVariantResource.updateRequirementVariant(
                project.ref,
                need.ref,
                requirement.ref,
                requirementVariant.ref,
                updateForm
            )
        }
        assertEquals(REQUIREMENTVARIANT_NOTFOUND, exception.message)

    }
}