package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.kravbank.dao.CodelistForm
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.resource.CodelistResource
import org.kravbank.service.CodelistService
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_NOTFOUND
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.codelists
import org.kravbank.utils.TestSetup.Arrange.updatedCodelistForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.ws.rs.core.Response


@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class CodelistResourceMockTest {


    // The first two are mocks - this way we avoid coupling the test to the database
    private final val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private final val codelistRepository: CodelistRepository = mock(CodelistRepository::class.java)

    // This is just an ordinary, innocent Kotlin class so we just make an instance of it.
    // This both means our test is more realistic, and also means we get to test the service for next to no extra cost.
    private final val codelistService = CodelistService(codelistRepository, projectRepository)

    // This is the thing we actually set out to test
    val codelistResource = CodelistResource(codelistService)

    private val arrangeSetup = TestSetup.Arrange

    private lateinit var codelist: Codelist
    private lateinit var project: Project


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()
        codelist = arrangeSetup.codelist
        project = arrangeSetup.project
        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(codelistRepository.findByRef(project.id, codelist.ref)).thenReturn(codelist)
    }

    @Test
    fun getCodelist_OK() {
        `when`(
            codelistRepository
                .findByRef(project.id, codelist.ref)
        ).thenReturn(codelist)

        val response: Response =
            codelistResource.getCodelistByRef(project.ref, codelist.ref)

        val entity: Codelist = CodelistForm().toEntity(response.entity as CodelistForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals(codelist.title, entity.title)
        assertEquals(codelist.description, entity.description)
    }

    @Test
    fun getCodelist_KO() {
        `when`(codelistRepository.findByRef(project.id, codelist.ref))
            .thenThrow(NotFoundException(CODELIST_NOTFOUND))
        try {
            codelistResource.getCodelistByRef(
                project.ref,
                codelist.ref
            ).entity as NotFoundException
        } catch (e: Exception) {
            assertEquals(CODELIST_NOTFOUND, e.message)
        }
    }

    @Test
    fun listCodelists_OK() {
        `when`(codelistRepository.listAllCodelists(project.id)).thenReturn(codelists)

        val response: Response = codelistResource.listCodelists(project.ref)

        val entity = response.entity

        // Litt over-the-top her kanskje, men  det viser "type narrowing", kalt "smart casts" i Kotlin
        //
        if (entity is List<*>) {

            assertNotNull(response)
            assertEquals(Response.Status.OK.statusCode, response.status)
            assertNotNull(response.entity)
            assertFalse(entity.isEmpty())
            val firstObjectInList = entity[0]
            if (firstObjectInList is CodelistForm) {
                assertEquals(codelists[0].title, firstObjectInList.title)
                assertEquals(codelists[0].description, firstObjectInList.description)
            } else {
                if (firstObjectInList !== null) {
                    fail("""Expected a list of CodelistForm, but the list contained: ${firstObjectInList::class.java.typeName}""")
                } else {
                    fail("""Expected a list of CodelistForm, but there was no object in the list.""")
                }
            }
        } else {
            fail("""Expected a list of CodelistForm, got: ${entity::class.java.typeName}""")
        }

    }

    @Test
    fun createCodelist_OK() {
        doNothing()
            .`when`(codelistRepository).persist(ArgumentMatchers.any(Codelist::class.java))

        `when`(codelistRepository.isPersistent(ArgumentMatchers.any(Codelist::class.java)))
            .thenReturn(true)

        val form = CodelistForm().fromEntity(arrangeSetup.codelist)

        val response: Response = codelistResource.createCodelist(project.ref, form)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)

    }

    @Test
    fun deleteCodelist_OK() {

        val response: Response = codelistResource.deleteCodelist(project.ref, codelist.ref)

        assertNotNull(response)
        assertEquals(codelist.ref, response.entity)
        verify(codelistRepository).deleteById(1L)
    }


    @Test
    fun deleteCodelist_KO() {

        //TODO (slette fordi vi ikke trenger teste hibernates deleteById metode?)

    }

    @Test
    fun updateCodelist_OK() {

        val response: Response = codelistResource.updateCodelist(
            project.ref,
            codelist.ref,
            updatedCodelistForm
        )

        val entity: Codelist = CodelistForm().toEntity(response.entity as CodelistForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals(updatedCodelistForm.title, entity.title)
    }

    @Test
    fun updateCodelist_KO() {
        `when`(
            codelistRepository.findByRef(
                project.id,
                codelist.ref
            )
        )
            .thenThrow(NotFoundException(CODELIST_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            codelistResource.updateCodelist(
                project.ref,
                codelist.ref,
                updatedCodelistForm
            )
        }

        assertEquals(CODELIST_NOTFOUND, exception.message)

    }
}

