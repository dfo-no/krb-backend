package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
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

@QuarkusTest
internal class CodelistServiceTest {

    private final val projectRepository: ProjectRepository = Mockito.mock(ProjectRepository::class.java)
    private final val codelistRepository: CodelistRepository = Mockito.mock(CodelistRepository::class.java)

    private final val codelistService = CodelistService(codelistRepository, projectRepository)

    private val arrangeSetup = TestSetup.Arrange

    private lateinit var createCodelistForm: CodelistForm
    private lateinit var updateCodelistForm: CodelistForm
    private lateinit var codelist: Codelist
    private lateinit var project: Project


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()

        updateCodelistForm = arrangeSetup.updatedCodelistForm
        createCodelistForm = arrangeSetup.codelistForm
        codelist = arrangeSetup.codelist
        project = arrangeSetup.project


        Mockito.`when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        Mockito.`when`(codelistRepository.findByRef(project.id, codelist.ref)).thenReturn(codelist)
        Mockito.`when`(codelistRepository.listAllCodelists(project.id)).thenReturn(codelists)

    }


    @Test
    fun get() {
        val response = codelistService.get(project.ref, codelist.ref)

        val entity: Codelist = response

        assertEquals(codelist.title, entity.title)
        assertEquals(codelist.id, entity.id)
        assertEquals(codelist.project, entity.project)
        assertEquals(codelist.description, entity.description)
    }

    @Test
    fun list() {
        val response = codelistService.list(project.ref)

        val entity: List<Codelist> = response

        assertNotNull(response)
        Assertions.assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(codelists[0].title, firstObjectInList.title)
        assertEquals(codelists[0].description, firstObjectInList.description)
        assertEquals(codelists[0].project, firstObjectInList.project)
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
                createCodelistForm
            )

        val entity: Codelist = response

        assertNotNull(entity)
        assertEquals(createCodelistForm.title, entity.title)
        assertEquals(createCodelistForm.description, entity.description)
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
            updateCodelistForm
        )

        val entity: Codelist = response

        assertNotNull(response)
        assertEquals(updateCodelistForm.title, entity.title)
        assertEquals(updateCodelistForm.description, entity.description)
    }
}