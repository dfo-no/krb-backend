package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.CodeForm
import org.kravbank.domain.Code
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.CodeRepository
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.resource.CodeResource
import org.kravbank.service.CodeService
import org.kravbank.utils.Messages.RepoErrorMsg.CODE_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.CODE_NOTFOUND
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.codes
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class CodeResourceMockTest {


    private final val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private final val codelistRepository: CodelistRepository = mock(CodelistRepository::class.java)
    private final val codeRepository: CodeRepository = mock(CodeRepository::class.java)

    private final val codeService = CodeService(
        codeRepository = codeRepository,
        codelistRepository = codelistRepository,
        projectRepository = projectRepository
    )

    val codeResource = CodeResource(codeService)

    private val arrangeSetup = TestSetup.Arrange

    private lateinit var code: Code
    private lateinit var codelist: Codelist
    private lateinit var project: Project
    private lateinit var updateCodeForm: CodeForm
    private lateinit var createForm: CodeForm

    @BeforeEach
    fun setUp() {
        arrangeSetup.start()


        code = arrangeSetup.code
        codelist = arrangeSetup.codelist
        project = arrangeSetup.project
        updateCodeForm = arrangeSetup.updatedCodeForm
        createForm = CodeForm().fromEntity(code)

        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(codelistRepository.findByRef(project.id, codelist.ref)).thenReturn(codelist)
        `when`(codeRepository.findByRef(codelist.id, code.ref)).thenReturn(code)
        `when`(codeRepository.listAllCodes(codelist.id)).thenReturn(codes)

    }


    @Test
    fun getCode_OK() {
        val response = codeResource.getCode(project.ref, codelist.ref, code.ref)

        val entity: Code = CodeForm().toEntity(response)

        assertNotNull(response)
        assertEquals(code.title, entity.title)
        assertEquals(code.description, entity.description)
    }

    @Test
    fun listCodes_OK() {
        val response = codeResource.listCodes(
            project.ref,
            codelist.ref
        )

        val entity: List<CodeForm> = response

        assertNotNull(response)
        assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(codes[0].title, firstObjectInList.title)
        assertEquals(codes[0].description, firstObjectInList.description)

    }

    @Test
    fun createCode_OK() {
        doNothing()
            .`when`(codeRepository)
            .persist(ArgumentMatchers.any(Code::class.java))

        `when`(codeRepository.isPersistent(ArgumentMatchers.any(Code::class.java)))
            .thenReturn(true)

        val response: Response = codeResource.createCode(
            project.ref,
            codelist.ref,
            createForm
        )

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun deleteCode_OK() {
        val response: Response = codeResource.deleteCode(project.ref, codelist.ref, code.ref)

        assertNotNull(response)
        assertEquals(code.ref, response.entity)
        verify(codeRepository).deleteById(code.id)
    }

    @Test
    fun updateCode_OK() {

        val response = codeResource.updateCode(
            project.ref,
            codelist.ref,
            code.ref,
            updateCodeForm
        )

        val entity: Code = CodeForm().toEntity(response)

        assertNotNull(response)
        assertEquals(updateCodeForm.title, entity.title)
        assertEquals(updateCodeForm.description, entity.description)
    }


    /**
     *
     * Below are KO's of relevant exceptions.
     *
     */


    @Test
    fun getCode_KO() {
        `when`(codeRepository.findByRef(codelist.id, code.ref))
            .thenThrow(NotFoundException(CODE_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            codeResource.getCode(
                project.ref,
                codelist.ref,
                code.ref,
            )
        }

        assertEquals(CODE_NOTFOUND, exception.message)
    }

    @Test
    fun createCode_KO() {
        val exception = assertThrows(BadRequestException::class.java) {
            codeResource.createCode(
                project.ref,
                codelist.ref,
                createForm,
            )
        }

        assertEquals(CODE_BADREQUEST_CREATE, exception.message)
    }


    @Test
    fun updateCode_KO() {
        `when`(
            codelistRepository.findByRef(
                project.id,
                codelist.ref
            )
        ).thenThrow(NotFoundException(CODE_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            codeResource.updateCode(
                project.ref,
                codelist.ref,
                code.ref,
                updateCodeForm
            )
        }
        assertEquals(CODE_NOTFOUND, exception.message)

    }
}
