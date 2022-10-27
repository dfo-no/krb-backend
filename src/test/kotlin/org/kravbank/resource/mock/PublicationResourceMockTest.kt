package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.PublicationForm
import org.kravbank.domain.*
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.PublicationRepository
import org.kravbank.resource.PublicationResource
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
internal class PublicationResourceMockTest {

    @InjectMock
    lateinit var publicationRepository: PublicationRepository

    @Inject
    lateinit var publicationResource: PublicationResource

    //entity
    var project: Project = Project()
    var code: Code = Code()
    var publication: Publication = Publication()
    var product: Product = Product()
    var requirement: Requirement = Requirement()
    var need: Need = Need()

    //lists
    var codes: MutableList<Code> = mutableListOf()
    var codelists: MutableList<Codelist> = mutableListOf()
    var requirements: MutableList<Requirement> = mutableListOf()
    var needs: MutableList<Need> = mutableListOf()
    var publications: MutableList<Publication> = mutableListOf()
    var products: MutableList<Product> = mutableListOf()

    val time: LocalDateTime = LocalDateTime.of(2010, 10, 10, 10, 10)

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


        publication = Publication()
        publication.project = project
        publication.id = 1L
        publication.comment = "En kommentar"
        publication.version = 10
        publication.ref = "rewffd79dsf223"

        publications.add(publication)

    }

    @Test
    fun getPublication_OK() {

        //arrange
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val publicationRef = "zzz4db69-edb2-431f-855a-4368e2bcddd1"

        publication.date = time


        //mock
        Mockito.`when`(
            publicationRepository.findByRef(
                projectId,
                publicationRef
            )
        )
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
    fun getPublication_KO() {
        assertFalse(true)
    }


    @Test
    fun listPublications_OK() {

        //arrange
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val publicationID = 8L
        val publicationRef = "zzz4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.`when`(publicationRepository.listAllPublications(publicationID)).thenReturn(publications)

        val response: Response = publicationResource.listPublications(projectRef)
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

        //arrange
        val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.doNothing().`when`(publicationRepository).persist(ArgumentMatchers.any(Publication::class.java))
        Mockito.`when`(publicationRepository.isPersistent(ArgumentMatchers.any(Publication::class.java))).thenReturn(true)

        //map
        val form = PublicationForm().fromEntity(publication)
        val response: Response = publicationResource.createPublication(projectRef, form)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status);
    }


    @Test
    fun createPublication_KO() {
        assertFalse(true)
    }

    @Test
    fun deletePublication_OK() {

        //arrange
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val publicationID = 8L
        val publicationRef = "zzz4db69-edb2-431f-855a-4368e2bcddd1"
        val ref = "dsfdsgs<'fåowi39543tdsf"

        val publication_2 = Publication()
        publication_2.id = 155L
        publication_2.ref = ref
        publication_2.version = 10
        publication_2.comment = "En kommentar her"
        publication_2.project = project
        publication_2.date = time


        Mockito.`when`(publicationRepository.deletePublication(publicationID)).thenReturn(true)
        val response: Response = publicationResource.deletePublication(projectRef, publicationRef)

        assertNotNull(response)
        assertEquals(publicationRef, response.entity.toString())

        print(response)


    }


    @Test
    fun deletePublication_KO() {
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val publicationID = 8L
        val publicationRef = "zzz4db69-edb2-431f-855a-4368e2bcddd1"

        Mockito.`when`(publicationRepository.deletePublication(publicationID)).thenReturn(false)
        try {
            publicationResource.deletePublication(projectRef, publicationRef).entity as BadRequestException
        } catch (e: Exception) {

            print(e.message)
            assertEquals("Bad request! Publication was not deleted", e.message)
        }
    }

    @Test
    fun updatePublication_OK() {

        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val publicationID = 8L
        val publicationRef = "zzz4db69-edb2-431f-855a-4368e2bcddd1"


        val form = PublicationForm()
        form.ref = publicationRef
        form.comment = "Oppdatert Comment"
        form.version = 19

        Mockito.`when`(publicationRepository.findByRef(projectId, publicationRef))
            .thenReturn(publication)

        val response: Response = publicationResource.updatePublication(projectRef, publicationRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)

        val entity: Publication = PublicationForm().toEntity(response.entity as PublicationForm)
        assertEquals("Oppdatert Comment", entity.comment);
    }


    @Test
    fun updatePublication_KO() {
        assertFalse(true)
    }


    }