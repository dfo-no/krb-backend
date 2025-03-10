package org.kravbank.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.RequirementVariantForm
import org.kravbank.domain.Need
import org.kravbank.domain.Project
import org.kravbank.domain.Requirement
import org.kravbank.domain.RequirementVariant
import org.kravbank.repository.*
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*


class RequirementVariantServiceTest {


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


    private val arrangeSetup = TestSetup()


    private lateinit var requirementVariants: List<RequirementVariant>
    private lateinit var requirementVariant: RequirementVariant
    private lateinit var requirement: Requirement
    private lateinit var project: Project
    private lateinit var need: Need
    private lateinit var createForm: RequirementVariantForm
    private lateinit var updateForm: RequirementVariantForm


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
    fun get() {
        val response =
            requirementVariantService.get(
                project.ref,
                need.ref,
                requirement.ref,
                requirementVariant.ref
            )

        val entity: RequirementVariant = response

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
        assertEquals(requirementVariant.requirement, entity.requirement)
    }

    @Test
    fun list() {
        val response =
            requirementVariantService.list(
                project.ref,
                need.ref,
                requirement.ref
            )

        val entity: List<RequirementVariant> = response

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
    fun create() {
        doNothing()
            .`when`(requirementVariantRepository)
            .persist(ArgumentMatchers.any(RequirementVariant::class.java))

        `when`(requirementVariantRepository.isPersistent(ArgumentMatchers.any(RequirementVariant::class.java)))
            .thenReturn(true)

        val response = requirementVariantService.create(
            project.ref,
            need.ref,
            requirement.ref,
            createForm
        )

        val entity: RequirementVariant = response

        assertNotNull(entity)
        assertEquals(createForm.instruction, entity.instruction)
        assertEquals(createForm.description, entity.description)
        assertEquals(createForm.useProduct, entity.useProduct)
        assertEquals(
            createForm.useSpecification,
            entity.useSpecification
        )
        assertEquals(
            createForm.useQualification,
            entity.useQualification
        )
    }

    @Test
    fun delete() {
        requirementVariantService.delete(
            project.ref,
            need.ref,
            requirement.ref,
            requirementVariant.ref
        )

        verify(requirementVariantRepository).deleteById(requirementVariant.id)

    }

    @Test
    fun update() {
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
}