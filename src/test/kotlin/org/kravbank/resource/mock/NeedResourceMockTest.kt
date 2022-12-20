package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.NeedForm
import org.kravbank.domain.Need
import org.kravbank.domain.Project
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.resource.NeedResource
import org.kravbank.service.NeedService
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.needs
import org.kravbank.utils.TestSetup.Arrange.newNeed
import org.kravbank.utils.TestSetup.Arrange.updatedNeedForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class NeedResourceMockTest {

    private final val projectRepository: ProjectRepository = Mockito.mock(ProjectRepository::class.java)
    private final val needRepository: NeedRepository = Mockito.mock(NeedRepository::class.java)

    private final val needService = NeedService(needRepository, projectRepository)

    val needResource = NeedResource(needService)

    private val arrangeSetup = TestSetup.Arrange

    private lateinit var need: Need
    private lateinit var project: Project

    @BeforeEach
    fun setUp() {
        arrangeSetup.start()

        need = arrangeSetup.need
        project = arrangeSetup.project

        Mockito.`when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        Mockito.`when`(needRepository.findByRef(project.id, need.ref)).thenReturn(need)

    }


    @Test
    fun getNeed_OK() {
        Mockito.`when`(
            needRepository.findByRef(
                need.id,
                need.ref
            )
        ).thenReturn(need)

        val response: Response = needResource.getNeed(project.ref, need.ref)

        val entity: Need = NeedForm().toEntity(response.entity as NeedForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals(need.title, entity.title)
        assertEquals(need.description, entity.description)
    }

    @Test
    fun listNeeds_OK() {
        Mockito.`when`(needRepository.listAllNeeds(project.id)).thenReturn(needs)

        val response: Response = needResource.listNeeds(project.ref)

        val entity = response.entity

        if (entity is List<*>) {

            assertNotNull(response)
            assertEquals(Response.Status.OK.statusCode, response.status)
            assertNotNull(response.entity)
            assertFalse(entity.isEmpty())

            val firstObjectInList = entity[0]
            if (firstObjectInList is NeedForm) {
                assertEquals(needs[0].title, firstObjectInList.title)
                assertEquals(needs[0].description, firstObjectInList.description)
            } else {
                if (firstObjectInList !== null) {
                    org.junit.jupiter.api.fail("""Expected a list of NeedForm, but the list contained: ${firstObjectInList::class.java.typeName}""")
                } else {
                    org.junit.jupiter.api.fail("""Expected a list of NeedForm, but there was no object in the list.""")
                }
            }
        } else {
            org.junit.jupiter.api.fail("""Expected a list of NeedForm, got: ${entity::class.java.typeName}""")
        }
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

        val response: Response = needResource.createNeed(project.ref, form)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun deleteNeed_OK() {

        val response: Response = needResource.deleteNeed(project.ref, need.ref)

        assertNotNull(response)
        assertEquals(need.ref, response.entity)
        Mockito.verify(needRepository).deleteById(need.id)

    }

    @Test
    fun updateNeed_OK() {
        Mockito
            .`when`(needRepository.findByRef(need.id, need.ref))
            .thenReturn(newNeed)

        val response: Response = needResource.updateNeed(
            project.ref,
            need.ref,
            updatedNeedForm
        )

        val entity: Need = NeedForm().toEntity(response.entity as NeedForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals(updatedNeedForm.title, entity.title)
        assertEquals(updatedNeedForm.description, entity.description)
    }
}