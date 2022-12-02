package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.TestSetup
import org.kravbank.domain.Need
import org.kravbank.repository.NeedRepository
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class NeedServiceTest {

    @InjectMock
    lateinit var needRepository: NeedRepository

    @Inject
    lateinit var needService: NeedService

    val arrangeSetup = TestSetup.Arrange

    @BeforeEach
    fun setUp() {
        arrangeSetup.start()
    }

    @Test
    fun get() {
        Mockito
            .`when`(
                needRepository
                    .findByRef(arrangeSetup.project_needId, arrangeSetup.need_projectRef)
            ).thenReturn(arrangeSetup.need)

        val mockedNeed: Need =
            needService.get(arrangeSetup.project_needRef, arrangeSetup.need_projectRef)

        Assertions.assertEquals(arrangeSetup.need.title, mockedNeed.title)
        Assertions.assertEquals(arrangeSetup.need.id, mockedNeed.id)
        Assertions.assertEquals(arrangeSetup.need.project, mockedNeed.project)
        Assertions.assertEquals(arrangeSetup.need.description, mockedNeed.description)
    }

    @Test
    fun list() {
        Mockito.`when`(
            needRepository
                .listAllNeeds(arrangeSetup.project_needId)
        ).thenReturn(arrangeSetup.needs)

        val mockedNeeds: List<Need> = needService.list(arrangeSetup.project_needRef)

        Assertions.assertEquals(arrangeSetup.need.title, mockedNeeds[0].title)
        Assertions.assertEquals(arrangeSetup.need.description, mockedNeeds[0].description)
        Assertions.assertEquals(arrangeSetup.need.ref, mockedNeeds[0].ref)
        Assertions.assertEquals(arrangeSetup.need.requirements, mockedNeeds[0].requirements)
        Assertions.assertEquals(arrangeSetup.need.project, mockedNeeds[0].project)
        Assertions.assertEquals(arrangeSetup.project.requirements, mockedNeeds[0].requirements)
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

        val mockedNeed: Need =
            needService.create(arrangeSetup.need.project!!.ref, arrangeSetup.needForm)

        Assertions.assertNotNull(mockedNeed)
        Assertions.assertEquals(arrangeSetup.newNeed.title, mockedNeed.title)
        Assertions.assertEquals(arrangeSetup.newNeed.description, mockedNeed.description)
    }

    @Test
    fun delete() {
        //todo: Kommer tilbake til denne testen senere
    }

    @Test
    fun update() {
        Mockito
            .`when`(
                needRepository
                    .findByRef(arrangeSetup.project_needId, arrangeSetup.need_projectRef)
            ).thenReturn(arrangeSetup.need)

        val mockedNeed: Need = needService.update(
            arrangeSetup.project_needRef,
            arrangeSetup.need_projectRef,
            arrangeSetup.updatedNeedForm
        )

        Assertions.assertNotNull(mockedNeed)
        Assertions.assertEquals(arrangeSetup.updatedNeedForm.title, mockedNeed.title)
        Assertions.assertEquals(arrangeSetup.updatedNeedForm.description, mockedNeed.description)
    }
}