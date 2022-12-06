package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.RequirementVariant
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.requirementVariant
import org.kravbank.utils.TestSetup.Arrange.requirementVariantForm
import org.kravbank.utils.TestSetup.Arrange.requirementVariants
import org.kravbank.utils.TestSetup.Arrange.updatedRequirementVariantForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class RequirementVariantServiceTest {

    @InjectMock
    lateinit var requirementVariantRepository: RequirementVariantRepository

    @Inject
    lateinit var requirementVariantService: RequirementVariantService

    private final val arrangeSetup = TestSetup.Arrange

    private final val requirementId: Long = arrangeSetup.requirement_requirementVariantId

    private final val requirementRef: String = arrangeSetup.requirement_requirementVariantRef

    private final val requirementVariantRef: String = arrangeSetup.requirementVariant_requirementRef

    private final val projectRef: String = arrangeSetup.project_requirementVariantRef

    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }

    @Test
    fun get() {
        Mockito
            .`when`(
                requirementVariantRepository
                    .findByRef(
                        requirementId,
                        requirementVariantRef
                    )
            ).thenReturn(requirementVariant)

        val mockedRequirementVariant: RequirementVariant =
            requirementVariantService.get(
                projectRef,
                arrangeSetup.requirement_requirementVariantRef,
                requirementVariantRef
            )

        Assertions.assertEquals(requirementVariant.instruction, mockedRequirementVariant.instruction)
        Assertions.assertEquals(requirementVariant.description, mockedRequirementVariant.description)
        Assertions.assertEquals(requirementVariant.useProduct, mockedRequirementVariant.useProduct)
        Assertions.assertEquals(
            requirementVariant.useSpecification,
            mockedRequirementVariant.useSpecification
        )
        Assertions.assertEquals(requirementVariant.product, mockedRequirementVariant.product)
        Assertions.assertEquals(
            requirementVariant.useQualification,
            mockedRequirementVariant.useQualification
        )
        Assertions.assertEquals(requirementVariant.requirement, mockedRequirementVariant.requirement)
    }

    @Test
    fun list() {
        Mockito.`when`(
            requirementVariantRepository
                .listAllRequirementVariants(requirementId)
        ).thenReturn(requirementVariants)

        val mockedRequirementVariants: List<RequirementVariant> =
            requirementVariantService.list(
                projectRef,
                arrangeSetup.requirement_requirementVariantRef
            )

        Assertions.assertEquals(arrangeSetup.requirementVariant.instruction, mockedRequirementVariants[0].instruction)
        Assertions.assertEquals(arrangeSetup.requirementVariant.description, mockedRequirementVariants[0].description)
        Assertions.assertEquals(arrangeSetup.requirementVariant.useProduct, mockedRequirementVariants[0].useProduct)
        Assertions.assertEquals(
            arrangeSetup.requirementVariant.useSpecification,
            mockedRequirementVariants[0].useSpecification
        )
        Assertions.assertEquals(arrangeSetup.requirementVariant.product, mockedRequirementVariants[0].product)
        Assertions.assertEquals(
            arrangeSetup.requirementVariant.useQualification,
            mockedRequirementVariants[0].useQualification
        )
    }

    @Test
    fun create() {
        Mockito
            .doNothing()
            .`when`(requirementVariantRepository)
            .persist(ArgumentMatchers.any(RequirementVariant::class.java))

        Mockito
            .`when`(requirementVariantRepository.isPersistent(ArgumentMatchers.any(RequirementVariant::class.java)))
            .thenReturn(true)

        val form = requirementVariantForm

        val mockedRequirementVariant: RequirementVariant =
            requirementVariantService.create(
                projectRef,
                requirementRef,
                form
            )

        Assertions.assertNotNull(mockedRequirementVariant)
        Assertions.assertEquals(form.instruction, mockedRequirementVariant.instruction)
        Assertions.assertEquals(form.description, mockedRequirementVariant.description)
        Assertions.assertEquals(form.useProduct, mockedRequirementVariant.useProduct)
        Assertions.assertEquals(
            form.useSpecification,
            mockedRequirementVariant.useSpecification
        )
        Assertions.assertEquals(
            form.useQualification,
            mockedRequirementVariant.useQualification
        )
    }

    @Test
    fun delete() {
        Mockito
            .`when`(requirementVariantRepository.deleteRequirementVariant(requirementId, requirementVariantRef))
            .thenReturn(requirementVariant)

        val mockedRequirementVariant: RequirementVariant = requirementVariantService.delete(
            projectRef,
            requirementRef,
            requirementVariantRef
        )

        Assertions.assertNotNull(mockedRequirementVariant)
        Assertions.assertEquals(requirementVariant.ref, mockedRequirementVariant.ref)
        Assertions.assertEquals(requirementVariant.requirement, mockedRequirementVariant.requirement)
        Assertions.assertEquals(requirementVariant.requirementText, mockedRequirementVariant.requirementText)
        Assertions.assertEquals(requirementVariant.useProduct, mockedRequirementVariant.useProduct)
        Assertions.assertEquals(requirementVariant.useSpecification, mockedRequirementVariant.useSpecification)
        Assertions.assertEquals(requirementVariant.instruction, mockedRequirementVariant.instruction)
        Assertions.assertEquals(requirementVariant.useQualification, mockedRequirementVariant.useQualification)
        Assertions.assertEquals(requirementVariant.description, mockedRequirementVariant.description)
        Assertions.assertEquals(requirementVariant.product, mockedRequirementVariant.product)

    }

    @Test
    fun update() {
        val form = updatedRequirementVariantForm
        Mockito
            .`when`(
                requirementVariantRepository
                    .findByRef(
                        requirementId,
                        requirementVariantRef
                    )
            ).thenReturn(requirementVariant)

        val mockedRequirementVariant: RequirementVariant =
            requirementVariantService.update(
                projectRef,
                requirementRef,
                requirementVariantRef,
                form
            )

        Assertions.assertNotNull(mockedRequirementVariant)
        Assertions.assertEquals(
            form.instruction,
            mockedRequirementVariant.instruction
        )
        Assertions.assertEquals(
            form.description,
            mockedRequirementVariant.description
        )
        Assertions.assertEquals(
            form.useProduct,
            mockedRequirementVariant.useProduct
        )
        Assertions.assertEquals(
            form.useSpecification,
            mockedRequirementVariant.useSpecification
        )
        Assertions.assertEquals(
            form.useQualification,
            mockedRequirementVariant.useQualification
        )
    }
}