package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.Requirement
import org.kravbank.repository.RequirementRepository
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.newRequirement
import org.kravbank.utils.TestSetup.Arrange.requirement
import org.kravbank.utils.TestSetup.Arrange.requirementForm
import org.kravbank.utils.TestSetup.Arrange.requirements
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class RequirementServiceTest {

    @InjectMock
    lateinit var requirementRepository: RequirementRepository

    @Inject
    lateinit var requirementService: RequirementService

    private val arrangeSetup = TestSetup.Arrange

    private val projectId: Long = arrangeSetup.project_requirementId

    private val projectRef: String = arrangeSetup.project_requirementRef

    private val requirementRef: String = arrangeSetup.requirement_projectRef

    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }

    @Test
    fun get() {
        Mockito
            .`when`(
                requirementRepository
                    .findByRef(projectId, requirementRef)
            ).thenReturn(requirement)

        val mockedRequirement: Requirement = requirementService.get(projectRef, requirementRef)

        Assertions.assertEquals(requirement.title, mockedRequirement.title)
        Assertions.assertEquals(requirement.description, mockedRequirement.description)
        Assertions.assertEquals(requirement.project, mockedRequirement.project)
        Assertions.assertEquals(requirement.need, mockedRequirement.need)
        Assertions.assertEquals(requirement.requirementvariants, mockedRequirement.requirementvariants)

    }

    @Test
    fun list() {
        Mockito.`when`(
            requirementRepository
                .listAllRequirements(projectId)
        ).thenReturn(requirements)

        val mockedRequirements: List<Requirement> = requirementService.list(projectRef)

        Assertions.assertEquals(requirements[0].title, mockedRequirements[0].title)
        Assertions.assertEquals(requirements[0].description, mockedRequirements[0].description)
        Assertions.assertEquals(requirements[0].project, mockedRequirements[0].project)
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

        val form = requirementForm

        val mockedRequirement: Requirement =
            requirementService.create(requirement.project!!.ref, form)

        Assertions.assertNotNull(mockedRequirement)
        Assertions.assertEquals(form.title, mockedRequirement.title)
        Assertions.assertEquals(form.description, mockedRequirement.description)

    }

    @Test
    fun delete() {
        Mockito
            .`when`(requirementRepository.deleteRequirement(projectId, requirementRef))
            .thenReturn(newRequirement)

        val mockedRequirement: Requirement = requirementService.delete(projectRef, requirementRef)

        Assertions.assertNotNull(mockedRequirement)
        Assertions.assertEquals(newRequirement.ref, mockedRequirement.ref)
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

