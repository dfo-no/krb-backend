package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.Project
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.PublicationExportRepository
import org.kravbank.utils.TestSetup
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@QuarkusTest
internal class PublicationExportServiceTest {


    private final val projectRepository: ProjectRepository = Mockito.mock(ProjectRepository::class.java)

    private final val publicationExportRepository: PublicationExportRepository = Mockito.mock(
        PublicationExportRepository::class.java
    )

    lateinit var publicationExportService: PublicationExportService

    lateinit var project: Project

    private final val arrangeSetup = TestSetup.Arrange


    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

        project = arrangeSetup.project

        `when`(projectRepository.findByRef("ccc4db69-edb2-431f-855a-4368e2bcddd1"))
            .thenReturn(project)

        publicationExportService = PublicationExportService(project, publicationExportRepository)

    }

    @Test
    fun saveBlob() {

        publicationExportService.saveBlob()

        TODO("assert here")

    }

    @Test
    fun getBlob() {
    }
}