package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.RequirementForm
import org.kravbank.domain.Project
import org.kravbank.domain.Requirement
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementRepository
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*


@QuarkusTest
internal class RequirementServiceTest {

    private final val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private final val requirementRepository: RequirementRepository = mock(RequirementRepository::class.java)
    private final val needRepository: NeedRepository = mock(NeedRepository::class.java)


    val requirementService = RequirementService(
        requirementRepository = requirementRepository,
        projectRepository = projectRepository,
        needRepository = needRepository
    )


    private val arrangeSetup = TestSetup.Arrange

    private lateinit var createRequirementForm: RequirementForm
    private lateinit var updateRequirementForm: RequirementForm
    private lateinit var requirements: List<Requirement>
    private lateinit var requirement: Requirement
    private lateinit var project: Project


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()

        updateRequirementForm = arrangeSetup.updatedRequirementForm
        createRequirementForm = arrangeSetup.requirementForm
        requirements = arrangeSetup.requirements
        requirement = arrangeSetup.requirement
        project = arrangeSetup.project

        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(requirementRepository.findByRef(project.id, requirement.ref)).thenReturn(requirement)
        `when`(requirementRepository.listAllRequirements(project.id)).thenReturn(requirements)

    }

    @Test
    fun get() {
        val response = requirementService.get(project.ref, requirement.ref)

        val entity: Requirement = response

        assertEquals(requirement.title, entity.title)
        assertEquals(requirement.description, entity.description)
        assertEquals(requirement.project, entity.project)
        assertEquals(requirement.need, entity.need)
        assertEquals(requirement.requirementvariants, entity.requirementvariants)

    }

    @Test
    fun list() {
        val response = requirementService.list(project.ref)

        val entity: List<Requirement> = response

        assertNotNull(response)
        assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(requirements[0].title, firstObjectInList.title)
        assertEquals(requirements[0].description, firstObjectInList.description)
        assertEquals(requirements[0].project, firstObjectInList.project)
    }

    @Test
    fun create() {
        doNothing()
            .`when`(requirementRepository)
            .persist(ArgumentMatchers.any(Requirement::class.java))

        `when`(requirementRepository.isPersistent(ArgumentMatchers.any(Requirement::class.java)))
            .thenReturn(true)

        val response =
            requirementService.create(project.ref, createRequirementForm)

        val entity: Requirement = response

        assertNotNull(response)
        assertEquals(createRequirementForm.title, entity.title)
        assertEquals(createRequirementForm.description, entity.description)

    }

    @Test
    fun delete() {
        requirementService.delete(project.ref, requirement.ref)

        verify(requirementRepository).delete(requirement)
    }


    @Test
    fun update() {
        val response = requirementService.update(
            project.ref,
            requirement.ref,
            updateRequirementForm
        )

        val entity: Requirement = response

        assertNotNull(entity)
        assertEquals(updateRequirementForm.title, entity.title)
        assertEquals(updateRequirementForm.description, entity.description)
    }
}

