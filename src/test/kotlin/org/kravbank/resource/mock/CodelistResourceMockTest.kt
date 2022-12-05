package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.kravbank.TestSetup
import org.kravbank.TestSetup.Arrange.codelist
import org.kravbank.TestSetup.Arrange.codelists
import org.kravbank.TestSetup.Arrange.newCodelist_2
import org.kravbank.TestSetup.Arrange.updatedCodelistForm
import org.kravbank.dao.CodelistForm
import org.kravbank.domain.Codelist
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.CodelistRepository
import org.kravbank.resource.CodelistResource
import org.kravbank.utils.ErrorMessage.RepoError.CODELIST_BADREQUEST_DELETE
import org.kravbank.utils.ErrorMessage.RepoError.CODELIST_NOTFOUND
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class CodelistResourceMockTest {

    @InjectMock
    lateinit var codelistRepository: CodelistRepository

    @Inject
    lateinit var codelistResource: CodelistResource

    private final val arrangeSetup = TestSetup.Arrange

    private final val projectId: Long = arrangeSetup.project_codelistId
    private final val projectRef: String = arrangeSetup.project_codelistRef
    private final val codelistRef: String = arrangeSetup.codelist_projectRef

    @BeforeEach
    fun setUp() {
        arrangeSetup.start()
    }

    @Test
    @Order(1)
    fun getCodelist_OK() {
        Mockito
            .`when`(
                codelistRepository
                    .findByRef(projectId, codelistRef)
            ).thenReturn(codelist)

        val response: Response =
            codelistResource.getCodelistByRef(projectRef, codelistRef)

        val entity: Codelist = CodelistForm().toEntity(response.entity as CodelistForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals(codelist.title, entity.title)
        assertEquals(codelist.description, entity.description)
    }

    @Test
    fun getCodelist_KO() {
        Mockito
            .`when`(codelistRepository.findByRef(projectId, codelistRef))
            .thenThrow(NotFoundException(CODELIST_NOTFOUND))
        try {
            codelistResource.getCodelistByRef(
                projectRef,
                codelistRef
            ).entity as NotFoundException
        } catch (e: Exception) {
            assertEquals(CODELIST_NOTFOUND, e.message)
        }
    }

    @Test
    fun listCodelists_OK() {
        Mockito
            .`when`(codelistRepository.listAllCodelists(projectId))
            .thenReturn(codelists)

        val response: Response = codelistResource.listCodelists(projectRef)

        val entity: List<CodelistForm> = response.entity as List<CodelistForm>

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals(codelists[0].title, entity[0].title)
        assertEquals(codelists[0].description, entity[0].description)
    }

    @Test
    fun createCodelist_OK() {
        Mockito
            .doNothing()
            .`when`(codelistRepository).persist(ArgumentMatchers.any(Codelist::class.java))
        Mockito
            .`when`(codelistRepository.isPersistent(ArgumentMatchers.any(Codelist::class.java)))
            .thenReturn(true)

        val form = CodelistForm().fromEntity(arrangeSetup.codelist)

        val response: Response = codelistResource.createCodelist(projectRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)

    }

    @Test
    fun deleteCodelist_OK() {
        Mockito
            .`when`(
                codelistRepository.deleteCodelist(
                    projectId,
                    codelistRef
                )
            )
            .thenReturn(newCodelist_2)

        val response: Response =
            codelistResource.deleteCodelist(projectRef, codelistRef)

        assertNotNull(response)
        assertEquals(newCodelist_2.ref, response.entity)
    }

    @Test
    fun deleteCodelist_KO() {
        Mockito
            .`when`(
                codelistRepository.deleteCodelist(
                    projectId,
                    codelistRef
                )
            )
            .thenThrow(BadRequestException(CODELIST_BADREQUEST_DELETE))
        try {

            codelistResource.deleteCodelist(
                projectRef,
                codelistRef
            ).entity as BadRequestException

        } catch (e: Exception) {
            assertEquals(CODELIST_BADREQUEST_DELETE, e.message)
        }
    }

    @Test
    fun updateCodelist_OK() {
        Mockito
            .`when`(codelistRepository.findByRef(projectId, codelistRef))
            .thenReturn(codelist)

        val response: Response = codelistResource.updateCodelist(
            projectRef,
            codelistRef,
            updatedCodelistForm
        )

        val entity: Codelist = CodelistForm().toEntity(response.entity as CodelistForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals(updatedCodelistForm.title, entity.title)
    }

    @Test
    fun updateCodelist_KO() {
        Mockito
            .`when`(
                codelistRepository.findByRef(
                    projectId,
                    codelistRef
                )
            )
            .thenThrow(NotFoundException(CODELIST_NOTFOUND))
        try {
            codelistResource.updateCodelist(
                projectRef,
                codelistRef,
                updatedCodelistForm
            ).entity as NotFoundException
        } catch (e: Exception) {
            assertEquals(CODELIST_NOTFOUND, e.message)
        }
    }
}