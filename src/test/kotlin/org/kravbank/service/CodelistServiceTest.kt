package org.kravbank.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.CodelistForm
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.codelists
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.verify


class CodelistServiceTest {

    private val projectRepository: ProjectRepository = Mockito.mock(ProjectRepository::class.java)
    private val codelistRepository: CodelistRepository = Mockito.mock(CodelistRepository::class.java)

    private val codelistService = CodelistService(codelistRepository, projectRepository)

    private val arrangeSetup = TestSetup.Arrange

    private lateinit var createForm: CodelistForm
    private lateinit var updateForm: CodelistForm
    private lateinit var codelist: Codelist
    private lateinit var project: Project


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()

        updateForm = arrangeSetup.updatedCodelistForm
        codelist = arrangeSetup.codelist
        project = arrangeSetup.project
        createForm = CodelistForm().fromEntity(codelist)


        Mockito.`when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        Mockito.`when`(codelistRepository.findByRef(project.id, codelist.ref)).thenReturn(codelist)
        Mockito.`when`(codelistRepository.listAllCodelists(project.id)).thenReturn(codelists)

    }


    @Test
    fun get() {
        val response = codelistService.get(project.ref, codelist.ref)

        assertEquals(codelist.title, response.title)
        assertEquals(codelist.id, response.id)
        assertEquals(codelist.project, response.project)
        assertEquals(codelist.description, response.description)
        assertEquals(2, response.codes.size)
        assertEquals("Første codelist", response.codes[0].title)
        assertEquals("Ny kodelist tittel", response.codes[0].title)
    }

    @Test
    fun list() {
        val response = codelistService.list(project.ref)

        assertNotNull(response)
        Assertions.assertFalse(response.isEmpty())
        val firstObjectInList = response[0]
        assertEquals(codelists[0].title, firstObjectInList.title)
        assertEquals(codelists[0].description, firstObjectInList.description)
        assertEquals(codelists[0].project, firstObjectInList.project)
        assertEquals(codelists[0].codes, firstObjectInList.codes)
    }

    @Test
    fun create() {
        Mockito
            .doNothing()
            .`when`(codelistRepository)
            .persist(ArgumentMatchers.any(Codelist::class.java))

        Mockito
            .`when`(codelistRepository.isPersistent(ArgumentMatchers.any(Codelist::class.java)))
            .thenReturn(true)


        val response =
            codelistService.create(
                arrangeSetup.codelist.project!!.ref,
                createForm
            )

        assertNotNull(response)
        assertEquals(createForm.title, response.title)
        assertEquals(createForm.description, response.description)
    }


    @Test
    fun delete() {
        codelistService.delete(project.ref, codelist.ref)

        verify(codelistRepository).deleteById(codelist.id)
    }

    @Test
    fun update() {
        val response = codelistService.update(
            project.ref,
            codelist.ref,
            updateForm
        )

        assertNotNull(response)
        assertEquals(updateForm.title, response.title)
        assertEquals(updateForm.description, response.description)
    }
}