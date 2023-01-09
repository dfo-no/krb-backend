package org.kravbank.resource.mock

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.NeedForm
import org.kravbank.domain.Need
import org.kravbank.domain.Project
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.resource.NeedResource
import org.kravbank.service.NeedService
import org.kravbank.utils.Messages.RepoErrorMsg.NEED_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.NEED_NOTFOUND
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.updatedNeedForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.ws.rs.core.Response

class NeedResourceMockTest {

    private val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private val needRepository: NeedRepository = mock(NeedRepository::class.java)

    private val needService = NeedService(needRepository, projectRepository)

    private val needResource = NeedResource(needService)

    private val arrangeSetup = TestSetup.Arrange


    private lateinit var updateNeedForm: NeedForm
    private lateinit var needs: List<Need>
    private lateinit var need: Need
    private lateinit var project: Project
    private lateinit var createForm: NeedForm


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()


        updateNeedForm = arrangeSetup.updatedNeedForm
        needs = arrangeSetup.needs
        need = arrangeSetup.need
        project = arrangeSetup.project
        createForm = NeedForm().fromEntity(need)


        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(needRepository.findByRef(project.id, need.ref)).thenReturn(need)
        `when`(needRepository.listAllNeeds(project.id)).thenReturn(needs)


    }


    @Test
    fun getNeed_OK() {
        val response = needResource.getNeed(project.ref, need.ref)

        val entity = NeedForm().toEntity(response)

        assertNotNull(response)
        assertEquals(need.title, entity.title)
        assertEquals(need.description, entity.description)
    }

    @Test
    fun listNeeds_OK() {
        val response = needResource.listNeeds(project.ref)

        assertNotNull(response)
        assertFalse(response.isEmpty())
        val firstObjectInList = response[0]
        assertEquals(needs[0].title, firstObjectInList.title)
        assertEquals(needs[0].description, firstObjectInList.description)

    }


    @Test
    fun createNeed_OK() {
        doNothing()
            .`when`(needRepository)
            .persist(ArgumentMatchers.any(Need::class.java))

        `when`(needRepository.isPersistent(ArgumentMatchers.any(Need::class.java)))
            .thenReturn(true)

        val response: Response = needResource.createNeed(project.ref, createForm)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun deleteNeed_OK() {
        val response: Response = needResource.deleteNeed(project.ref, need.ref)

        assertNotNull(response)
        assertEquals(need.ref, response.entity)
        verify(needRepository).deleteById(need.id)

    }

    @Test
    fun updateNeed_OK() {
        val response = needResource.updateNeed(
            project.ref,
            need.ref,
            updateNeedForm
        )

        val entity = NeedForm().toEntity(response)

        assertNotNull(response)
        assertEquals(updatedNeedForm.title, entity.title)
        assertEquals(updatedNeedForm.description, entity.description)
    }


    /**
     *
     * KO's of relevant exceptions.
     *
     */

    @Test
    fun getNeed_KO() {
        `when`(needRepository.findByRef(project.id, need.ref))
            .thenThrow(NotFoundException(NEED_NOTFOUND))


        val exception = assertThrows(NotFoundException::class.java) {
            needResource.getNeed(
                project.ref,
                need.ref
            )
        }
        assertEquals(NEED_NOTFOUND, exception.message)
    }

    @Test
    fun createNeed_KO() {
        val exception = assertThrows(BadRequestException::class.java) {
            needResource.createNeed(
                project.ref,
                createForm,
            )
        }

        assertEquals(NEED_BADREQUEST_CREATE, exception.message)
    }

    @Test
    fun updateNeed_KO() {
        `when`(
            needRepository.findByRef(
                project.id,
                need.ref
            )
        ).thenThrow(NotFoundException(NEED_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            needResource.updateNeed(
                project.ref,
                need.ref,
                updateNeedForm
            )
        }

        assertEquals(NEED_NOTFOUND, exception.message)
    }
}