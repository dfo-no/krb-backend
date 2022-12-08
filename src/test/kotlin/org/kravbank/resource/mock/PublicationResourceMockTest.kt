package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.PublicationForm
import org.kravbank.domain.Publication
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.PublicationRepository
import org.kravbank.resource.PublicationResource
import org.kravbank.utils.Messages.RepoErrorMsg.PUBLICATION_BADREQUEST_DELETE
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

        val response: Response = publicationResource.getPublication(projectRef, publicationRef)

        val entity: Publication = PublicationForm().toEntity(response.entity as PublicationForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals(publication.comment, entity.comment)
        assertEquals(publication.version, entity.version)
    }

    @Test
    fun listPublications_OK() {
        Mockito
            .`when`(publicationRepository.listAllPublications(projectId))
            .thenReturn(publications)

        val response: Response = publicationResource.listPublications(projectRef)

        val entity: List<PublicationForm> = response.entity as List<PublicationForm>

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals(publications[0].comment, entity[0].comment)
        assertEquals(publications[0].version, entity[0].version)
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

        val response: Response = publicationResource.createPublication(projectRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun updatePublication_OK() {
        Mockito
            .`when`(publicationRepository.findByRef(projectId, publicationRef))
            .thenReturn(publication)

        val form = updatedPublicationForm

        val response: Response = publicationResource.updatePublication(projectRef, publicationRef, form)

        val entity: Publication = PublicationForm().toEntity(response.entity as PublicationForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals(form.comment, entity.comment)
        assertEquals(form.version, entity.version)
    }


    @Test
    fun deletePublication_OK() {
        Mockito
            .`when`(publicationRepository.deletePublication(projectId, publicationRef))
            .thenReturn(publication)

        val response: Response = publicationResource.deletePublication(projectRef, publicationRef)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals(publication.ref, response.entity)
    }

    @Test
    fun deletePublication_KO() {
        Mockito
            .`when`(publicationRepository.deletePublication(projectId, publicationRef))
            .thenThrow(BadRequestException(PUBLICATION_BADREQUEST_DELETE))

        try {
            publicationResource.deletePublication(projectRef, publicationRef).entity as BadRequestException

        } catch (e: Exception) {
            assertEquals(PUBLICATION_BADREQUEST_DELETE, e.message)
        }
    }


}