package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.resource.CodelistResource
import org.kravbank.domain.*
import org.kravbank.lang.exception.BadRequestException
import org.kravbank.lang.exception.NotFoundException
import org.kravbank.repository.CodelistRepository
import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.form.codelist.CodelistFormUpdate
import org.kravbank.utils.mapper.codelist.CodelistMapper
import org.kravbank.utils.mapper.codelist.CodelistUpdateMapper
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject
import javax.ws.rs.core.Response


@QuarkusTest
internal class CodelistResourceTestTMock {

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
        project.version = 2
        project.publishedDate = LocalDateTime.now().minusDays(2)
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


        //add to list
        codelists.add(codelist)
        codes.add(code)
        products.add(product)
        needs.add(need)

    }

    /*
        @AfterEach
        fun reset_mocks() {
            Mockito.reset(codelistResource, codelistRepository)
        }

     */

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

        val entity: Codelist = CodelistMapper().toEntity(response.entity as CodelistForm)

        assertEquals("hello243567", entity.ref)
        assertEquals("Første codelist", entity.title)
        assertEquals("første codelist beskrivelse", entity.description)
        assertEquals(project, entity.project)


    }

    @Test
    fun getCodelist_NOTFOUND() {
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"

        Mockito.`when`(
            codelistRepository.findByRef(
                projectId,
                codelistRef
            )
        )
            .thenThrow(NotFoundException("Codelist was not found!"))
        try {
            val response: Any =
                codelistResource.getCodelistByRef(projectRef, codelistRef).entity as NotFoundException
        } catch (e: Exception) {
            assertEquals("Codelist was not found!", e.message)
        }


    }

    @Test
    fun listCodelists_OK() {

        val id = 1L
        val ref = "ccc4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.`when`(codelistRepository.listAllCodelists(id)).thenReturn(codelists)
        val response: Response = codelistResource.listCodelists(ref)

        //mapper response fra DTO til entitets liste
        val entity: MutableList<Codelist> = mutableListOf()
        for (r in response.entity as List<CodelistForm>) entity.add(CodelistMapper().toEntity(r))

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        // println("entity ::::>>>${entity.get(0).title}")
        assertFalse(entity.isEmpty())
        assertEquals("Første codelist", entity.get(0).title)
        assertEquals("første codelist beskrivelse", entity.get(0).description)


    }

    @Test
    fun createCodelist_OK() {

        //Mockito.reset(codelistResource, codelistRepository)


        //Mockito.doNothing().`when`(codelistRepository).createCodelist(Mockito.any() )


        //Mockito.`when`(codelistRepository.isPersistent(ArgumentMatchers.any(Codelist::class.java))).thenReturn(true)

        val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"
        codelist_2 = Codelist()
        codelist_2.title = "Andre codelist"
        codelist_2.description = "Andre codelist beskrivelse"
        codelist_2.ref = "hello2423rfd4353grfdg243567"
        codelist_2.project = project
        codelist_2.codes = codes
        codelist_2.id = (4L)
        codelist_2.ref = "et7ytijdsa8"

        val mappedToDto = CodelistMapper().fromEntity(codelist_2)

        val response: Response = codelistResource.createCodelist(projectRef, mappedToDto)

        print(response)
        print(response.entity)
        assertNotNull(response);


    }

    fun createCodelist_BADREQUEST() {

        //Mockito.reset(codelistResource, codelistRepository)


        //Mockito.doNothing().`when`(codelistRepository).createCodelist(Mockito.any() )


        //Mockito.`when`(codelistRepository.isPersistent(ArgumentMatchers.any(Codelist::class.java))).thenReturn(true)

        val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"
        codelist_2 = Codelist()
        codelist_2.title = "Andre codelist"
        codelist_2.description = "Andre codelist beskrivelse"
        codelist_2.ref = "hello2423rfd4353grfdg243567"
        codelist_2.project = project
        codelist_2.codes = codes
        codelist_2.id = (4L)
        codelist_2.ref = "et7ytijdsa8"

        val mappedToDto = CodelistMapper().fromEntity(codelist_2)
        val response: Response = codelistResource.createCodelist(projectRef, mappedToDto)
        assertNotNull(response)
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
    fun deleteCodelist_BADREQUEST() {
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"
        val ref = "dsfdsgs<'fåowi39543tdsf"

        Mockito.`when`(codelistRepository.deleteCodelist(projectId, codelistRef))
            .thenThrow(BadRequestException("Bad request! Codelist was not deleted"))
        try {
            val response: Response = codelistResource.deleteCodelist(projectRef, codelistRef)
        } catch (e: Exception) {
            print("CAUSE ${e.cause}")
            assertEquals("Bad request! Codelist was not deleted", e.message)
        }
    }

    @Test
    fun updateCodelist_OK() {

        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"

        val updatedCodelist = CodelistFormUpdate()
        updatedCodelist.title = "Oppdatert tittel"

        Mockito.`when`(codelistRepository.findByRef(projectId, codelistRef))
            .thenReturn(codelist)

        val response: Response = codelistResource.updateCodelist(projectRef, codelistRef, updatedCodelist)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        val entity: Codelist = CodelistUpdateMapper().toEntity(response.entity as CodelistFormUpdate)
        assertEquals("Oppdatert tittel", entity.title);

    }

    @Test
    fun updateCodelist_NOTFOUND() {

        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val codelistRef = "qqq4db69-edb2-431f-855a-4368e2bcddd1"

        Mockito.`when`(codelistRepository.findByRef(projectId, codelistRef))
            .thenThrow(NotFoundException("Codelist was not found!"))

        try {
            val response: Any =
                codelistResource.getCodelistByRef(projectRef, codelistRef).entity as NotFoundException
        } catch (e: Exception) {
            assertEquals("Codelist was not found!", e.message)
        }

    }
}