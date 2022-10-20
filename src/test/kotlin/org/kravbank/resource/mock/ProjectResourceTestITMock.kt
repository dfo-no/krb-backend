package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.*
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException

import org.kravbank.resource.ProjectResource
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.form.codelist.CodelistFormUpdate
import org.kravbank.utils.form.project.ProjectForm
import org.kravbank.utils.form.project.ProjectFormUpdate
import org.kravbank.utils.mapper.codelist.CodelistMapper
import org.kravbank.utils.mapper.codelist.CodelistUpdateMapper
import org.kravbank.utils.mapper.project.ProjectMapper
import org.kravbank.utils.mapper.project.ProjectUpdateMapper
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
internal class ProjectResourceTestITMock {

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


    @BeforeEach
    fun setUp() {

        //arrange
        project.title = "første prosjekt"
        project.description = "første prosjekt beskrivelse"
        // project.version = 2
        // project.publishedDate = LocalDateTime.now().minusDays(2)
        project.ref = "ccc5db69-edb2-431f-855a-4368e2bcddd1"
        project.id = 120
        project.codelist = codelists
        project.requirements = requirements
        project.publications = publications
        project.needs = needs
        project.products = products
        //project.deletedDate = LocalDateTime.now().minusDays(1)

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
        //product.requirementvariant
        // product.deletedDate

        need = Need()
        need.ref = "ewdsfsada567"
        need.id = 122L
        need.title = "tittel"
        need.description = "desv"
        //need.requirements =

        publication = Publication()


        requirement = Requirement()


        projects.add(project)
    }


    @Test
    fun getProject_OK() {

        //arrange
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.`when`(projectRepository.findByRef(projectRef)).thenReturn(project)
        val response: Response = projectResource.getProject(projectRef)
        val entity: Project = ProjectMapper().toEntity(response.entity as ProjectForm)

        //assert
        assertEquals("første prosjekt", entity.title)
        assertEquals("første prosjekt beskrivelse", entity.description)
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
    }

    @Test
    fun getProject_KO() {


        //arrange
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.`when`(projectRepository.findByRef(projectRef)).thenThrow(NotFoundException("Project not found"))
        try {
            projectResource.getProject(projectRef).entity as NotFoundException
        } catch (e: Exception) {
            //assert
            print(e.message)
            assertEquals("Project not found", e.message)
        }
    }


    @Test
    fun listProjects_OK() {

        //mock
        Mockito.`when`(projectRepository.listAllProjects()).thenReturn(projects)
        val response: Response = projectResource.listProjects()

        //map
        val entity: MutableList<Project> = mutableListOf()
        for (p in response.entity as List<ProjectForm>) entity.add(ProjectMapper().toEntity(p))

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals("første prosjekt", entity.get(0).title)
        assertEquals("første prosjekt beskrivelse", entity.get(0).description)
    }

    @Test
    fun createProject_OK() {


        //arrange
        val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.doNothing().`when`(projectRepository).persist(ArgumentMatchers.any(Project::class.java))
        Mockito.`when`(projectRepository.isPersistent(ArgumentMatchers.any(Project::class.java))).thenReturn(true)


        //map
        val projectDTO = ProjectMapper().fromEntity(project)
        val response: Response = projectResource.createProject(projectDTO)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status);
    }

    @Test
    fun createProject_KO() {

        assertTrue(false)

    }


    @Test
    fun deleteProject_OK() {

        assertTrue(false)

        /*
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"
        val ref = "dsfdsgs<'fåowi39543tdsf"


        Mockito.`when`(projectRepository.deleteProject(projectId)).then { Any() }
       // Mockito.`when`(projectRepository.isPersistent(ArgumentMatchers.any(Project::class.java))).thenReturn(false)

        //map

        val response: Response = projectResource.deleteProjectByRef(projectRef)
        val entity: Project = ProjectMapper().toEntity(response.entity as ProjectForm)

        //assert
        assertNotNull(entity)
        assertEquals("bbb4db69-edb2-431f-855a-4368e2bcddd1", entity);

         */
    }

    @Test
    fun deleteProject_KO() {

        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"

        Mockito.`when`(projectRepository.findByRef(projectRef))
            .thenThrow(NotFoundException("Project not found"))
        try {
            projectResource.deleteProjectByRef(projectRef).entity as NotFoundException
        } catch (e: Exception) {
            print(e.message)
            assertEquals("Project not found", e.message)
        }
    }

    @Test
    fun updateProject_OK() {

        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"

        val updateProject = ProjectFormUpdate()
        updateProject.title = "Oppdatert tittel"

        Mockito.`when`(projectRepository.findByRef(projectRef))
            .thenReturn(project)

        val response: Response = projectResource.updateProject(projectRef, updateProject)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        val entity: Project = ProjectUpdateMapper().toEntity(response.entity as ProjectFormUpdate)
        assertEquals("Oppdatert tittel", entity.title);
    }

    @Test
    fun updateProject_KO() {

        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"

        val updateProject = ProjectFormUpdate()
        updateProject.title = "Oppdatert tittel"

        Mockito.`when`(projectRepository.findByRef(projectRef))
            .thenThrow(NotFoundException("Project not found"))

        try {
            projectResource.updateProject(projectRef, updateProject).entity as NotFoundException
        } catch (e: Exception) {
            print(e.message)
            assertEquals("Project not found", e.message)
        }
    }
}