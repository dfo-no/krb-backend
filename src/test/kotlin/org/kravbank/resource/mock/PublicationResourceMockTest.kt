package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.PublicationForm
import org.kravbank.domain.Publication
import org.kravbank.repository.PublicationRepository
import org.kravbank.resource.PublicationResource
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.publication
import org.kravbank.utils.TestSetup.Arrange.publicationForm
import org.kravbank.utils.TestSetup.Arrange.publications
import org.kravbank.utils.TestSetup.Arrange.updatedPublicationForm
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

    private final val arrangeSetup = TestSetup.Arrange

    private val projectId: Long = arrangeSetup.project_publicationId
    private val projectRef: String = arrangeSetup.project_publicationRef
    private val publicationRef: String = arrangeSetup.publication_projectRef

    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }

    @Test
    fun getPublication_OK() {
        Mockito
            .`when`(publicationRepository.findByRef(projectId, publicationRef))
            .thenReturn(publication)

        val response = publicationResource.getPublication(projectRef, publicationRef)

        val entity: Publication = PublicationForm().toEntity(response)

        assertNotNull(response)
        assertEquals(publication.comment, entity.comment)
        assertEquals(publication.version, entity.version)
    }

    @Test
    fun listPublications_OK() {
        Mockito
            .`when`(publicationRepository.listAllPublications(projectId))
            .thenReturn(publications)

        val response = publicationResource.listPublications(projectRef)

        assertNotNull(response)
        assertFalse(response.isEmpty())
        assertEquals(publications[0].comment, response[0].comment)
        assertEquals(publications[0].version, response[0].version)
    }

    @Test
    fun createPublication_OK() {
        Mockito
            .doNothing()
            .`when`(publicationRepository).persist(ArgumentMatchers.any(Publication::class.java))

        Mockito
            .`when`(publicationRepository.isPersistent(ArgumentMatchers.any(Publication::class.java)))
            .thenReturn(true)

        val form = publicationForm

        val response = publicationResource.createPublication(projectRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun updatePublication_OK() {
        Mockito
            .`when`(publicationRepository.findByRef(projectId, publicationRef))
            .thenReturn(publication)

        val form = updatedPublicationForm

        val response = publicationResource.updatePublication(projectRef, publicationRef, form)

        val entity: Publication = PublicationForm().toEntity(response)

        assertNotNull(response)
        assertEquals(form.comment, entity.comment)
        assertEquals(form.version, entity.version)
    }

    /*TODO("Fix delete test")

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

                        val response = publicationResource.deletePublication(projectRef, publicationRef)

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

                 */

}