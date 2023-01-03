package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.PublicationForm
import org.kravbank.domain.Project
import org.kravbank.domain.Publication
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationRepository
import org.kravbank.resource.PublicationResource
import org.kravbank.service.PublicationService
import org.kravbank.utils.Messages.RepoErrorMsg.PUBLICATION_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.PUBLICATION_NOTFOUND
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.ws.rs.core.Response


@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class PublicationResourceMockTest {


    private final val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private final val publicationRepository: PublicationRepository = mock(PublicationRepository::class.java)

    private final val publicationService = PublicationService(publicationRepository, projectRepository)

    val publicationResource = PublicationResource(publicationService)


    private val arrangeSetup = TestSetup.Arrange


    private lateinit var publications: List<Publication>
    private lateinit var publication: Publication
    private lateinit var project: Project
    private lateinit var updateForm: PublicationForm
    private lateinit var createForm: PublicationForm


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()


        publications = arrangeSetup.publications
        publication = arrangeSetup.publication
        project = arrangeSetup.project
        updateForm = arrangeSetup.updatedPublicationForm
        createForm = PublicationForm().fromEntity(publication)


        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(publicationRepository.findByRef(project.id, publication.ref)).thenReturn(publication)
        `when`(publicationRepository.listAllPublications(project.id)).thenReturn(publications)

    }


    @Test
    fun getPublication_OK() {
        val response = publicationResource.getPublication(project.ref, publication.ref)

        val entity: Publication = PublicationForm().toEntity(response)

        assertNotNull(response)
        assertEquals(publication.comment, entity.comment)
        assertEquals(publication.version, entity.version)
    }

    @Test
    fun listPublications_OK() {
        val response = publicationResource.listPublications(project.ref)

        val entity: List<PublicationForm> = response

        assertNotNull(response)
        assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(publications[0].comment, firstObjectInList.comment)
        assertEquals(publications[0].version, firstObjectInList.version)
    }

    @Test
    fun createPublication_OK() {
        doNothing().`when`(publicationRepository).persist(ArgumentMatchers.any(Publication::class.java))

        `when`(publicationRepository.isPersistent(ArgumentMatchers.any(Publication::class.java))).thenReturn(true)

        val response = publicationResource.createPublication(project.ref, createForm)

        val entity: Response = response

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, entity.status)
    }

    @Test
    fun updatePublication_OK() {
        `when`(publicationRepository.findByRef(project.id, publication.ref)).thenReturn(publication)


        val response = publicationResource.updatePublication(project.ref, publication.ref, updateForm)

        val entity: Publication = PublicationForm().toEntity(response)

        assertNotNull(response)
        assertEquals(updateForm.comment, entity.comment)
        assertEquals(updateForm.version, entity.version)
    }


    @Test
    fun deletePublication_OK() {
        publicationResource.deletePublication(project.ref, publication.ref)

        verify(publicationRepository).delete(publication)
    }


    /**
     *
     * KO's of relevant exceptions.
     *
     */


    @Test
    fun getPublication_KO() {
        `when`(publicationRepository.findByRef(project.id, publication.ref))
            .thenThrow(NotFoundException(PUBLICATION_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            publicationResource.getPublication(
                project.ref,
                publication.ref
            )
        }

        assertEquals(PUBLICATION_NOTFOUND, exception.message)
    }

    @Test
    fun createPublication_KO() {
        val exception = assertThrows(BadRequestException::class.java) {
            publicationResource.createPublication(
                project.ref,
                createForm,
            )
        }

        assertEquals(PUBLICATION_BADREQUEST_CREATE, exception.message)
    }

    @Test
    fun updatePublication_KO() {
        `when`(
            publicationRepository.findByRef(
                project.id,
                publication.ref
            )
        ).thenThrow(NotFoundException(PUBLICATION_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            publicationResource.updatePublication(
                project.ref,
                publication.ref,
                updateForm
            )
        }

        assertEquals(PUBLICATION_NOTFOUND, exception.message)
    }

}