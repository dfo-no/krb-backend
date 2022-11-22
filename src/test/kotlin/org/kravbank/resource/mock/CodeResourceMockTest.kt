package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.CodeForm
import org.kravbank.domain.*
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.CodeRepository
import org.kravbank.resource.CodeResource
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class CodeResourceMockTest {

    @InjectMock
    lateinit var codeRepository: CodeRepository

    @Inject
    lateinit var codeResource: CodeResource

    //arrange
    val projectId = 3L
    val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
    val codelistId = 4L
    val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"
    val codeRef = "script1b69-edb2-431f-855a-4368e2bcddd1"
    //val codeId = 17L


    //entity
    var codelist: Codelist = Codelist()
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
        code.id = 101
        code.ref = "dsafdsjkl823jhkfkjdhkjl"
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
    fun getCode_OK() {
        Mockito.`when`(codeRepository.findByRef(codelistId, codeRef)).thenReturn(code)

        val response: Response = codeResource.getCode(projectRef, codelistRef, codeRef)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)

        val entity: Code = CodeForm().toEntity(response.entity as CodeForm)

        assertEquals("Tittel kode", entity.title)
        assertEquals("beskrivelse kode", entity.description)
    }

    @Test
    fun listCodes_OK() {
        //arrange
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistId = 4L
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.`when`(codeRepository.listAllCodes(codelistId)).thenReturn(codes)
        val response: Response = codeResource.listCodes(projectRef, codelistRef)

        val entity: List<CodeForm> = response.entity as List<CodeForm>

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals("Tittel kode", entity[0].title)
        assertEquals("beskrivelse kode", entity[0].description)
    }

    @Test
    fun createCode_OK() {
        //mock
        Mockito.doNothing().`when`(codeRepository).persist(ArgumentMatchers.any(Code::class.java))
        Mockito.`when`(codeRepository.isPersistent(ArgumentMatchers.any(Code::class.java))).thenReturn(true)

        val form = CodeForm().fromEntity(code)
        val response: Response = codeResource.createCode(projectRef, codelistRef, form)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status);
    }


    @Test
    fun deleteCode_OK() {
        //mock
        Mockito
            .`when`(codeRepository.deleteCode(codelistId, codeRef))
            .thenReturn(code)

        val response: Response = codeResource.deleteCode(projectRef, codelistRef, codeRef)

        //assert
        assertNotNull(response)
        assertEquals("dsafdsjkl823jhkfkjdhkjl", response.entity.toString())
    }


    @Test
    fun deleteCode_KO() {
        //mock
        Mockito
            .`when`(codeRepository.deleteCode(codelistId, codeRef))
            .thenThrow(BadRequestException("Bad request! Code was not deleted"))
        try {
            codeResource.deleteCode(projectRef, codelistRef, codeRef).entity as BadRequestException
        } catch (e: Exception) {
            assertEquals("Bad request! Code was not deleted", e.message)
        }
    }

    @Test
    fun updateCode_OK() {

        //arrange
        val form = CodeForm()
        form.ref = codeRef
        form.title = "Oppdatert tittel"
        form.description = "Oppdatert beskrivelse"

        //mock
        Mockito
            .`when`(codeRepository.findByRef(codelistId, codeRef))
            .thenReturn(code)

        val response: Response = codeResource.updateCode(projectRef, codelistRef, codeRef, form)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        val entity: Code = CodeForm().toEntity(response.entity as CodeForm)
        assertEquals("Oppdatert tittel", entity.title)
        assertEquals("Oppdatert beskrivelse", entity.description);
    }

    /*
Todo:
         Testen(e) kan være nyttig for å teste at feilmeldingene som kastes, behandles på riktig måte.
         Kommer tilbake til den når jeg finner ut av hvorfor mocking ikke gir riktig verdi / ikke-null

    @Test
    fun getCode_KO() {
        //mock
        Mockito
            .`when`(codeRepository.findByRef(projectId, codelistRef))
            .thenThrow(NotFoundException("Code was not found!"))
        try {
            codeResource.listCodes(projectRef, codelistRef).entity as NotFoundException
        } catch (e: Exception) {
            //assert
            assertEquals("Code was not found!", e.message)
        }
    }

    @Test
    fun createCode_KO() {
        //mock
        Mockito.`when`(codeRepository.isPersistent(code)).thenReturn(false)
        Mockito.`when`(codeRepository.createCode(code))
            .thenThrow(BadRequestException("Bad request! Code was not created"))

        try {
            val form = CodeForm().fromEntity(code)
            codeResource.createCode(projectRef, codelistRef, form).entity as BadRequestException

        } catch (e: Exception) {
            //assert
            assertEquals("Bad request! Code was not created", e.message);
        }
    }

    @Test
    fun updateCode_KO() {
        assertFalse(true)
    }

     */
}