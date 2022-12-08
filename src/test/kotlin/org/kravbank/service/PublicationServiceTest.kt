package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.Publication
import org.kravbank.repository.PublicationRepository
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.newPublication
import org.kravbank.utils.TestSetup.Arrange.publication
import org.kravbank.utils.TestSetup.Arrange.publications
import org.kravbank.utils.TestSetup.Arrange.updatedPublicationForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class PublicationServiceTest {

    @InjectMock
    lateinit var publicationRepository: PublicationRepository

    @Inject
    lateinit var publicationService: PublicationService

    private final val arrangeSetup = TestSetup.Arrange

    private final val projectId: Long = arrangeSetup.project_publicationId

    private final val projectRef: String = arrangeSetup.project_publicationRef

    private final val publicationRef: String = arrangeSetup.publication_projectRef


    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }

    @Test
    fun get() {
        Mockito
            .`when`(
                publicationRepository.findByRef(
                    projectId,
                    publicationRef
                )
            )
            .thenReturn(publication)

        val mockedPublication: Publication =
            publicationService.get(arrangeSetup.project_publicationRef, arrangeSetup.publication_projectRef)

        Assertions.assertEquals(publication.comment, mockedPublication.comment)
        Assertions.assertEquals(publication.id, mockedPublication.id)
        Assertions.assertEquals(publication.project, mockedPublication.project)
        Assertions.assertEquals(publication.date, mockedPublication.date)
    }

    @Test
    fun list() {
        Mockito.`when`(
            publicationRepository
                .listAllPublications(projectId)
        ).thenReturn(publications)

        val mockedPublications: List<Publication> = publicationService.list(arrangeSetup.project_publicationRef)

        Assertions.assertEquals(publications[0].comment, mockedPublications[0].comment)
        Assertions.assertEquals(publications[0].id, mockedPublications[0].id)
        Assertions.assertEquals(publications[0].project, mockedPublications[0].project)
        Assertions.assertEquals(publications[0].date, mockedPublications[0].date)
    }

    @Test
    fun create() {
        Mockito
            .doNothing()
            .`when`(publicationRepository)
            .persist(ArgumentMatchers.any(Publication::class.java))

        Mockito
            .`when`(publicationRepository.isPersistent(ArgumentMatchers.any(Publication::class.java)))
            .thenReturn(true)

        val mockedPublication: Publication =
            publicationService.create(arrangeSetup.publication.project!!.ref, arrangeSetup.publicationForm)

        Assertions.assertNotNull(mockedPublication)
        Assertions.assertEquals(arrangeSetup.newPublication.comment, mockedPublication.comment)
        Assertions.assertEquals(arrangeSetup.newPublication.version, mockedPublication.version)
    }

    @Test
    fun delete() {
        Mockito
            .`when`(publicationRepository.deletePublication(projectId, publicationRef))
            .thenReturn(newPublication)

        val mockedPublication: Publication = publicationService.delete(projectRef, publicationRef)

        Assertions.assertNotNull(mockedPublication)
        Assertions.assertEquals(TestSetup.newRequirement.ref, mockedPublication.ref)

    }

    @Test
    fun update() {
        Mockito
            .`when`(
                publicationRepository.findByRef(
                    projectId,
                    publicationRef
                )
            ).thenReturn(publication)

        val form = updatedPublicationForm

        val mockedPublication: Publication = publicationService.update(
            projectRef,
            publicationRef,
            form
        )

        Assertions.assertNotNull(mockedPublication)
        Assertions.assertEquals(form.comment, mockedPublication.comment)
        Assertions.assertEquals(form.version, mockedPublication.version)


    }

}