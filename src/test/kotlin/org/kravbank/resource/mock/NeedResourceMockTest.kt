package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.NeedForm
import org.kravbank.domain.Need
import org.kravbank.repository.NeedRepository
import org.kravbank.resource.NeedResource
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.need
import org.kravbank.utils.TestSetup.Arrange.needs
import org.kravbank.utils.TestSetup.Arrange.newNeed
import org.kravbank.utils.TestSetup.Arrange.updatedNeedForm
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

    private final val arrangeSetup = TestSetup.Arrange

    private val projectId: Long = arrangeSetup.project_needId
    private val projectRef: String = arrangeSetup.project_needRef
    private val needRef: String = arrangeSetup.need_projectRef

    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }


    @Test
    fun getNeed_OK() {
        Mockito.`when`(
            needRepository.findByRef(
                projectId,
                needRef
            )
        ).thenReturn(need)

        val response = needResource.getNeed(projectRef, needRef)

        val entity: Need = NeedForm().toEntity(response)

        assertNotNull(response)
        assertEquals(need.title, entity.title)
        assertEquals(need.description, entity.description)
    }

    @Test
    fun listNeeds_OK() {
        Mockito.`when`(needRepository.listAllNeeds(projectId)).thenReturn(needs)

        val response = needResource.listNeeds(projectRef)

        assertNotNull(response)
        assertFalse(response.isEmpty())
        assertEquals(needs[0].title, response[0].title)
        assertEquals(needs[0].description, response[0].description)
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

        val response = needResource.createNeed(projectRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun deleteNeed_OK() {
        Mockito
            .`when`(needRepository.deleteNeed(projectId, needRef))
            .thenReturn(newNeed)

        val response = needResource.deleteNeed(projectRef, needRef)

        assertNotNull(response)
        assertEquals(newNeed.ref, response.entity.toString())

    }

    @Test
    fun updateNeed_OK() {
        Mockito
            .`when`(needRepository.findByRef(projectId, needRef))
            .thenReturn(newNeed)

        val response = needResource.updateNeed(
            projectRef,
            needRef,
            updatedNeedForm
        )

        val entity: Need = NeedForm().toEntity(response)

        assertNotNull(response)
        assertEquals(updatedNeedForm.title, entity.title)
        assertEquals(updatedNeedForm.description, entity.description)
    }
}