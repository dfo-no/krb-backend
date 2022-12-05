package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.TestSetup
import org.kravbank.domain.Requirement
import org.kravbank.repository.RequirementRepository
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class RequirementServiceTest {

    @InjectMock
    lateinit var requirementRepository: RequirementRepository

    @Inject
    lateinit var requirementService: RequirementService

    val arrangeSetup = TestSetup.Arrange

    @BeforeEach
    fun setUp() {
        arrangeSetup.start()
    }

    @Test
    fun get() {
        Mockito
            .`when`(
                requirementRepository
                    .findByRef(arrangeSetup.project_requirementId, arrangeSetup.requirement_projectRef)
            ).thenReturn(arrangeSetup.requirement)

        val mockedRequirement: Requirement =
            requirementService.get(arrangeSetup.project_requirementRef, arrangeSetup.requirement_projectRef)

        Assertions.assertEquals(arrangeSetup.requirement.title, mockedRequirement.title)
        Assertions.assertEquals(arrangeSetup.requirement.description, mockedRequirement.description)
        Assertions.assertEquals(arrangeSetup.requirement.project, mockedRequirement.project)
        Assertions.assertEquals(arrangeSetup.requirement.need, mockedRequirement.need)
        Assertions.assertEquals(arrangeSetup.requirement.requirementvariants, mockedRequirement.requirementvariants)

    }

    @Test
    fun list() {
        Mockito.`when`(
            requirementRepository
                .listAllRequirements(arrangeSetup.project_requirementId)
        ).thenReturn(arrangeSetup.requirements)

        val mockedRequirements: List<Requirement> = requirementService.list(arrangeSetup.project_requirementRef)

        Assertions.assertEquals(arrangeSetup.requirement.title, mockedRequirements[0].title)
        Assertions.assertEquals(arrangeSetup.requirement.description, mockedRequirements[0].description)
        Assertions.assertEquals(arrangeSetup.requirement.project, mockedRequirements[0].project)
    }

    @Test
    fun create() {
        Mockito
            .doNothing()
            .`when`(requirementRepository)
            .persist(ArgumentMatchers.any(Requirement::class.java))
        Mockito
            .`when`(requirementRepository.isPersistent(ArgumentMatchers.any(Requirement::class.java)))
            .thenReturn(true)

        val mockedRequirement: Requirement =
            requirementService.create(arrangeSetup.requirement.project!!.ref, arrangeSetup.requirementForm)

        Assertions.assertNotNull(mockedRequirement)
        Assertions.assertEquals(arrangeSetup.newRequirement.title, mockedRequirement.title)
        Assertions.assertEquals(arrangeSetup.newRequirement.description, mockedRequirement.description)
    }

    @Test
    fun delete() {
        //TODO("Kommer tilbake her senere")
    }

    @Test
    fun update() {
        Mockito
            .`when`(
                requirementRepository
                    .findByRef(arrangeSetup.project_requirementId, arrangeSetup.requirement_projectRef)
            ).thenReturn(arrangeSetup.requirement)

        val mockedRequirement: Requirement = requirementService.update(
            arrangeSetup.project_requirementRef,
            arrangeSetup.requirement_projectRef,
            arrangeSetup.updatedRequirementForm
        )

        Assertions.assertNotNull(mockedRequirement)
        Assertions.assertEquals(arrangeSetup.updatedRequirementForm.title, mockedRequirement.title)
        Assertions.assertEquals(arrangeSetup.updatedRequirementForm.description, mockedRequirement.description)
    }
}

