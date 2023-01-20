package org.kravbank.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.NeedForm
import org.kravbank.domain.Need
import org.kravbank.domain.Project
import org.kravbank.repository.NeedRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*


class NeedServiceTest {

    private val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private val needRepository: NeedRepository = mock(NeedRepository::class.java)

    private val needService = NeedService(needRepository, projectRepository)

    private val arrangeSetup = TestSetup.Arrange

    private lateinit var needs: List<Need>
    private lateinit var need: Need
    private lateinit var project: Project
    private lateinit var createForm: NeedForm
    private lateinit var updateForm: NeedForm


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()

        need = arrangeSetup.need
        needs = arrangeSetup.needs
        project = arrangeSetup.project
        updateForm = arrangeSetup.updatedNeedForm
        createForm = NeedForm().fromEntity(need)


        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(needRepository.findByRef(project.id, need.ref)).thenReturn(need)
        `when`(needRepository.listAllNeeds(project.id)).thenReturn(needs)

    }


    @Test
    fun get() {
        val mockedNeed: Need =
            needService.get(project.ref, need.ref)

        assertEquals(need.title, mockedNeed.title)
        assertEquals(need.id, mockedNeed.id)
        assertEquals(need.project, mockedNeed.project)
        assertEquals(need.description, mockedNeed.description)

    }

    @Test
    fun list() {
        val response = needService.list(project.ref)

        val entity: List<Need> = response

        assertNotNull(response)
        Assertions.assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(needs[0].title, firstObjectInList.title)
        assertEquals(needs[0].description, firstObjectInList.description)
        assertEquals(needs[0].ref, firstObjectInList.ref)
        assertEquals(needs[0].requirements, firstObjectInList.requirements)
        assertEquals(needs[0].project, firstObjectInList.project)
        assertEquals(needs[0].requirements, firstObjectInList.requirements)

    }

    @Test
    fun create() {
        doNothing()
            .`when`(needRepository)
            .persist(ArgumentMatchers.any(Need::class.java))

        `when`(needRepository.isPersistent(ArgumentMatchers.any(Need::class.java)))
            .thenReturn(true)


        val mockedNeed: Need =
            needService.create(arrangeSetup.need.project!!.ref, createForm)

        assertNotNull(mockedNeed)
        assertEquals(createForm.title, mockedNeed.title)
        assertEquals(createForm.description, mockedNeed.description)

    }


    @Test
    fun delete() {
        needService.delete(project.ref, need.ref)

        verify(needRepository).deleteById(need.id)

    }

    @Test
    fun update() {

        val response = needService.update(
            project.ref,
            need.ref,
            updateForm
        )

        val entity: Need = response


        assertNotNull(entity)
        assertEquals(updateForm.title, entity.title)
        assertEquals(updateForm.description, entity.description)

    }
}