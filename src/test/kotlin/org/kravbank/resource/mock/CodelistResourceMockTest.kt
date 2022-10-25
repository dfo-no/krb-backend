package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.CodelistForm
import org.kravbank.domain.*
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.CodelistRepository
import org.kravbank.resource.CodelistResource
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
internal class CodelistResourceMockTest {

    @InjectMock
    lateinit var codelistRepository: CodelistRepository

    @Inject
    lateinit var codelistResource: CodelistResource

    //entity
    var codelist: Codelist = Codelist()
    var codelist_2: Codelist = Codelist()
    var codelist_3: Codelist = Codelist()

    var project: Project = Project()
    var code: Code = Code()
    var publication: Publication = Publication()
    var product: Product = Product()
    var requirement: Requirement = Requirement()
    var need: Need = Need()

    //list
    var codes: MutableList<Code> = mutableListOf()
    var codelists: MutableList<Codelist> = mutableListOf()
    var requirements: MutableList<Requirement> = mutableListOf()
    var needs: MutableList<Need> = mutableListOf()
    var publications: MutableList<Publication> = mutableListOf()
    var products: MutableList<Product> = mutableListOf()

    @BeforeEach
    fun setUp() {

        //arrange
        project = Project()
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

        //add to list
        codelists.add(codelist)
        codes.add(code)
        products.add(product)
        needs.add(need)

    }

    @Test
    fun getCodelist_OK() {

        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"
        Mockito.`when`(
            codelistRepository.findByRef(
                projectId,
                codelistRef
            )
        )
            .thenReturn(codelist)

        val response: Response =
            codelistResource.getCodelistByRef(projectRef, codelistRef)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)

        val entity: Codelist = CodelistForm().toEntity(response.entity as CodelistForm)

        assertEquals("Første codelist", entity.title)
        assertEquals("første codelist beskrivelse", entity.description)
    }

    @Test
    fun getCodelist_KO() {

        //arrange
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.`when`(
            codelistRepository.findByRef(
                projectId,
                codelistRef
            )
        ).thenThrow(NotFoundException("Codelist was not found!"))
        try {
            codelistResource.getCodelistByRef(projectRef, codelistRef).entity as NotFoundException
        } catch (e: Exception) {
            //assert
            assertEquals("Codelist was not found!", e.message)
        }
    }

    @Test
    fun listCodelists_OK() {
        //arrange
        val id = 1L
        val ref = "ccc4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.`when`(codelistRepository.listAllCodelists(id)).thenReturn(codelists)
        val response: Response = codelistResource.listCodelists(ref)

        //map
        val entity: MutableList<CodelistForm> = response.entity as MutableList<CodelistForm>

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals("Første codelist", entity[0].title)
        assertEquals("første codelist beskrivelse", entity[0].description)
    }

    @Test
    fun createCodelist_OK() {
        //arrange
        val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.doNothing().`when`(codelistRepository).persist(ArgumentMatchers.any(Codelist::class.java))
        Mockito.`when`(codelistRepository.isPersistent(ArgumentMatchers.any(Codelist::class.java))).thenReturn(true)

        //map
        val form = CodelistForm().fromEntity(codelist)
        val response: Response = codelistResource.createCodelist(projectRef, form)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun createCodelist_KO() {
        //arrange
        val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
        val form = CodelistForm()
        form.title = "Oppdatert tittel"
        form.description = "desc"
        //createCodelistDTO.project = project
        form.ref = "8yuhitd6sa5"

        val codelist_4 = Codelist()
        codelist_4.title = "Oppdatert tittel"
        codelist_4.description = "desc"
        codelist_4.project = project
        codelist_4.ref = "8yuhitd6sa5"


        val codelist = CodelistForm().toEntity(form)
        codelist.project = project


        //mock
        Mockito.`when`(codelistRepository.isPersistent(codelist_4)).thenReturn(false)
        Mockito.`when`(codelistRepository.createCodelist(codelist_4))
            .thenThrow(BadRequestException("Bad request! Codelist was not created"))

        var type: Any? = null

        try {
            type = codelistResource.createCodelist(projectRef, form).entity as BadRequestException

        } catch (e: Exception) {
            //assert
            print("PRINTING MESSAGE ${e.message}")

            assertEquals("Bad request! Codelist was not created", e.message);
        }

        print("PRINTER TYPE $type")
    }

    @Test
    fun deleteCodelist_OK() {
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"
        val ref = "dsfdsgs<'fåowi39543tdsf"

        codelist_3 = Codelist()
        codelist_3.title = "Første codelist"
        codelist_3.description = "første codelist beskrivelse"
        codelist_3.ref = ref
        codelist_3.project = project
        codelist_3.codes = codes
        codelist_3.id = (1L)

        Mockito.`when`(codelistRepository.deleteCodelist(projectId, codelistRef)).thenReturn(codelist_3)
        val response: Response = codelistResource.deleteCodelist(projectRef, codelistRef)
        assertNotNull(response)
        assertEquals(ref, response.entity)
    }

    @Test
    fun deleteCodelist_KO() {
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"

        Mockito.`when`(codelistRepository.deleteCodelist(projectId, codelistRef))
            .thenThrow(BadRequestException("Bad request! Codelist was not deleted"))
        try {
            codelistResource.deleteCodelist(projectRef, codelistRef).entity as BadRequestException
        } catch (e: Exception) {
            assertEquals("Bad request! Codelist was not deleted", e.message)
        }
    }

    @Test
    fun updateCodelist_OK() {
        val projectId = 3L
        val codelistId = 4L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"

        val form = CodelistForm()
        form.ref = "qqq4db69-edb2-431f-855a-4368e2bcddd1"
        form.title = "Oppdatert tittel"
        form.description = "samme som før"

        Mockito.`when`(codelistRepository.findByRef(projectId, codelistRef))
            .thenReturn(codelist)

        val response: Response = codelistResource.updateCodelist(projectRef, codelistRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)

        val entity: Codelist = CodelistForm()
            .toEntity(response.entity as CodelistForm)
        assertEquals("Oppdatert tittel", entity.title);

    }

    @Test
    fun updateCodelist_KO() {
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"

        val form = CodelistForm()
        form.title = "Oppdatert tittel"
        Mockito.`when`(codelistRepository.findByRef(projectId, codelistRef))
            .thenThrow(NotFoundException("Codelist was not found!"))

        try {
            codelistResource.updateCodelist(projectRef, codelistRef, form).entity as NotFoundException
        } catch (e: Exception) {
            assertEquals("Codelist was not found!", e.message)
        }

    }
}