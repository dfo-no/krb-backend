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

    val setup = TestSetup.SetDomains

    @BeforeEach
    fun setUp() {
        setup.arrange()
    }

    @Test
    fun get() {
        Mockito
            .`when`(
                needRepository
                    .findByRef(setup.project_needId, setup.need_projectRef)
            ).thenReturn(setup.need)

        val mockedNeed: Need =
            needService.get(setup.project_needRef, setup.need_projectRef)

        Assertions.assertEquals(setup.need.title, mockedNeed.title)
        Assertions.assertEquals(setup.need.id, mockedNeed.id)
        Assertions.assertEquals(setup.need.project, mockedNeed.project)
        Assertions.assertEquals(setup.need.description, mockedNeed.description)
    }

    @Test
    fun list() {
        Mockito.`when`(
            needRepository
                .listAllNeeds(setup.project_needId)
        ).thenReturn(setup.needs)

        val mockedNeeds: List<Need> = needService.list(setup.project_needRef)

        Assertions.assertEquals(setup.need.title, mockedNeeds[0].title)
        Assertions.assertEquals(setup.need.description, mockedNeeds[0].description)
        Assertions.assertEquals(setup.need.ref, mockedNeeds[0].ref)
        Assertions.assertEquals(setup.need.requirements, mockedNeeds[0].requirements)
        Assertions.assertEquals(setup.need.project, mockedNeeds[0].project)
        Assertions.assertEquals(setup.project.requirements, mockedNeeds[0].requirements)
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
            needService.create(setup.need.project!!.ref, setup.needForm)

        Assertions.assertNotNull(mockedNeed)
        Assertions.assertEquals(setup.newNeed.title, mockedNeed.title)
        Assertions.assertEquals(setup.newNeed.description, mockedNeed.description)
        Assertions.assertEquals(setup.newNeed.requirements, mockedNeed.requirements)
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
                    .findByRef(setup.project_needId, setup.need_projectRef)
            ).thenReturn(setup.need)

        val mockedNeed: Need = needService.update(
            setup.project_needRef,
            setup.need_projectRef,
            setup.updatedNeedForm
        )

        Assertions.assertNotNull(mockedNeed)
        Assertions.assertEquals(setup.updatedNeedForm.title, mockedNeed.title)
        Assertions.assertEquals(setup.updatedNeedForm.description, mockedNeed.description)
    }
}