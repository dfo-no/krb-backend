package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.Project
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.project
import org.kravbank.utils.TestSetup.Arrange.projectForm
import org.kravbank.utils.TestSetup.Arrange.projects
import org.kravbank.utils.TestSetup.Arrange.updatedProjectForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class ProjectServiceTest {

    @InjectMock
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var projectService: ProjectService

    private final val arrangeSetup = TestSetup.Arrange

    private final val projectRef: String = arrangeSetup.projectRef

    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }

    @Test
    fun get() {
        Mockito
            .`when`(projectRepository.findByRef(projectRef))
            .thenReturn(project)

        val mockedProject: Project = projectService.get(projectRef)

        Assertions.assertEquals(project.title, mockedProject.title)
        Assertions.assertEquals(project.description, mockedProject.description)
        Assertions.assertEquals(project.ref, mockedProject.ref)
        Assertions.assertEquals(project.codelist, mockedProject.codelist)
        Assertions.assertEquals(project.publications, mockedProject.publications)
        Assertions.assertEquals(project.products, mockedProject.products)
        Assertions.assertEquals(project.needs, mockedProject.needs)
        Assertions.assertEquals(project.requirements, mockedProject.requirements)
    }

    @Test
    fun list() {
        Mockito
            .`when`(projectRepository.listAllProjects())
            .thenReturn(projects)

        val mockedProjects: List<Project> = projectService.list()

        Assertions.assertEquals(projects[0].title, mockedProjects[0].title)
        Assertions.assertEquals(projects[0].description, mockedProjects[0].description)
        Assertions.assertEquals(projects[0].ref, mockedProjects[0].ref)
        Assertions.assertEquals(projects[0].codelist, mockedProjects[0].codelist)
        Assertions.assertEquals(projects[0].publications, mockedProjects[0].publications)
        Assertions.assertEquals(projects[0].products, mockedProjects[0].products)
        Assertions.assertEquals(projects[0].needs, mockedProjects[0].needs)
        Assertions.assertEquals(projects[0].requirements, mockedProjects[0].requirements)
    }

    @Test
    fun create() {
        Mockito
            .doNothing()
            .`when`(projectRepository)
            .persist(ArgumentMatchers.any(Project::class.java))

        Mockito
            .`when`(projectRepository.isPersistent(ArgumentMatchers.any(Project::class.java)))
            .thenReturn(true)

        val form = projectForm

        val mockedProject: Project = projectService.create(form)

        Assertions.assertNotNull(mockedProject)
        Assertions.assertEquals(form.title, mockedProject.title)
        Assertions.assertEquals(form.description, mockedProject.description)
    }

    @Test
    fun delete() {

        //soft delete
        //todo: Kommer tilbake til denne testen senere
        /*
        Mockito
              .`when`(projectRepository.deleteProject(setup.project.id))
              .thenReturn(true)
          Mockito
              .`when`(projectRepository.isPersistent(ArgumentMatchers.any(Project::class.java)))
              .thenReturn(false)

          val mockedProject: Project = projectService.delete(setup.project.ref) //setup.projectref

          //assert
          Assertions.assertNotNull(mockedProject)
          Assertions.assertEquals(setup.project.ref, mockedProject.ref)
          Assertions.assertEquals("første prosjekt", mockedProject.title)
          Assertions.assertEquals("første prosjektbeskrivelse", mockedProject.description)
         */
    }

    @Test
    fun update() {
        Mockito
            .`when`(projectRepository.findByRef(arrangeSetup.project.ref))
            .thenReturn(arrangeSetup.project)

        val form = updatedProjectForm

        val mockedProject: Project = projectService.update(
            projectRef,
            form
        )

        Assertions.assertNotNull(mockedProject)
        Assertions.assertEquals(form.title, mockedProject.title)
        Assertions.assertEquals(form.description, mockedProject.description)
    }
}