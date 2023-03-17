package org.kravbank.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.RequirementForm
import org.kravbank.domain.Need
import org.kravbank.domain.Project
import org.kravbank.domain.Requirement
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementRepository
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*


class RequirementServiceTest {

    private val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private val requirementRepository: RequirementRepository = mock(RequirementRepository::class.java)
    private val needRepository: NeedRepository = mock(NeedRepository::class.java)


    private val requirementService = RequirementService(
        requirementRepository = requirementRepository,
        projectRepository = projectRepository,
        needRepository = needRepository
    )


    private val arrangeSetup = TestSetup()


    private lateinit var requirements: List<Requirement>
    private lateinit var requirement: Requirement
    private lateinit var project: Project
    private lateinit var need: Need
    private lateinit var createForm: RequirementForm
    private lateinit var updateForm: RequirementForm


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()


        requirements = arrangeSetup.requirements
        requirement = arrangeSetup.requirement
        project = arrangeSetup.project
        need = arrangeSetup.need
        updateForm = arrangeSetup.updatedRequirementForm
        createForm = RequirementForm().fromEntity(requirement)


        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(requirementRepository.findByRef(project.id, need.id, requirement.ref)).thenReturn(requirement)
        `when`(requirementRepository.listAllRequirements(project.id, need.id)).thenReturn(requirements)
        `when`(needRepository.findByRef(project.id, need.ref)).thenReturn(need)


    }

    @Test
    fun get() {
        val response = requirementService.get(project.ref, need.ref, requirement.ref)

        val entity: Requirement = response

        assertEquals(requirement.title, entity.title)
        assertEquals(requirement.description, entity.description)
        assertEquals(requirement.project, entity.project)
        assertEquals(requirement.need, entity.need)
        assertEquals(requirement.requirementvariants, entity.requirementvariants)

    }

    @Test
    fun list() {
        val response = requirementService.list(project.ref, need.ref)

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
            requirementService.create(project.ref, need.ref, createForm)

        val entity: Requirement = response

        assertNotNull(response)
        assertEquals(createForm.title, entity.title)
        assertEquals(createForm.description, entity.description)

    }

    @Test
    fun delete() {
        requirementService.delete(project.ref, need.ref, requirement.ref)

        verify(requirementRepository).deleteById(requirement.id)
    }


    @Test
    fun update() {
        val response = requirementService.update(
            project.ref,
            need.ref,
            requirement.ref,
            updateForm
        )

        val entity: Requirement = response

        assertNotNull(entity)
        assertEquals(updateForm.title, entity.title)
        assertEquals(updateForm.description, entity.description)
    }
}

