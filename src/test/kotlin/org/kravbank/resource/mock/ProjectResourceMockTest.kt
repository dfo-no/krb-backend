package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProjectForm
import org.kravbank.domain.Project
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.ProjectRepository
import org.kravbank.resource.ProjectResource
import org.kravbank.utils.Messages.RepoErrorMsg.PROJECT_NOTFOUND
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.project
import org.kravbank.utils.TestSetup.Arrange.projectForm
import org.kravbank.utils.TestSetup.Arrange.projects
import org.kravbank.utils.TestSetup.Arrange.updatedProjectForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class ProjectResourceMockTest {

    @InjectMock
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var projectResource: ProjectResource

    private final val arrangeSetup = TestSetup.Arrange

    private val projectRef: String = arrangeSetup.projectRef

    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }

    @Test
    fun getProject_OK() {
        Mockito
            .`when`(projectRepository.findByRef(projectRef))
            .thenReturn(project)

        val response = projectResource.getProject(projectRef)

        val entity: Project = ProjectForm()
            .toEntity(response)

        assertEquals(project.title, entity.title)
        assertEquals(project.description, entity.description)
        assertNotNull(response)
    }

    @Test
    fun getProject_KO() {
        Mockito
            .`when`(projectRepository.findByRef(projectRef))
            .thenThrow(NotFoundException(PROJECT_NOTFOUND))

        try {

            projectResource.getProject(projectRef)

        } catch (e: Exception) {
            assertEquals(PROJECT_NOTFOUND, e.message)
        }
    }

    @Test
    fun listProjects_OK() {
        Mockito
            .`when`(projectRepository.listAllProjects())
            .thenReturn(projects)

        val response = projectResource.listProjects()

        assertNotNull(response)
        assertFalse(response.isEmpty())
        assertEquals(projects[0].title, response[0].title)
        assertEquals(projects[0].description, response[0].description)
    }

    @Test
    fun createProject_OK() {
        Mockito
            .doNothing()
            .`when`(projectRepository).persist(ArgumentMatchers.any(Project::class.java))

        Mockito
            .`when`(projectRepository.isPersistent(ArgumentMatchers.any(Project::class.java)))
            .thenReturn(true)

        val form = projectForm

        val response: Response = projectResource.createProject(form)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun deleteProject_KO() {
        Mockito
            .`when`(projectRepository.findByRef(projectRef))
            .thenThrow(NotFoundException(PROJECT_NOTFOUND))
        try {
            projectResource.deleteProject(projectRef)
        } catch (e: Exception) {
            assertEquals(PROJECT_NOTFOUND, e.message)
        }
    }

    @Test
    fun updateProject_OK() {
        Mockito
            .`when`(projectRepository.findByRef(projectRef))
            .thenReturn(project)

        val form = updatedProjectForm

        val response = projectResource.updateProject(projectRef, form)


        assertNotNull(response)
        assertEquals(form.title, response.title)
        assertEquals(form.description, response.description)
    }

    @Test
    fun updateProject_KO() {
        Mockito.`when`(projectRepository.findByRef(projectRef))
            .thenThrow(NotFoundException(PROJECT_NOTFOUND))

        val form = updatedProjectForm

        try {
            projectResource.updateProject(projectRef, form)
        } catch (e: Exception) {
            assertEquals(PROJECT_NOTFOUND, e.message)
        }
    }

    /*
    TODO("fiks delete test")
    @Test
    fun deleteProject_OK() {
        //Mock
        Mockito
            .`when`(projectRepository.deleteProject(projectId))
            .thenReturn(true)

        // Mockito.`when`(projectRepository.isPersistent(ArgumentMatchers.any(Project::class.java))).thenReturn(false)

        val response: Response = projectResource.deleteProject(projectRef)
        val entity: Project = ProjectForm().toEntity(response)

        //assert
        assertNotNull(entity)
        assertEquals("bbb4db69-edb2-431f-855a-4368e2bcddd1", entity.ref)
    }
     */
}

