package org.kravbank.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.Project
import org.kravbank.domain.Publication
import org.kravbank.domain.PublicationExport
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationExportRepository
import org.kravbank.repository.PublicationRepository
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


internal class PublicationExportServiceTest {


    private val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)

    private val publicationRepository: PublicationRepository = mock(
        PublicationRepository::class.java
    )

    private val publicationExportRepository: PublicationExportRepository = mock(
        PublicationExportRepository::class.java
    )

    private val publicationExportService = PublicationExportService(
        projectRepository = projectRepository,
        publicationExportRepository = publicationExportRepository,
        publicationRepository = publicationRepository
    )


    //@Mock
    //private var publicationExports = ArrayList<PublicationExport>()

    private lateinit var project: Project

    private lateinit var publication: Publication

    private lateinit var publicationExport: PublicationExport

    private val arrangeSetup = TestSetup.Arrange


    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

        project = arrangeSetup.project
        publication = arrangeSetup.publication
        publicationExport = arrangeSetup.publicationExport


        `when`(projectRepository.findByRef(anyString()))
            .thenReturn(project)

        `when`(publicationRepository.findByRef(anyLong(), anyString()))
            .thenReturn(publication)

        `when`(publicationExportRepository.findByRef(anyString(), anyString()))
            .thenReturn(publicationExport)

        //  MockitoAnnotations.openMocks(this)


    }

    @Test
    fun get() {

        /*
        TODO
        val response =
            publicationExportService.get(
                project.ref,
                publication.ref,
                publicationExport.ref
            )

        assertEquals(publicationExport.ref, response.ref)
        assertEquals(1, response.content.size)


         */

    }

    @Test
    fun list() {

    }

    @Test
    fun create() {

        /*
        TODO

        publicationExport.blobFormat = encodeBlob(writeValueAsBytes(project))


        doNothing()
            .`when`(publicationExportRepository)
            .persist(ArgumentMatchers.any(PublicationExport::class.java))

        `when`(publicationExportRepository.isPersistent(ArgumentMatchers.any(PublicationExport::class.java)))
            .thenReturn(true)

        val response = publicationExportService.create(project.ref, publication.ref)

        val entity: PublicationExport = response

        assertNotNull(entity)

         */
    }
}