package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.PublicationForm
import org.kravbank.domain.Project
import org.kravbank.domain.Publication
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationRepository
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@QuarkusTest
internal class PublicationServiceTest {

    private final val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private final val publicationRepository: PublicationRepository = mock(PublicationRepository::class.java)


    val publicationService = PublicationService(
        publicationRepository = publicationRepository,
        projectRepository = projectRepository
    )


    private val arrangeSetup = TestSetup.Arrange

    private lateinit var createForm: PublicationForm
    private lateinit var updateForm: PublicationForm
    private lateinit var publications: List<Publication>
    private lateinit var publication: Publication
    private lateinit var project: Project


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()


        publications = arrangeSetup.publications
        publication = arrangeSetup.publication
        project = arrangeSetup.project
        updateForm = arrangeSetup.updatedPublicationForm
        createForm = arrangeSetup.publicationForm


        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(publicationRepository.findByRef(project.id, publication.ref)).thenReturn(publication)
        `when`(publicationRepository.listAllPublications(project.id)).thenReturn(publications)
    }


    @Test
    fun get() {
        val response =
            publicationService.get(
                project.ref,
                publication.ref
            )

        val entity: Publication = response

        assertEquals(publication.ref, entity.ref)
        assertEquals(publication.comment, entity.comment)
        assertEquals(publication.id, entity.id)
        assertEquals(publication.project, entity.project)
        assertEquals(publication.date, entity.date)
        assertEquals(publication.version, entity.version)

    }

    @Test
    fun list() {

        val response = publicationService.list(project.ref)

        val entity: List<Publication> = response

        assertNotNull(response)
        assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(publications[0].ref, firstObjectInList.ref)
        assertEquals(publications[0].comment, firstObjectInList.comment)
        assertEquals(publications[0].id, firstObjectInList.id)
        assertEquals(publications[0].project, firstObjectInList.project)
        assertEquals(publications[0].date, firstObjectInList.date)
        assertEquals(publications[0].version, firstObjectInList.version)

    }

    @Test
    fun create() {
        doNothing()
            .`when`(publicationRepository)
            .persist(ArgumentMatchers.any(Publication::class.java))

        `when`(publicationRepository.isPersistent(ArgumentMatchers.any(Publication::class.java)))
            .thenReturn(true)

        val response =
            publicationService.create(project.ref, createForm)

        val entity: Publication = response

        val lastUsedVersion = publications.maxByOrNull { publication -> publication.version }!!.version

        assertNotNull(entity)
        assertEquals(createForm.comment, entity.comment)
        assertEquals(lastUsedVersion + 1, entity.version) //next version in list of publications

    }

    @Test
    fun delete() {
        publicationService.delete(project.ref, publication.ref)

        verify(publicationRepository).deleteById(publication.id)

    }

    @Test
    fun update() {
        val response = publicationService.update(
            project.ref,
            publication.ref,
            updateForm
        )

        val entity: Publication = response

        assertNotNull(entity)
        assertEquals(updateForm.comment, entity.comment)
    }
}