package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.PublicationForm
import org.kravbank.domain.*
import org.kravbank.repository.PublicationRepository
import org.kravbank.resource.PublicationResource
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class PublicationResourceMockTest {

    @InjectMock
    lateinit var publicationRepository: PublicationRepository

    @Inject
    lateinit var publicationResource: PublicationResource

    //entity
    var project: Project = Project()
    var publication: Publication = Publication()
    var product: Product = Product()
    var requirement: Requirement = Requirement()
    var need: Need = Need()

    var reqVariant: RequirementVariant = RequirementVariant()

    //lists
    var codelists: MutableList<Codelist> = mutableListOf()
    var requirements: MutableList<Requirement> = mutableListOf()
    var needs: MutableList<Need> = mutableListOf()
    var publications: MutableList<Publication> = mutableListOf()
    var products: MutableList<Product> = mutableListOf()
    var reqVariants: MutableList<RequirementVariant> = mutableListOf()

    //arrange
    val projectId = 3L
    val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
    val publicationRef = "zzz4db69-edb2-431f-855a-4368e2bcddd1"


    @BeforeEach
    fun setUp() {

        //arrange
        project = Project()
        project.title = "første prosjekt"
        project.description = "første prosjekt beskrivelse"
        project.ref = "ccc5db69-edb2-431f-855a-4368e2bcddd1"
        project.id = 120
        project.codelist = codelists
        project.publications = publications
        project.requirements = requirements
        project.needs = needs
        project.products = products

        product.id = 999L
        product.ref = "98870ds9fgsdfklmklklds"
        product.title = "Produkt tittel"
        product.description = "Produkt beskrivelse"
        product.project = project
        product.requirementvariant = reqVariant

        need = Need()
        need.ref = "need2b69-edb2-431f-855a-4368e2bcddd1"
        need.id = 123L
        need.title = "tittel"
        need.description = "desv"

        requirement = Requirement()
        requirement.ref = "23chgvjkhty87"
        requirement.project = project
        requirement.id = 500L
        requirement.need = need
        requirement.title = "Requirement tittel"
        requirement.description = "Requirement beskrivelse"
        requirement.requirementvariants = reqVariants

        reqVariant = RequirementVariant()
        reqVariant.requirement = requirement
        reqVariant.id = 400L
        reqVariant.ref = "tfghjda67765hjbnknmbkljsakl"
        reqVariant.description = "Req variant beskrivelse"
        reqVariant.requirementText = "Tekst"
        reqVariant.useQualification = false
        reqVariant.useSpesification = true
        reqVariant.useProduct = true
        reqVariant.instruction = "Ny instruksjon"
        reqVariant.product = products

        publication = Publication()
        publication.project = project
        publication.id = 102L
        publication.comment = "En kommentar"
        publication.version = 10
        publication.ref = "rewffd79dsf223"

        publications.add(publication)
        requirements.add(requirement)
        reqVariants.add(reqVariant)
        products.add(product)

    }

    @Test
    fun getPublication_OK() {
        //mock
        Mockito
            .`when`(publicationRepository.findByRef(projectId, publicationRef))
            .thenReturn(publication)

        val response: Response = publicationResource.getPublication(projectRef, publicationRef)
        val entity: Publication = PublicationForm().toEntity(response.entity as PublicationForm)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals("En kommentar", entity.comment)
        assertEquals(10, entity.version)
    }

    @Test
    fun listPublications_OK() {
        //mock
        Mockito
            .`when`(publicationRepository.listAllPublications(projectId))
            .thenReturn(publications)

        val response: Response = publicationResource.listPublications(projectRef)

        //val entity =
        //   listOf(response.entity).filterIsInstance<PublicationForm>()
        //.takeIf { it.size == listOf(response.entity).size }!!

        @Suppress("UNCHECKED_CAST")
        val entity: List<PublicationForm> = response.entity as List<PublicationForm>

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals("En kommentar", entity[0].comment)
        assertEquals(10, entity[0].version)
    }

    @Test
    fun createPublication_OK() {
        //mock
        Mockito
            .doNothing().`when`(publicationRepository).persist(ArgumentMatchers.any(Publication::class.java))
        Mockito
            .`when`(publicationRepository.isPersistent(ArgumentMatchers.any(Publication::class.java)))
            .thenReturn(true)

        val form = PublicationForm().fromEntity(publication)
        val response: Response = publicationResource.createPublication(projectRef, form)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun updatePublication_OK() {

        //arrange
        val form = PublicationForm()
        form.ref = publicationRef
        form.comment = "Oppdatert Comment"
        form.version = 19

        //mock
        Mockito
            .`when`(publicationRepository.findByRef(projectId, publicationRef))
            .thenReturn(publication)

        val response: Response = publicationResource.updatePublication(projectRef, publicationRef, form)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        val entity: Publication = PublicationForm().toEntity(response.entity as PublicationForm)
        assertEquals("Oppdatert Comment", entity.comment)
    }


    /*


    @Test
    fun deletePublication_OK() {
        val publication_2 = Publication()
        publication_2.id = 155L
        publication_2.ref = "dsfdsgs<'fåowi39543tdsf"
        publication_2.version = 10
        publication_2.comment = "En kommentar her"
        publication_2.project = project
        publication_2.date = time


        //mock
        Mockito
            .`when`(publicationRepository.deletePublication(publicationId))
            .thenReturn(true)

        val response: Response = publicationResource.deletePublication(projectRef, publicationRef)

        //assert
        assertNotNull(response)
        assertEquals(publicationRef, response.entity.toString())

    }



       @Test
    fun deletePublication_KO() {

        Mockito
            .`when`(publicationRepository.deletePublication(publicationId))
            .thenReturn(false)

        try {
            publicationResource.deletePublication(projectRef, publicationRef).entity as BadRequestException
        } catch (e: Exception) {
            assertEquals("Bad request! Publication was not deleted", e.message)
        }
    }


    @Test
    fun createPublication_KO() {
        assertFalse(true)
    }


        @Test
        fun getPublication_KO() {
            assertFalse(true)
        }


        @Test
        fun updatePublication_KO() {
            assertFalse(true)
        }


     */


}