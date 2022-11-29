package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.TestSetup
import org.kravbank.domain.Project
import org.kravbank.repository.ProjectRepository
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class ProjectServiceTest {

    @InjectMock
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var projectService: ProjectService

    val setup = TestSetup.SetDomains

    @BeforeEach
    fun setUp() {
        setup.arrange()
    }

    @Test
    fun get() {
        Mockito
            .`when`(projectRepository.findByRef(setup.project.ref))
            .thenReturn(setup.project)

        val mockedProject: Project = projectService.get(setup.project.ref)

        Assertions.assertEquals("første prosjekt", mockedProject.title)
        Assertions.assertEquals("første prosjektbeskrivelse", mockedProject.description)
        Assertions.assertEquals(setup.project.ref, mockedProject.ref)
        Assertions.assertEquals(setup.project.codelist, mockedProject.codelist)
        Assertions.assertEquals(setup.project.publications, mockedProject.publications)
        Assertions.assertEquals(setup.project.products, mockedProject.products)
        Assertions.assertEquals(setup.project.needs, mockedProject.needs)
        Assertions.assertEquals(setup.project.requirements, mockedProject.requirements)

    }

    @Test
    fun list() {
        Mockito.`when`(projectRepository.listAllProjects()).thenReturn(setup.projects)

        val mockedProjects: List<Project> = projectService.list()

        Assertions.assertEquals("første prosjekt", mockedProjects[0].title)
        Assertions.assertEquals("første prosjektbeskrivelse", mockedProjects[0].description)
        Assertions.assertEquals(setup.project.ref, mockedProjects[0].ref)
        Assertions.assertEquals(setup.project.codelist, mockedProjects[0].codelist)
        Assertions.assertEquals(setup.project.publications, mockedProjects[0].publications)
        Assertions.assertEquals(setup.project.products, mockedProjects[0].products)
        Assertions.assertEquals(setup.project.needs, mockedProjects[0].needs)
        Assertions.assertEquals(setup.project.requirements, mockedProjects[0].requirements)
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

        val mockedProject: Project = projectService.create(setup.projectForm)

        Assertions.assertNotNull(mockedProject)
        Assertions.assertEquals("andre prosjekt", mockedProject.title)
        Assertions.assertEquals("andre prosjektbeskrivelse", mockedProject.description)
        // todo: ref endres for hver testkjøring - se autogen domain
        //  Assertions.assertEquals(setup.project.ref, mockedProject.ref)
        Assertions.assertEquals(setup.project.codelist, mockedProject.codelist)
        Assertions.assertEquals(setup.project.publications, mockedProject.publications)
        Assertions.assertEquals(setup.project.products, mockedProject.products)
        Assertions.assertEquals(setup.project.needs, mockedProject.needs)
        Assertions.assertEquals(setup.project.requirements, mockedProject.requirements)
    }

    @Test
    fun delete() {

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
            .`when`(projectRepository.findByRef(setup.project.ref))
            .thenReturn(setup.project)

        val mockedProject: Project = projectService.update(setup.project.ref, setup.updatedProjectForm)

        Assertions.assertNotNull(mockedProject)
        Assertions.assertEquals("Oppdatert tittel", mockedProject.title)
        Assertions.assertEquals("Oppdatert beskrivelse", mockedProject.description)
        Assertions.assertEquals(setup.project.codelist, mockedProject.codelist)
        Assertions.assertEquals(setup.project.publications, mockedProject.publications)
        Assertions.assertEquals(setup.project.products, mockedProject.products)
        Assertions.assertEquals(setup.project.needs, mockedProject.needs)
        Assertions.assertEquals(setup.project.requirements, mockedProject.requirements)
    }
}