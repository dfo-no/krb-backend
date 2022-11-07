package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProjectForm
import org.kravbank.domain.*
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.ProjectRepository
import org.kravbank.resource.ProjectResource
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
internal class ProjectResourceMockTest {

    @InjectMock
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var projectResource: ProjectResource

    var project: Project = Project()

    //entity
    var code: Code = Code()
    var publication: Publication = Publication()
    var product: Product = Product()
    var requirement: Requirement = Requirement()
    var need: Need = Need()
    var codelist: Codelist = Codelist()


    //list
    var projects: MutableList<Project> = mutableListOf()
    var codes: MutableList<Code> = mutableListOf()
    var codelists: MutableList<Codelist> = mutableListOf()
    var requirements: MutableList<Requirement> = mutableListOf()
    var needs: MutableList<Need> = mutableListOf()
    var publications: MutableList<Publication> = mutableListOf()
    var products: MutableList<Product> = mutableListOf()

    //arrange
    val projectId = 1L
    val projectRef = "ccc4db69-edb2-431f-855a-4368e2bcddd1"

    // val projectId = 3L
    //val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"

    //val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"

    @BeforeEach
    fun setUp() {
        //arrange
        project.title = "første prosjekt"
        project.description = "første prosjekt beskrivelse"
        project.ref = "ccc5db69-edb2-431f-855a-4368e2bcddd1"
        project.id = 120
        project.codelist = codelists
        project.requirements = requirements
        project.publications = publications
        project.needs = needs
        project.products = products

        code = Code()
        code.title = "Tittel kode"
        code.description = "beskrivelse kode"
        code.codelist = codelist

        codelist = Codelist()
        codelist.title = "Første codelist"
        codelist.description = "første codelist beskrivelse"
        codelist.ref = "hello243567"
        codelist.project = project
        codelist.codes = codes
        codelist.id = (1L)

        product = Product()
        product.project = project
        product.id = 121L
        product.ref = "34352"
        product.title = "prod"
        product.description = "desc"

        need = Need()
        need.ref = "ewdsfsada567"
        need.id = 122L
        need.title = "tittel"
        need.description = "desv"

        publication = Publication()
        requirement = Requirement()
        projects.add(project)
    }


    @Test
    fun getProject_OK() {
        //mock
        Mockito
            .`when`(projectRepository.findByRef(projectRef))
            .thenReturn(project)

        val response: Response = projectResource.getProject(projectRef)
        val entity: Project = ProjectForm()
            .toEntity(response.entity as ProjectForm)

        //assert
        assertEquals("første prosjekt", entity.title)
        assertEquals("første prosjekt beskrivelse", entity.description)
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
    }

    @Test
    fun getProject_KO() {
        //mock
        Mockito
            .`when`(projectRepository.findByRef(projectRef))
            .thenThrow(NotFoundException("Project not found"))

        try {
            projectResource.getProject(projectRef).entity as NotFoundException
        } catch (e: Exception) {
            //assert
            assertEquals("Project not found", e.message)
        }
    }

    @Test
    fun listProjects_OK() {

        //mock
        Mockito.`when`(projectRepository.listAllProjects()).thenReturn(projects)
        val response: Response = projectResource.listProjects()

        //map
        val entity: List<ProjectForm> = response.entity as List<ProjectForm>

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals("første prosjekt", entity[0].title)
        assertEquals("første prosjekt beskrivelse", entity[0].description)
    }

    @Test
    fun createProject_OK() {

        //mock
        Mockito
            .doNothing()
            .`when`(projectRepository).persist(ArgumentMatchers.any(Project::class.java))
        Mockito
            .`when`(projectRepository.isPersistent(ArgumentMatchers.any(Project::class.java)))
            .thenReturn(true)

        val form = ProjectForm().fromEntity(project)
        val response: Response = projectResource.createProject(form)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status);
    }


    @Test
    fun deleteProject_KO() {
        //mock
        Mockito
            .`when`(projectRepository.findByRef(projectRef))
            .thenThrow(NotFoundException("Project not found"))
        try {
            projectResource.deleteProject(projectRef).entity as NotFoundException
        } catch (e: Exception) {
            assertEquals("Project not found", e.message)
        }
    }

    @Test
    fun updateProject_OK() {
        //arrange
        val updateProject = ProjectForm()
        updateProject.title = "Oppdatert tittel"
        updateProject.description = "Oppdatert desc"

        //mock
        Mockito
            .`when`(projectRepository.findByRef(projectRef))
            .thenReturn(project)
        val response: Response = projectResource.updateProject(projectRef, updateProject)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        val entity: Project = ProjectForm().toEntity(response.entity as ProjectForm)
        assertEquals("Oppdatert tittel", entity.title);
    }

    @Test
    fun updateProject_KO() {
        //arrange
        val updateProject = ProjectForm()
        updateProject.title = "Oppdatert tittel"

        //mock
        Mockito.`when`(projectRepository.findByRef(projectRef))
            .thenThrow(NotFoundException("Project not found"))

        try {
            projectResource.updateProject(projectRef, updateProject).entity as NotFoundException
        } catch (e: Exception) {
            //assert
            assertEquals("Project not found", e.message)
        }
    }


    /*
     todo:
          Denne testen er nyttig, men kommer tilbake til den når jeg får løst enten med
          1. mock og assert med bool retur fra repo
          eller
          2. endre fra bool til entity retur fra repo
        @Test
    fun deleteProject_OK() {
        //Mock
        Mockito
            .`when`(projectRepository.deleteProject(projectId))
            .thenReturn(true)

        // Mockito.`when`(projectRepository.isPersistent(ArgumentMatchers.any(Project::class.java))).thenReturn(false)

        val response: Response = projectResource.deleteProject(projectRef)
        val entity: Project = ProjectForm().toEntity(response.entity as ProjectForm)

        //assert
        assertNotNull(entity)
        assertEquals("bbb4db69-edb2-431f-855a-4368e2bcddd1", entity.ref)
    }

    @Test

     Todo:
         KO-testen kan være nyttig for å teste at feilmeldingene som kastes, behandles på riktig måte.
         Kommer tilbake til den når jeg finner ut av hvorfor mocking ikke gir riktig verdi / ikke-null

    fun createProject_KO() {
        assertTrue(false)
    }
     */


}