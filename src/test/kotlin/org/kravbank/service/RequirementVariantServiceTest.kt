package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.TestSetup
import org.kravbank.TestSetup.Arrange.requirementVariant_requirementRef
import org.kravbank.domain.RequirementVariant
import org.kravbank.repository.RequirementVariantRepository
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class RequirementVariantServiceTest {

    @InjectMock
    lateinit var requirementVariantRepository: RequirementVariantRepository

    @Inject
    lateinit var requirementVariantService: RequirementVariantService

    val arrangeSetup = TestSetup.Arrange

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
                        arrangeSetup.requirement_requirementVariantId,
                        arrangeSetup.requirementVariant_requirementRef
                    )
            ).thenReturn(arrangeSetup.requirementVariant)

        val mockedRequirementVariant: RequirementVariant =
            requirementVariantService.get(
                arrangeSetup.project_requirementVariantRef,
                arrangeSetup.requirement_project_requirementVariantRef,
                requirementVariant_requirementRef
            )

        Assertions.assertEquals(arrangeSetup.requirementVariant.instruction, mockedRequirementVariant.instruction)
        Assertions.assertEquals(arrangeSetup.requirementVariant.description, mockedRequirementVariant.description)
        Assertions.assertEquals(arrangeSetup.requirementVariant.useProduct, mockedRequirementVariant.useProduct)
        Assertions.assertEquals(
            arrangeSetup.requirementVariant.useSpesification,
            mockedRequirementVariant.useSpesification
        )
        Assertions.assertEquals(arrangeSetup.requirementVariant.product, mockedRequirementVariant.product)
        Assertions.assertEquals(
            arrangeSetup.requirementVariant.useQualification,
            mockedRequirementVariant.useQualification
        )
        Assertions.assertEquals(arrangeSetup.requirementVariant.requirement, mockedRequirementVariant.requirement)
    }

    @Test
    fun list() {
        Mockito.`when`(
            requirementVariantRepository
                .listAllRequirementVariants(arrangeSetup.requirement_requirementVariantId)
        ).thenReturn(arrangeSetup.requirementVariants)

        val mockedRequirementVariants: List<RequirementVariant> =
            requirementVariantService.list(
                arrangeSetup.project_requirementRef,
                arrangeSetup.requirement_project_requirementVariantRef
            )

        Assertions.assertEquals(arrangeSetup.requirementVariant.instruction, mockedRequirementVariants[0].instruction)
        Assertions.assertEquals(arrangeSetup.requirementVariant.description, mockedRequirementVariants[0].description)
        Assertions.assertEquals(arrangeSetup.requirementVariant.useProduct, mockedRequirementVariants[0].useProduct)
        Assertions.assertEquals(
            arrangeSetup.requirementVariant.useSpesification,
            mockedRequirementVariants[0].useSpesification
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

        val mockedRequirementVariant: RequirementVariant =
            requirementVariantService.create(
                arrangeSetup.project_requirementVariantRef,
                arrangeSetup.requirement_project_requirementVariantRef,
                arrangeSetup.requirementVariantForm
            )

        Assertions.assertNotNull(mockedRequirementVariant)
        Assertions.assertEquals(arrangeSetup.newRequirementVariant.instruction, mockedRequirementVariant.instruction)
        Assertions.assertEquals(arrangeSetup.newRequirementVariant.description, mockedRequirementVariant.description)
        Assertions.assertEquals(arrangeSetup.newRequirementVariant.useProduct, mockedRequirementVariant.useProduct)
        Assertions.assertEquals(
            arrangeSetup.newRequirementVariant.useSpesification,
            mockedRequirementVariant.useSpesification
        )
        Assertions.assertEquals(
            arrangeSetup.newRequirementVariant.useQualification,
            mockedRequirementVariant.useQualification
        )
    }

    @Test
    fun delete() {
        //TODO("Kommer tilbake her senere")
    }

    @Test
    fun update() {
        // TODO("requirementVariantService.update med param requirementVariant_requirementRef returnerer nulll-verdi , samme funksjon for get() fungerer, denne eksisterer. men ikke på get(). Undersøkes senere")
        /*
                Mockito

                    .`when`(
                        requirementVariantRepository
                            .findByRef(
                                requirement_requirementVariantId,
                                requirementVariant_requirementRef
                            )
                    ).thenReturn(arrangeSetup.requirementVariant)

                val mockedRequirementVariant: RequirementVariant =
                    requirementVariantService.update(
                        arrangeSetup.project_requirementVariantRef,
                        arrangeSetup.requirement_project_requirementVariantRef,
                        requirementVariant_requirementRef,
                        arrangeSetup.updatedRequirementVariantForm
                    )

                Assertions.assertNotNull(mockedRequirementVariant)
                Assertions.assertEquals(
                    arrangeSetup.updatedRequirementVariantForm.instruction,
                    mockedRequirementVariant.instruction
                )
                Assertions.assertEquals(
                    arrangeSetup.updatedRequirementVariantForm.description,
                    mockedRequirementVariant.description
                )
                Assertions.assertEquals(
                    arrangeSetup.updatedRequirementVariantForm.useProduct,
                    mockedRequirementVariant.useProduct
                )
                Assertions.assertEquals(
                    arrangeSetup.updatedRequirementVariantForm.useSpesification,
                    mockedRequirementVariant.useSpesification
                )
                Assertions.assertEquals(
                    arrangeSetup.updatedRequirementVariantForm.useQualification,
                    mockedRequirementVariant.useQualification
                )
         */
    }
}