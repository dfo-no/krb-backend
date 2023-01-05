package org.kravbank.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.Project
import org.kravbank.domain.Publication
import org.kravbank.domain.PublicationExport
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationExportRepository
import org.kravbank.repository.PublicationRepository
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*


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


        // `when`(publicationExportRepository.list(anyLong()))
        //    .thenReturn(publicationExports)

        `when`(publicationExportRepository.findByRef(anyLong(), anyString()))
            .thenReturn(publicationExport)

        //  MockitoAnnotations.openMocks(this)

    }

    @Test
    fun get() {

        val response =
            publicationExportService.get(
                project.ref,
                publication.ref,
                publicationExport.ref
            )

        assertEquals(publicationExport.ref, response.ref)
        assertEquals(1, response.content.size)


    }

    @Test
    fun list() {

    }

    @Test
    fun create() {

        doNothing()
            .`when`(publicationExportRepository)
            .persist(ArgumentMatchers.any(PublicationExport::class.java))

        `when`(publicationExportRepository.isPersistent(ArgumentMatchers.any(PublicationExport::class.java)))
            .thenReturn(true)

        val response = publicationExportService.create(project.ref, publication.ref)

        val entity: PublicationExport = response

        assertNotNull(entity)
    }
}