package org.kravbank.resource.mock

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.CodeForm
import org.kravbank.domain.Code
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.CodeRepository
import org.kravbank.repository.CodelistRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.resource.CodeResource
import org.kravbank.service.CodeService
import org.kravbank.utils.Messages.RepoErrorMsg.CODE_BADREQUEST_DELETE
import org.kravbank.utils.Messages.RepoErrorMsg.CODE_NOTFOUND
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.code
import org.kravbank.utils.TestSetup.Arrange.codeForm
import org.kravbank.utils.TestSetup.Arrange.codes
import org.kravbank.utils.TestSetup.Arrange.newCode
import org.kravbank.utils.TestSetup.Arrange.updatedCodeForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.ws.rs.core.Response

internal class CodeResourceMockTest {
    private val codeRepository = mock(CodeRepository::class.java)
    private val codelistRepository = mock(CodelistRepository::class.java)
    private val projectRepository = mock(ProjectRepository::class.java)

    private val codeService = CodeService(codeRepository, codelistRepository, projectRepository)
    private val codeResource: CodeResource = CodeResource(codeService)

    val arrangeSetup = TestSetup.Arrange

    private val codelistId: Long = arrangeSetup.codelist_codeId
    private val codelistRef: String = arrangeSetup.codelist_codeRef
    private val codeRef: String = arrangeSetup.code_codelistRef
    private val projectRef: String = arrangeSetup.project_codeRef

    @BeforeEach
    fun setUp() {

        arrangeSetup.start()
        `when`(projectRepository.findByRef(anyString())).thenReturn(arrangeSetup.project)
        `when`(codelistRepository.findByRef(anyLong(), anyString())).thenReturn(arrangeSetup.codelist)
        `when`(codeRepository.findByRef((anyLong()), anyString())).thenReturn(arrangeSetup.code)
        `when`(codeRepository.listAllCodes(anyLong())).thenReturn(arrangeSetup.codes)

    }


    @Test
    fun getCode_OK() {
        `when`(
                codeRepository.findByRef(
                    codelistId,
                    codeRef
                )
            ).thenReturn(code)

        val response = codeResource.getCode(
            projectRef,
            codelistRef,
            codeRef
        )

        assertNotNull(response)

        val entity: Code = CodeForm().toEntity(response)


        assertEquals(code.title, entity.title)
        assertEquals(code.description, entity.description)

    }

    @Test
    fun listCodes_OK() {
        `when`(codeRepository.listAllCodes(codelistId))
            .thenReturn(codes)
        val response = codeResource.listCodes(
            projectRef,
            codelistRef
        )

        val entity: List<CodeForm> = response

        assertNotNull(response)
        assertFalse(entity.isEmpty())
        assertEquals(codes[0].title, entity[0].title)
        assertEquals(codes[0].description, entity[0].description)
    }

    @Test
    fun createCode_OK() {
        doNothing()
            .`when`(codeRepository)
            .persist(ArgumentMatchers.any(Code::class.java))
        `when`(codeRepository.isPersistent(any(Code::class.java)))
            .thenReturn(true)

        val response: Response = codeResource.createCode(
            projectRef,
            codelistRef,
            codeForm
        )

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun deleteCode_OK() {
        `when`(codeRepository.deleteCode(anyLong(), anyString()))
            .thenReturn(newCode)

        val response: Response = codeResource.deleteCode(projectRef, codelistRef, codeRef)

        assertNotNull(response)
        assertEquals(newCode.ref, response.entity)
    }

    @Test
    fun deleteCode_KO() {
        `when`(codeRepository.deleteCode(anyLong(), anyString()))
            .thenThrow(BadRequestException(CODE_BADREQUEST_DELETE))
        try {
            codeResource.deleteCode(projectRef, codelistRef, codeRef).entity as BadRequestException
        } catch (e: Exception) {
            assertEquals(CODE_BADREQUEST_DELETE, e.message)
        }
    }

    @Test
    fun updateCode_OK() {
        `when`(
                codeRepository.findByRef(
                    codelistId,
                    codeRef
                )
            )
            .thenReturn(arrangeSetup.newCode)

        val response = codeResource.updateCode(
            projectRef,
            codelistRef,
            codeRef,
            updatedCodeForm
        )

        val entity: Code = CodeForm().toEntity(response)

        assertNotNull(response)
        assertEquals(updatedCodeForm.title, entity.title)
        assertEquals(updatedCodeForm.description, entity.description)
    }

    @Test
    fun getCode_KO() {
        `when`(codeRepository.findByRef(codelistId, codeRef))
            .thenThrow(NotFoundException(CODE_NOTFOUND))
        try {

            codeResource.getCode(
                projectRef,
                codelistRef,
                codeRef
            )

        } catch (e: Exception) {
            assertEquals(CODE_NOTFOUND, e.message)
        }
    }
}