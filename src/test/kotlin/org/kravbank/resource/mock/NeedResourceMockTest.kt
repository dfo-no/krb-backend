package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.NeedForm
import org.kravbank.domain.*
import org.kravbank.repository.NeedRepository
import org.kravbank.resource.NeedResource
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
internal class NeedResourceMockTest {

    @InjectMock
    lateinit var needRepository: NeedRepository

    @Inject
    lateinit var needResource: NeedResource

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
        need.description = "beskrivelse"

        publication = Publication()
        requirement = Requirement()

        //add to list
        codelists.add(codelist)
        codes.add(code)
        products.add(product)
        needs.add(need)

    }

    @Test
    fun getNeed_OK() {

        //arrange
        val projectId = 2L
        val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
        val needRef = "need1b69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.`when`(
            needRepository.findByRef(
                projectId,
                needRef
            )
        ).thenReturn(need)

        val response: Response = needResource.getNeed(projectRef, needRef)
        val entity: Need = NeedForm().toEntity(response.entity as NeedForm)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals("tittel", entity.title)
        assertEquals("beskrivelse", entity.description)
    }

    @Test
    fun listNeeds_OK() {

        //arrange
        val projectId = 2L
        val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
        val needID = 10L
        val needRef = "need1b69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.`when`(needRepository.listAllNeeds(projectId)).thenReturn(needs)

        val response: Response = needResource.listNeeds(projectRef)
        val entity: List<NeedForm> = response.entity as List<NeedForm>

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        Assertions.assertFalse(entity.isEmpty())
        assertEquals("tittel", entity[0].title)
        assertEquals("beskrivelse", entity[0].description)
    }

    @Test
    fun createNeed_OK() {

        //arrange
        val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.doNothing().`when`(needRepository).persist(ArgumentMatchers.any(Need::class.java))
        Mockito.`when`(needRepository.isPersistent(ArgumentMatchers.any(Need::class.java))).thenReturn(true)

        //map
        val form = NeedForm().fromEntity(need)
        val response: Response = needResource.createNeed(projectRef, form)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status);
    }


    @Test
    fun deleteNeed_OK() {

        //arrange
        val projectId = 2L
        val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
        val needID = 10L
        val needRef = "need1b69-edb2-431f-855a-4368e2bcddd1"

        Mockito.`when`(needRepository.deleteNeed(projectId, needRef)).thenReturn(need)
        val response: Response = needResource.deleteNeed(projectRef, needRef)

        assertNotNull(response)
        assertEquals("ewdsfsada567", response.entity.toString())

    }


    @Test
    fun updateNeed_OK() {
        //arrange
        val projectId = 2L
        val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
        val needID = 10L
        val needRef = "need1b69-edb2-431f-855a-4368e2bcddd1"

        val form = NeedForm()
        form.ref = needRef
        form.title = "Oppdatert tittel"
        form.description = "Oppdatert beskrivelse"

        Mockito.`when`(needRepository.findByRef(projectId, needRef))
            .thenReturn(need)

        val response: Response = needResource.updateNeed(projectRef, needRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)

        val entity: Need = NeedForm().toEntity(response.entity as NeedForm)

        assertEquals("Oppdatert tittel", entity.title)
        assertEquals("Oppdatert beskrivelse", entity.description);

    }


    /*
Todo:
         Testen(e) kan være nyttig for å teste at feilmeldingene som kastes, behandles på riktig måte.
         Kommer tilbake til den når jeg finner ut av hvorfor mocking ikke gir riktig verdi / ikke-null


     @Test
     fun createNeed_KO() {
         assertFalse(true)
     }


     @Test
     fun getNeed_KO() {
         assertFalse(true)
     }


     @Test
     fun deleteNeed_KO() {
        assertFalse(true)
     }


     @Test
     fun updateNeed_KO() {
         assertFalse(true)
     }


      */
}