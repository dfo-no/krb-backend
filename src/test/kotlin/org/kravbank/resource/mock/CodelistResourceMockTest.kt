package org.kravbank.resource.mock

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.CodelistForm
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.resource.CodelistResource
import org.kravbank.service.CodelistService
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_NOTFOUND
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.ws.rs.core.Response


class CodelistResourceMockTest {


    // The first two are mocks - this way we avoid coupling the test to the database
    private val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private val codelistRepository: CodelistRepository = mock(CodelistRepository::class.java)

    // This is just an ordinary, innocent Kotlin class so we just make an instance of it.
    // This both means our test is more realistic, and also means we get to test the service for next to no extra cost.
    private val codelistService = CodelistService(codelistRepository, projectRepository)

    // This is the thing we actually set out to test
    private val codelistResource = CodelistResource(codelistService)

    private val arrangeSetup = TestSetup()

    private lateinit var updateCodelistForm: CodelistForm
    private lateinit var codelist: Codelist
    private lateinit var codelists: MutableList<Codelist>
    private lateinit var project: Project
    private lateinit var createForm: CodelistForm


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()

        updateCodelistForm = arrangeSetup.updatedCodelistForm
        codelist = arrangeSetup.codelist
        codelists = arrangeSetup.codelists
        project = arrangeSetup.project
        createForm = CodelistForm().fromEntity(codelist)

        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(codelistRepository.findByRef(project.id, codelist.ref)).thenReturn(codelist)
        `when`(codelistRepository.listAllCodelists(project.id)).thenReturn(codelists)

    }

    @Test
    fun getCodelist_OK() {
        val response = codelistResource.getCodelistByRef(project.ref, codelist.ref)

        val entity: Codelist = CodelistForm().toEntity(response)

        assertNotNull(response)
        assertEquals(codelist.title, entity.title)
        assertEquals(codelist.description, entity.description)
    }

    @Test
    fun listCodelists_OK() {
        val response = codelistResource.listCodelists(project.ref)

        val entity: List<CodelistForm> = response

        assertNotNull(response)
        assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(codelists[0].title, firstObjectInList.title)
        assertEquals(codelists[0].description, firstObjectInList.description)
    }

    @Test
    fun createCodelist_OK() {
        doNothing()
            .`when`(codelistRepository).persist(ArgumentMatchers.any(Codelist::class.java))

        `when`(codelistRepository.isPersistent(ArgumentMatchers.any(Codelist::class.java)))
            .thenReturn(true)

        val response: Response = codelistResource.createCodelist(project.ref, createForm)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)

    }

    @Test
    fun deleteCodelist_OK() {

        val response: Response = codelistResource.deleteCodelist(project.ref, codelist.ref)

        assertNull(response)
        verify(codelistRepository).deleteById(1L)
    }


    @Test
    fun updateCodelist_OK() {
        val response = codelistResource.updateCodelist(
            project.ref,
            codelist.ref,
            updateCodelistForm
        )

        val entity: Codelist = CodelistForm().toEntity(response)

        assertNotNull(response)
        assertEquals(updateCodelistForm.title, entity.title)
        assertEquals(updateCodelistForm.description, entity.description)

    }


    /**
     *
     * KO's of relevant exceptions.
     *
     */


    @Test
    fun getCodelist_KO() {
        `when`(codelistRepository.findByRef(project.id, codelist.ref))
            .thenThrow(NotFoundException(CODELIST_NOTFOUND))


        val exception = assertThrows(NotFoundException::class.java) {
            codelistResource.getCodelistByRef(
                project.ref,
                codelist.ref
            )
        }
        assertEquals(CODELIST_NOTFOUND, exception.message)
    }


    @Test
    fun createCodelist_KO() {
        val exception = assertThrows(BadRequestException::class.java) {
            codelistResource.createCodelist(
                project.ref,
                createForm,
            )
        }

        assertEquals(CODELIST_BADREQUEST_CREATE, exception.message)
    }


    @Test
    fun updateCodelist_KO() {
        `when`(
            codelistRepository.findByRef(
                project.id,
                codelist.ref
            )
        ).thenThrow(NotFoundException(CODELIST_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            codelistResource.updateCodelist(
                project.ref,
                codelist.ref,
                updateCodelistForm
            )
        }
        assertEquals(CODELIST_NOTFOUND, exception.message)

    }
}

