package org.kravbank.resource.mock

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.RequirementForm
import org.kravbank.domain.Project
import org.kravbank.domain.Requirement
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementRepository
import org.kravbank.resource.RequirementResource
import org.kravbank.service.RequirementService
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENT_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENT_NOTFOUND
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.ws.rs.core.Response


class RequirementResourceMockTest {


    private val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private val requirementRepository: RequirementRepository = mock(RequirementRepository::class.java)
    private val needRepository: NeedRepository = mock(NeedRepository::class.java)

    private val requirementService = RequirementService(
        requirementRepository = requirementRepository,
        projectRepository = projectRepository,
        needRepository = needRepository
    )

    private val requirementResource = RequirementResource(requirementService)


    private val arrangeSetup = TestSetup()

    private lateinit var requirements: List<Requirement>
    private lateinit var requirement: Requirement
    private lateinit var project: Project
    private lateinit var updateForm: RequirementForm
    private lateinit var createForm: RequirementForm


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()


        updateForm = arrangeSetup.updatedRequirementForm
        requirements = arrangeSetup.requirements
        requirement = arrangeSetup.requirement
        project = arrangeSetup.project
        createForm = RequirementForm().fromEntity(requirement)


        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(requirementRepository.findByRef(project.id, requirement.ref)).thenReturn(requirement)
        `when`(requirementRepository.listAllRequirements(project.id)).thenReturn(requirements)

    }


    @Test
    fun getRequirement_OK() {
        val response = requirementResource.getRequirement(project.ref, requirement.ref)

        val entity: Requirement = RequirementForm().toEntity(response)

        assertNotNull(response)
        assertEquals(requirement.title, entity.title)
        assertEquals(requirement.description, entity.description)
    }


    @Test
    fun listRequirements_OK() {
        val response = requirementResource.listRequirements(project.ref)

        val entity: List<RequirementForm> = response

        assertNotNull(response)
        assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(requirements[0].title, firstObjectInList.title)
        assertEquals(requirements[0].description, firstObjectInList.description)
    }


    @Test
    fun createRequirement_OK() {
        doNothing()
            .`when`(requirementRepository).persist(ArgumentMatchers.any(Requirement::class.java))

        `when`(requirementRepository.isPersistent(ArgumentMatchers.any(Requirement::class.java)))
            .thenReturn(true)

        val response = requirementResource.createRequirement(project.ref, createForm)

        val entity: Response = response

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, entity.status)
    }

    @Test
    fun deleteRequirement_OK() {
        val response = requirementResource.deleteRequirement(project.ref, requirement.ref)

        assertNotNull(response)
        assertEquals(204, response.status)
        verify(requirementRepository).deleteById(requirement.id)

    }

    @Test
    fun updateRequirement_OK() {
        val response = requirementResource.updateRequirement(project.ref, requirement.ref, updateForm)

        val entity: Requirement = RequirementForm().toEntity(response)

        assertNotNull(response)
        assertEquals(updateForm.title, entity.title)
        assertEquals(updateForm.description, entity.description)
    }


    /**
     *
     * KO's of relevant exceptions.
     *
     */


    @Test
    fun getRequirement_KO() {
        `when`(requirementRepository.findByRef(project.id, requirement.ref))
            .thenThrow(NotFoundException(REQUIREMENT_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            requirementResource.getRequirement(
                project.ref,
                requirement.ref
            )
        }
        assertEquals(REQUIREMENT_NOTFOUND, exception.message)
    }


    @Test
    fun createRequirement_KO() {
        val exception = assertThrows(BadRequestException::class.java) {
            requirementResource.createRequirement(
                project.ref,
                createForm,
            )
        }

        assertEquals(REQUIREMENT_BADREQUEST_CREATE, exception.message)
    }


    @Test
    fun updateRequirement_KO() {
        `when`(requirementRepository.findByRef(project.id, requirement.ref))
            .thenThrow(NotFoundException(REQUIREMENT_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            requirementResource.updateRequirement(
                project.ref,
                requirement.ref,
                updateForm
            )
        }
        assertEquals(REQUIREMENT_NOTFOUND, exception.message)

    }
}