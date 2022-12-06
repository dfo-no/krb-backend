package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.Need
import org.kravbank.repository.NeedRepository
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.need
import org.kravbank.utils.TestSetup.Arrange.needForm
import org.kravbank.utils.TestSetup.Arrange.needs
import org.kravbank.utils.TestSetup.Arrange.updatedNeedForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class NeedServiceTest {

    @InjectMock
    lateinit var needRepository: NeedRepository

    @Inject
    lateinit var needService: NeedService

    private val arrangeSetup = TestSetup.Arrange

    private val projectId: Long = arrangeSetup.project_needId

    private val needRef: String = arrangeSetup.need_projectRef

    private val projectRef: String = arrangeSetup.project_needRef


    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }

    @Test
    fun get() {
        Mockito
            .`when`(
                needRepository
                    .findByRef(projectId, needRef)
            ).thenReturn(need)

        val mockedNeed: Need =
            needService.get(projectRef, needRef)

        Assertions.assertEquals(need.title, mockedNeed.title)
        Assertions.assertEquals(need.id, mockedNeed.id)
        Assertions.assertEquals(need.project, mockedNeed.project)
        Assertions.assertEquals(need.description, mockedNeed.description)
    }

    @Test
    fun list() {
        Mockito
            .`when`(needRepository.listAllNeeds(projectId))
            .thenReturn(needs)

        val mockedNeeds: List<Need> = needService.list(projectRef)

        Assertions.assertEquals(needs[0].title, mockedNeeds[0].title)
        Assertions.assertEquals(needs[0].description, mockedNeeds[0].description)
        Assertions.assertEquals(needs[0].ref, mockedNeeds[0].ref)
        Assertions.assertEquals(needs[0].requirements, mockedNeeds[0].requirements)
        Assertions.assertEquals(needs[0].project, mockedNeeds[0].project)
        Assertions.assertEquals(needs[0].requirements, mockedNeeds[0].requirements)
    }

    @Test
    fun create() {
        Mockito
            .doNothing()
            .`when`(needRepository)
            .persist(ArgumentMatchers.any(Need::class.java))
        Mockito
            .`when`(needRepository.isPersistent(ArgumentMatchers.any(Need::class.java)))
            .thenReturn(true)

        val form = needForm

        val mockedNeed: Need =
            needService.create(arrangeSetup.need.project!!.ref, form)

        Assertions.assertNotNull(mockedNeed)
        Assertions.assertEquals(form.title, mockedNeed.title)
        Assertions.assertEquals(form.description, mockedNeed.description)
    }

    @Test
    fun delete() {
        Mockito
            .`when`(needRepository.deleteNeed(projectId, needRef))
            .thenReturn(need)

        val mockedNeed: Need = needService.delete(
            projectRef,
            needRef
        )

        Assertions.assertNotNull(mockedNeed)
        Assertions.assertEquals(need.ref, mockedNeed.ref)
        Assertions.assertEquals(need.title, mockedNeed.title)
        Assertions.assertEquals(need.description, mockedNeed.description)
        Assertions.assertEquals(need.project, mockedNeed.project)
        Assertions.assertEquals(need.requirements, mockedNeed.requirements)
    }

    @Test
    fun update() {
        Mockito
            .`when`(
                needRepository
                    .findByRef(projectId, needRef)
            ).thenReturn(arrangeSetup.need)

        val form = updatedNeedForm

        val mockedNeed: Need = needService.update(
            projectRef,
            needRef,
            form
        )

        Assertions.assertNotNull(mockedNeed)
        Assertions.assertEquals(form.title, mockedNeed.title)
        Assertions.assertEquals(form.description, mockedNeed.description)
    }
}