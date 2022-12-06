package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.CodeForm
import org.kravbank.domain.Code
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.CodeRepository
import org.kravbank.resource.CodeResource
import org.kravbank.utils.Messages.RepoErrorMsg.CODE_BADREQUEST_DELETE
import org.kravbank.utils.Messages.RepoErrorMsg.CODE_NOTFOUND
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.code
import org.kravbank.utils.TestSetup.Arrange.codeForm
import org.kravbank.utils.TestSetup.Arrange.codes
import org.kravbank.utils.TestSetup.Arrange.newCode
import org.kravbank.utils.TestSetup.Arrange.updatedCodeForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class CodeResourceMockTest {

    @InjectMock
    lateinit var codeRepository: CodeRepository

    @Inject
    lateinit var codeResource: CodeResource

    final val arrangeSetup = TestSetup.Arrange

    private final val codelistId: Long = arrangeSetup.codelist_codeId
    private final val codelistRef: String = arrangeSetup.codelist_codeRef
    private final val codeRef: String = arrangeSetup.code_codelistRef
    private final val projectRef: String = arrangeSetup.project_codeRef

    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }


    @Test
    fun getCode_OK() {
        Mockito
            .`when`(
                codeRepository.findByRef(
                    codelistId,
                    codeRef
                )
            ).thenReturn(code)

        val response: Response = codeResource.getCode(
            projectRef,
            codelistRef,
            codeRef
        )

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)

        val entity: Code = CodeForm().toEntity(response.entity as CodeForm)

        assertEquals(code.title, entity.title)
        assertEquals(code.description, entity.description)
    }

    @Test
    fun listCodes_OK() {
        Mockito
            .`when`(codeRepository.listAllCodes(codelistId))
            .thenReturn(codes)
        val response: Response = codeResource.listCodes(
            projectRef,
            codelistRef
        )

        val entity: List<CodeForm> = response.entity as List<CodeForm>

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals(codes[0].title, entity[0].title)
        assertEquals(codes[0].description, entity[0].description)
    }

    @Test
    fun createCode_OK() {
        Mockito
            .doNothing()
            .`when`(codeRepository)
            .persist(ArgumentMatchers.any(Code::class.java))
        Mockito
            .`when`(codeRepository.isPersistent(ArgumentMatchers.any(Code::class.java)))
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
        Mockito
            .`when`(codeRepository.deleteCode(codelistId, codeRef))
            .thenReturn(newCode)

        val response: Response = codeResource.deleteCode(projectRef, codelistRef, codeRef)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals(newCode.ref, response.entity)
    }

    @Test
    fun deleteCode_KO() {
        Mockito
            .`when`(codeRepository.deleteCode(codelistId, codeRef))
            .thenThrow(BadRequestException(CODE_BADREQUEST_DELETE))
        try {
            codeResource.deleteCode(projectRef, codelistRef, codeRef).entity as BadRequestException
        } catch (e: Exception) {
            assertEquals(CODE_BADREQUEST_DELETE, e.message)
        }
    }

    @Test
    fun updateCode_OK() {
        Mockito
            .`when`(
                codeRepository.findByRef(
                    codelistId,
                    codeRef
                )
            )
            .thenReturn(arrangeSetup.newCode)

        val response: Response = codeResource.updateCode(
            projectRef,
            codelistRef,
            codeRef,
            updatedCodeForm
        )

        val entity: Code = CodeForm().toEntity(response.entity as CodeForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals(updatedCodeForm.title, entity.title)
        assertEquals(updatedCodeForm.description, entity.description)
    }

    @Test
    fun getCode_KO() {
        Mockito
            .`when`(codeRepository.findByRef(codelistId, codeRef))
            .thenThrow(NotFoundException(CODE_NOTFOUND))
        try {

            codeResource.getCode(
                projectRef,
                codelistRef,
                codeRef
            )
                .entity as NotFoundException

        } catch (e: Exception) {
            assertEquals(CODE_NOTFOUND, e.message)
        }
    }
}