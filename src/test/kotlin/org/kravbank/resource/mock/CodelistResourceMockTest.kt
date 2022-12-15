package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.kravbank.dao.CodelistForm
import org.kravbank.domain.Codelist
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.CodelistRepository
import org.kravbank.resource.CodelistResource
import org.kravbank.utils.Messages.RepoErrorMsg.CODELIST_NOTFOUND
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.codelist
import org.kravbank.utils.TestSetup.Arrange.codelists
import org.kravbank.utils.TestSetup.Arrange.updatedCodelistForm
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
    /*
        @Test
        fun deleteCodelist_OK() {
            //TODO fiks nullpointer error
            Slettet  deleteCodelist fra codelist repo ,
            brukte i steden panaches  deleteById fra serviceklassen
            codeliste service kaster error foundCodlist er null, noe som er merkelig siden begge metodene returnerer boolean (samme mockito given-setup)

            Mockito
                .`when`(
                    codelistRepository.deleteById(
                        4
                    )
                )
                .thenReturn(true)

            val response: Response =
                codelistResource.deleteCodelist(projectRef, codelistRef)

            assertNotNull(response)
            assertEquals(codelistRef, response.entity)
        }


     */


    /* // TODO
           slett pga panaches egen delete metode?
           eventuelt ikke hvis i skal kaste feil fra serviceklassen
        @Test
        fun deleteCodelist_KO() {
            Mockito
                .`when`(
                    codelistRepository.deleteById(
                        codelist_projectId
                    )
                )
                .thenThrow(BadRequestException(CODELIST_BADREQUEST_DELETE))

            val exception = assertThrows(
                BadRequestException::class.java
            ) {
                codelistResource.deleteCodelist(
                    projectRef,
                    codelistRef
                )
            }

            assertEquals(CODELIST_BADREQUEST_DELETE, exception.message)
        }


     */

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