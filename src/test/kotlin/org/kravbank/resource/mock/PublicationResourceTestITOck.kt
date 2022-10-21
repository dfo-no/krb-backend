package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.*
import org.kravbank.repository.PublicationRepository
import org.kravbank.resource.PublicationResource
import org.kravbank.utils.publication.dto.PublicationForm
import org.kravbank.utils.publication.mapper.PublicationMapper
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
internal class PublicationResourceTestITOck {

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

    val time: LocalDateTime = LocalDateTime.of(2010,10,10,10,10)


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
        publication.date = time

    }

    @Test
    fun getPublication() {
        val projectId = 3L

        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val publicationRef = "zzz4db69-edb2-431f-855a-4368e2bcddd1"
        Mockito.`when`(
            publicationRepository.findByRef(
                projectId,
                publicationRef
            ))
            .thenReturn(publication)

        val response: Response =
            publicationResource.getPublication(projectRef, publicationRef)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)

        val entity: Publication = PublicationMapper()
            .toEntity(response.entity as PublicationForm)

        assertEquals("En kommentar", entity.comment)
        assertEquals(10, entity.version)
        assertEquals(time,entity.date)


    }

    @Test
    fun listPublications() {
    }

    @Test
    fun createPublication() {
    }

    @Test
    fun deletePublication() {
    }

    @Test
    fun updatePublication() {
    }
}