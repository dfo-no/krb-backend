package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
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
import org.kravbank.utils.TestSetup.Arrange.needs
import org.kravbank.utils.TestSetup.Arrange.updatedNeedForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@QuarkusTest
internal class NeedServiceTest {

    private final val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private final val needRepository: NeedRepository = mock(NeedRepository::class.java)

    val needService = NeedService(needRepository, projectRepository)

    private val arrangeSetup = TestSetup.Arrange

    private lateinit var createNeedForm: NeedForm
    private lateinit var need: Need
    private lateinit var project: Project

    @BeforeEach
    fun setUp() {
        arrangeSetup.start()

        createNeedForm = arrangeSetup.needForm
        need = arrangeSetup.need
        project = arrangeSetup.project

        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(needRepository.findByRef(project.id, need.ref)).thenReturn(need)

    }


    @Test
    fun get() {
        `when`(
            needRepository
                .findByRef(project.id, need.ref)
        ).thenReturn(need)

        val mockedNeed: Need =
            needService.get(project.ref, need.ref)

        assertEquals(need.title, mockedNeed.title)
        assertEquals(need.id, mockedNeed.id)
        assertEquals(need.project, mockedNeed.project)
        assertEquals(need.description, mockedNeed.description)

    }

    @Test
    fun list() {
        `when`(needRepository.listAllNeeds(project.id)).thenReturn(needs)

        val mockedNeeds: List<Need> = needService.list(project.ref)

        assertEquals(needs[0].title, mockedNeeds[0].title)
        assertEquals(needs[0].description, mockedNeeds[0].description)
        assertEquals(needs[0].ref, mockedNeeds[0].ref)
        assertEquals(needs[0].requirements, mockedNeeds[0].requirements)
        assertEquals(needs[0].project, mockedNeeds[0].project)
        assertEquals(needs[0].requirements, mockedNeeds[0].requirements)

    }

    @Test
    fun create() {
        doNothing()
            .`when`(needRepository)
            .persist(ArgumentMatchers.any(Need::class.java))

        `when`(needRepository.isPersistent(ArgumentMatchers.any(Need::class.java)))
            .thenReturn(true)


        val mockedNeed: Need =
            needService.create(arrangeSetup.need.project!!.ref, createNeedForm)

        assertNotNull(mockedNeed)
        assertEquals(createNeedForm.title, mockedNeed.title)
        assertEquals(createNeedForm.description, mockedNeed.description)

    }


    @Test
    fun delete() {
        needService.delete(project.ref, need.ref)

        verify(needRepository).deleteById(need.id)

    }

    @Test
    fun update() {

        val form = updatedNeedForm

        val response = needService.update(
            project.ref,
            need.ref,
            form
        )

        val entity: Need = response


        assertNotNull(entity)
        assertEquals(form.title, entity.title)
        assertEquals(form.description, entity.description)

    }
}