package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.TestSetup
import org.kravbank.TestSetup.Arrange.need
import org.kravbank.TestSetup.Arrange.needs
import org.kravbank.TestSetup.Arrange.newNeed
import org.kravbank.TestSetup.Arrange.updatedNeedForm
import org.kravbank.dao.NeedForm
import org.kravbank.domain.Need
import org.kravbank.repository.NeedRepository
import org.kravbank.resource.NeedResource
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class NeedResourceMockTest {

    @InjectMock
    lateinit var needRepository: NeedRepository

    @Inject
    lateinit var needResource: NeedResource

    final val arrangeSetup = TestSetup.Arrange

    @BeforeEach
    fun setUp() {
        arrangeSetup.start()
    }

    val projectId = arrangeSetup.project_needId
    val projectRef = arrangeSetup.project_needRef
    val needRef = arrangeSetup.need_projectRef

    @Test
    fun getNeed_OK() {
        Mockito.`when`(
            needRepository.findByRef(
                projectId,
                needRef
            )
        ).thenReturn(need)

        val response: Response = needResource.getNeed(projectRef, needRef)

        val entity: Need = NeedForm().toEntity(response.entity as NeedForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals(need.title, entity.title)
        assertEquals(need.description, entity.description)
    }

    @Test
    fun listNeeds_OK() {
        Mockito.`when`(needRepository.listAllNeeds(projectId)).thenReturn(needs)

        val response: Response = needResource.listNeeds(projectRef)

        val entity: List<NeedForm> = response.entity as List<NeedForm>

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals(needs[0].title, entity[0].title)
        assertEquals(needs[0].description, entity[0].description)
    }

    @Test
    fun createNeed_OK() {
        Mockito
            .doNothing()
            .`when`(needRepository)
            .persist(ArgumentMatchers.any(Need::class.java))

        Mockito
            .`when`(needRepository.isPersistent(ArgumentMatchers.any(Need::class.java)))
            .thenReturn(true)

        val form = NeedForm().fromEntity(need)

        val response: Response = needResource.createNeed(projectRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun deleteNeed_OK() {
        Mockito
            .`when`(needRepository.deleteNeed(projectId, needRef))
            .thenReturn(newNeed)

        val response: Response = needResource.deleteNeed(projectRef, needRef)

        assertNotNull(response)
        assertEquals(newNeed.ref, response.entity.toString())

    }

    @Test
    fun updateNeed_OK() {
        Mockito
            .`when`(needRepository.findByRef(projectId, needRef))
            .thenReturn(newNeed)

        val response: Response = needResource.updateNeed(
            projectRef,
            needRef,
            updatedNeedForm
        )

        val entity: Need = NeedForm().toEntity(response.entity as NeedForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals(updatedNeedForm.title, entity.title)
        assertEquals(updatedNeedForm.description, entity.description)
    }


    /*
Todo:
         Testen(e) kan være nyttig for å teste at feilmeldingene som kastes, behandles på riktig måte.
         Kommer tilbake til den når jeg finner ut av hvorfor mocking ikke gir riktig verdi / ikke-null


     @Test
     fun createNeed_KO() {
         assertFalse(true)
     }


     @Test
     fun getNeed_KO() {
         assertFalse(true)
     }


     @Test
     fun deleteNeed_KO() {
        assertFalse(true)
     }


     @Test
     fun updateNeed_KO() {
         assertFalse(true)
     }

      */
}