package org.kravbank.resource.mock

import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.kravbank.resource.ProjectResource
import org.kravbank.domain.Project
import org.kravbank.repository.ProjectRepository
import java.time.LocalDateTime
import javax.inject.Inject

internal class ProjectResourceTestIT {

    @InjectMock
    lateinit var projectRepository: ProjectRepository
    @Inject
    lateinit var projectResource: ProjectResource

    var project: Project = Project ()

    @BeforeEach
    fun setUp() {
        project = Project()
        project.title = "første prosjekt"
        project.description = "første prosjekt beskrivelse"
        project.version = 2
        project.publishedDate = LocalDateTime.now()
        project.ref ="ccc5db69-edb2-431f-855a-4368e2bcddd1"
    }

    @Test
    fun getProject() {
    }

    @Test
    fun listProjects() {
    }

    @Test
    fun createProject() {
    }

    @Test
    fun deleteProjectByRef() {
    }

    @Test
    fun updateProject() {
    }

    @Test
    fun getProjectService() {
    }
}