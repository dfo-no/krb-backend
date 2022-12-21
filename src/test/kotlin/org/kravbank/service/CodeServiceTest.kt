package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.CodeForm
import org.kravbank.domain.Code
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import org.kravbank.repository.CodeRepository
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@QuarkusTest
internal class CodeServiceTest {


    private final val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private final val codeRepository: CodeRepository = mock(CodeRepository::class.java)
    private final val codelistRepository: CodelistRepository = mock(CodelistRepository::class.java)


    val codeService = CodeService(
        codeRepository = codeRepository,
        codelistRepository = codelistRepository,
        projectRepository = projectRepository
    )


    private val arrangeSetup = TestSetup.Arrange

    private lateinit var createCodeForm: CodeForm
    private lateinit var updateCodeForm: CodeForm
    private lateinit var codes: List<Code>
    private lateinit var code: Code
    private lateinit var codelist: Codelist
    private lateinit var project: Project


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()

        updateCodeForm = arrangeSetup.updatedCodeForm
        createCodeForm = arrangeSetup.codeForm
        codes = arrangeSetup.codes
        code = arrangeSetup.code
        codelist = arrangeSetup.codelist
        project = arrangeSetup.project


        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(codelistRepository.findByRef(project.id, codelist.ref)).thenReturn(codelist)
        `when`(codeRepository.findByRef(codelist.id, code.ref)).thenReturn(code)
        `when`(codeRepository.listAllCodes(codelist.id)).thenReturn(codes)

    }


    @Test
    fun get() {
        val response = codeService.get(project.ref, codelist.ref, code.ref)

        val entity: Code = response

        assertNotNull(entity)
        assertEquals(code.title, entity.title)
        assertEquals(code.id, entity.id)
        assertEquals(code.description, entity.description)
    }

    @Test
    fun list() {
        val response = codeService.list(project.ref, codelist.ref)

        val entity: List<Code> = response


        assertNotNull(response)
        assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(codes[0].title, firstObjectInList.title)
        assertEquals(codes[0].id, firstObjectInList.id)
        assertEquals(codes[0].description, firstObjectInList.description)

    }


    @Test
    fun create() {
        doNothing()
            .`when`(codeRepository)
            .persist(ArgumentMatchers.any(Code::class.java))

        `when`(codeRepository.isPersistent(ArgumentMatchers.any(Code::class.java)))
            .thenReturn(true)


        val mockedCode: Code =
            codeService.create(
                project.ref,
                codelist.ref,
                createCodeForm
            )

        assertNotNull(mockedCode)
        assertEquals(createCodeForm.title, mockedCode.title)
        assertEquals(createCodeForm.description, mockedCode.description)

    }

    @Test
    fun delete() {
        codeService.delete(
            project.ref,
            codelist.ref,
            code.ref
        )

        verify(codeRepository).deleteById(code.id)
    }

    @Test
    fun update() {
        val response = codeService.update(
            project.ref,
            codelist.ref,
            code.ref,
            updateCodeForm
        )

        val entity: Code = response

        assertNotNull(entity)
        assertEquals(updateCodeForm.title, entity.title)
        assertEquals(updateCodeForm.description, entity.description)
    }
}