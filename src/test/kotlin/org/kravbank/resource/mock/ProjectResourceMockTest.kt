package org.kravbank.resource.mock

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProjectForm
import org.kravbank.domain.Project
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.ProjectRepository
import org.kravbank.resource.ProjectResource
import org.kravbank.service.ProjectService
import org.kravbank.utils.Messages.RepoErrorMsg.PROJECT_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.PROJECT_NOTFOUND
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.ws.rs.core.Response

class ProjectResourceMockTest {

    private val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)

    private val projectService = ProjectService(projectRepository)

    private val arrangeSetup = TestSetup()

    private val projectResource = ProjectResource(projectService)


    private lateinit var projects: List<Project>
    private lateinit var project: Project
    private lateinit var updatedProjectForm: ProjectForm
    private lateinit var createForm: ProjectForm


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()

        updatedProjectForm = arrangeSetup.updatedProjectForm
        project = arrangeSetup.project
        projects = arrangeSetup.projects
        createForm = ProjectForm().fromEntity(project)



        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(projectRepository.listAllProjects()).thenReturn(projects)

    }


    @Test
    fun getProject_OK() {
        val response = projectResource.getProject(project.ref)

        val entity: Project = ProjectForm().toEntity(response)

        assertNotNull(response)
        assertEquals(project.title, entity.title)
        assertEquals(project.description, entity.description)
    }


    @Test
    fun listProjects_OK() {

        val response = projectResource.listProjects()

        val entity: List<ProjectForm> = response

        assertNotNull(response)
        assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(projects[0].title, firstObjectInList.title)
        assertEquals(projects[0].description, firstObjectInList.description)
    }


    @Test
    fun createProject_OK() {
        doNothing()
            .`when`(projectRepository).persist(ArgumentMatchers.any(Project::class.java))

        `when`(projectRepository.isPersistent(ArgumentMatchers.any(Project::class.java)))
            .thenReturn(true)

        val response: Response = projectResource.createProject(createForm)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }


    @Test
    fun updateProject_OK() {
        val response = projectResource.updateProject(project.ref, updatedProjectForm)

        val entity: Project = ProjectForm().toEntity(response)

        assertNotNull(response)
        assertEquals(updatedProjectForm.title, entity.title)
        assertEquals(updatedProjectForm.description, entity.description)
    }


    @Test
    fun deleteProject_OK() {

        val response: Response = projectResource.deleteProject(project.ref)

        assertNotNull(response)
        assertEquals(204, response.status)
        verify(projectRepository).deleteById(project.id)

    }


    /**
     *
     * KO's of relevant exceptions.
     *
     */


    @Test
    fun getProject_KO() {
        `when`(projectRepository.findByRef(project.ref))
            .thenThrow(NotFoundException(PROJECT_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            projectResource.getProject(project.ref)
        }

        assertEquals(PROJECT_NOTFOUND, exception.message)

    }


    @Test
    fun createCodelist_KO() {
        val exception = assertThrows(BadRequestException::class.java) {
            projectResource.createProject(
                createForm,
            )
        }
        assertEquals(PROJECT_BADREQUEST_CREATE, exception.message)
    }


    @Test
    fun updateProject_KO() {
        `when`(projectRepository.findByRef(project.ref))
            .thenThrow(NotFoundException(PROJECT_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            projectResource.updateProject(project.ref, updatedProjectForm)
        }

        assertEquals(PROJECT_NOTFOUND, exception.message)
    }

}

