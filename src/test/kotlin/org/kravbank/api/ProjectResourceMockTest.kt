package org.kravbank.api

import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test


@QuarkusTest
@QuarkusIntegrationTest
internal class ProjectResourceMockTest {
/*
    @Inject
    var projectService: ProjectService? = null

    @InjectMock
    var bookRepository: BookRepository? = null

    @BeforeEach
    fun setUp() {
        `when`(bookRepository.findBy("Frank Herbert"))
            .thenReturn(
                Arrays.stream(
                    arrayOf(
                        Book("Dune", "Frank Herbert"),
                        Book("Children of Dune", "Frank Herbert")
                    )
                )
            )
    }



    @Test
    fun whenFindByAuthor_thenBooksShouldBeFound() {
        assertEquals(2, libraryService.find("Frank Herbert").size())
    }

 */
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
    fun deleteProjectById() {
    }

    @Test
    fun updateProject() {
    }

    @Test
    fun getProjectService() {
    }
}