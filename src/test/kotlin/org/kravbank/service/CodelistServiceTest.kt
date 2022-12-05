package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.TestSetup
import org.kravbank.domain.Codelist
import org.kravbank.repository.CodelistRepository
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class CodelistServiceTest {

    @InjectMock
    lateinit var codelistRepository: CodelistRepository

    @Inject
    lateinit var codelistService: CodelistService

    val arrangeSetup = TestSetup.Arrange

    @BeforeEach
    fun setUp() {
        arrangeSetup.start()
    }

    @Test
    fun get() {
        Mockito
            .`when`(
                codelistRepository
                    .findByRef(arrangeSetup.project_codelistId, arrangeSetup.codelist_projectRef)
            ).thenReturn(arrangeSetup.codelist)

        val mockedCodelist: Codelist =
            codelistService.get(arrangeSetup.project_codelistRef, arrangeSetup.codelist_projectRef)

        Assertions.assertEquals(arrangeSetup.codelist.title, mockedCodelist.title)
        Assertions.assertEquals(arrangeSetup.codelist.id, mockedCodelist.id)
        Assertions.assertEquals(arrangeSetup.codelist.project, mockedCodelist.project)
        Assertions.assertEquals(arrangeSetup.codelist.description, mockedCodelist.description)
    }

    @Test
    fun list() {
        Mockito.`when`(
            codelistRepository
                .listAllCodelists(arrangeSetup.project_codelistId)
        ).thenReturn(arrangeSetup.codelists)

        val mockedCodelists: List<Codelist> = codelistService.list(arrangeSetup.project_codelistRef)

        Assertions.assertEquals(arrangeSetup.codelist.title, mockedCodelists[0].title)
        Assertions.assertEquals(arrangeSetup.codelist.description, mockedCodelists[0].description)
        //Assertions.assertEquals(arrangeSetup.codelist.ref, mockedCodelists[0].ref)
        Assertions.assertEquals(arrangeSetup.codelist.project, mockedCodelists[0].project)
    }

    @Test
    fun create() {
        Mockito
            .doNothing()
            .`when`(codelistRepository)
            .persist(ArgumentMatchers.any(Codelist::class.java))
        Mockito
            .`when`(codelistRepository.isPersistent(ArgumentMatchers.any(Codelist::class.java)))
            .thenReturn(true)

        val mockedCodelist: Codelist =
            codelistService.create(arrangeSetup.codelist.project!!.ref, arrangeSetup.codelistForm)

        Assertions.assertNotNull(mockedCodelist)
        Assertions.assertEquals(arrangeSetup.newCodelist.title, mockedCodelist.title)
        Assertions.assertEquals(arrangeSetup.newCodelist.description, mockedCodelist.description)
    }

    @Test
    fun delete() {
        Mockito
            .`when`(
                codelistRepository.deleteCodelist(
                    arrangeSetup.project_codelistId,
                    arrangeSetup.codelist_projectRef
                )
            )
            .thenReturn(arrangeSetup.newCodelist_2)

        val mockedCodelist: Codelist =
            codelistService.delete(arrangeSetup.project_codelistRef, arrangeSetup.codelist_projectRef)

        Assertions.assertNotNull(mockedCodelist)
        Assertions.assertEquals(TestSetup.newCodelist_2, mockedCodelist)
    }

    @Test
    fun update() {
        Mockito
            .`when`(
                codelistRepository
                    .findByRef(arrangeSetup.project_codelistId, arrangeSetup.codelist_projectRef)
            ).thenReturn(arrangeSetup.codelist)

        val mockedCodelist: Codelist = codelistService.update(
            arrangeSetup.project_codelistRef,
            arrangeSetup.codelist_projectRef,
            arrangeSetup.updatedCodelistForm
        )

        Assertions.assertNotNull(mockedCodelist)
        Assertions.assertEquals(arrangeSetup.updatedCodelistForm.title, mockedCodelist.title)
        Assertions.assertEquals(arrangeSetup.updatedCodelistForm.description, mockedCodelist.description)
    }
}