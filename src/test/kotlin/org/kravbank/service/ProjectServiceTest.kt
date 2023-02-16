package org.kravbank.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProjectForm
import org.kravbank.domain.Project
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

class ProjectServiceTest {


    private val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)

    private val projectService = ProjectService(projectRepository)

    private val arrangeSetup = TestSetup()

    private lateinit var projects: List<Project>
    private lateinit var project: Project
    private lateinit var createForm: ProjectForm
    private lateinit var updateForm: ProjectForm


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()


        project = arrangeSetup.project
        projects = arrangeSetup.projects
        updateForm = arrangeSetup.updatedProjectForm
        createForm = ProjectForm().fromEntity(project)


        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(projectRepository.listAllProjects()).thenReturn(projects)

    }

    @Test
    fun get() {
        val response = projectService.get(project.ref)

        val entity: Project = response

        Assertions.assertEquals(project.id, entity.id)
        Assertions.assertEquals(project.title, entity.title)
        Assertions.assertEquals(project.description, entity.description)
        Assertions.assertEquals(project.ref, entity.ref)
        Assertions.assertEquals(project.codelist, entity.codelist)
        Assertions.assertEquals(project.publications, entity.publications)
        Assertions.assertEquals(project.products, entity.products)
        Assertions.assertEquals(project.needs, entity.needs)
        Assertions.assertEquals(project.requirements, entity.requirements)

    }

    @Test
    fun list() {

        val response = projectService.list()

        val entity: List<Project> = response

        Assertions.assertNotNull(response)
        Assertions.assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        Assertions.assertEquals(projects[0].title, firstObjectInList.title)
        Assertions.assertEquals(projects[0].description, firstObjectInList.description)
        Assertions.assertEquals(projects[0].ref, firstObjectInList.ref)
        Assertions.assertEquals(projects[0].codelist, firstObjectInList.codelist)
        Assertions.assertEquals(projects[0].publications, firstObjectInList.publications)
        Assertions.assertEquals(projects[0].products, firstObjectInList.products)
        Assertions.assertEquals(projects[0].needs, firstObjectInList.needs)
        Assertions.assertEquals(projects[0].requirements, firstObjectInList.requirements)
    }

    @Test
    fun create() {
        doNothing()
            .`when`(projectRepository)
            .persist(ArgumentMatchers.any(Project::class.java))

        `when`(projectRepository.isPersistent(ArgumentMatchers.any(Project::class.java)))
            .thenReturn(true)


        val response = projectService.create(createForm)

        val entity: Project = response

        Assertions.assertNotNull(entity)
        Assertions.assertEquals(createForm.title, entity.title)
        Assertions.assertEquals(createForm.description, entity.description)
    }

    @Test
    fun delete() {
        projectService.delete(
            project.ref,
        )

        verify(projectRepository).deleteById(project.id)

    }

    @Test
    fun update() {
        val response = projectService.update(
            project.ref,
            updateForm
        )

        val entity: Project = response

        Assertions.assertNotNull(entity)
        Assertions.assertEquals(updateForm.title, entity.title)
        Assertions.assertEquals(updateForm.description, entity.description)
    }


}