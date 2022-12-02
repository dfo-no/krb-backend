package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.TestSetup
import org.kravbank.domain.Publication
import org.kravbank.repository.PublicationRepository
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class PublicationServiceTest {

    @InjectMock
    lateinit var publicationRepository: PublicationRepository

    @Inject
    lateinit var publicationService: PublicationService

    val arrangeSetup = TestSetup.Arrange

    @BeforeEach
    fun setUp() {
        arrangeSetup.start()
    }

    @Test
    fun get() {
        Mockito
            .`when`(
                publicationRepository.findByRef(
                    arrangeSetup.project_publicationId,
                    arrangeSetup.publication_projectRef
                )
            )
            .thenReturn(arrangeSetup.publication)

        val mockedPublication: Publication =
            publicationService.get(arrangeSetup.project_publicationRef, arrangeSetup.publication_projectRef)

        Assertions.assertEquals(arrangeSetup.publication.comment, mockedPublication.comment)
        Assertions.assertEquals(200, mockedPublication.id)
        Assertions.assertEquals(arrangeSetup.publication.project, mockedPublication.project)
        Assertions.assertEquals(arrangeSetup.publication.date, mockedPublication.date)
    }

    @Test
    fun list() {
        Mockito.`when`(
            publicationRepository
                .listAllPublications(arrangeSetup.project_publicationId)
        ).thenReturn(arrangeSetup.publications)

        val mockedPublications: List<Publication> = publicationService.list(arrangeSetup.project_publicationRef)

        Assertions.assertEquals(arrangeSetup.publication.comment, mockedPublications[0].comment)
        Assertions.assertEquals(200, mockedPublications[0].id)
        Assertions.assertEquals(arrangeSetup.publication.project, mockedPublications[0].project)
        Assertions.assertEquals(arrangeSetup.publication.date, mockedPublications[0].date)
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

        /*
        //todo: Kommer tilbake til denne testen senere

        Mockito
            .`when`(publicationRepository.deletePublication(setup.publication.id))
            .thenReturn(true)
        Mockito
            .`when`(publicationRepository.isPersistent(ArgumentMatchers.any(Publication::class.java)))
            .thenReturn(false)

        val mockedProject: Publication =
            publicationService.delete(setup.project_publicationRef, setup.publication.ref) //setup.projectref

        //assert
        Assertions.assertNotNull(mockedProject)
        Assertions.assertEquals(setup.project.ref, mockedProject.ref)
        Assertions.assertEquals("første prosjekt", mockedProject.ref)
         */
    }

    @Test
    fun update() {
        Mockito
            .`when`(
                publicationRepository.findByRef(arrangeSetup.project_publicationId, arrangeSetup.publication_projectRef)
            ).thenReturn(arrangeSetup.publication)

        val mockedPublication: Publication = publicationService.update(
            arrangeSetup.project_publicationRef,
            arrangeSetup.publication_projectRef,
            arrangeSetup.updatedPublicationForm
        )

        Assertions.assertNotNull(mockedPublication)
        Assertions.assertEquals(arrangeSetup.updatedPublicationForm.comment, mockedPublication.comment)
    }

}