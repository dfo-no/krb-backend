package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.Codelist
import org.kravbank.repository.CodelistRepository
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.codelist
import org.kravbank.utils.TestSetup.Arrange.codelistForm
import org.kravbank.utils.TestSetup.Arrange.codelists
import org.kravbank.utils.TestSetup.Arrange.updatedCodelistForm
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.verify
import javax.inject.Inject

@QuarkusTest
internal class CodelistServiceTest {

    @InjectMock
    lateinit var codelistRepository: CodelistRepository

    @Inject
    lateinit var codelistService: CodelistService

    private final val arrangeSetup = TestSetup.Arrange

    private final val projectId: Long = arrangeSetup.project_codelistId

    private final val codelistRef: String = arrangeSetup.codelist_projectRef

    private final val projectRef: String = arrangeSetup.project_codelistRef


    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }

    @Test
    fun get() {
        Mockito
            .`when`(
                codelistRepository
                    .findByRef(projectId, codelistRef)
            ).thenReturn(codelist)

        val mockedCodelist: Codelist =
            codelistService.get(projectRef, codelistRef)

        Assertions.assertEquals(codelist.title, mockedCodelist.title)
        Assertions.assertEquals(codelist.id, mockedCodelist.id)
        Assertions.assertEquals(codelist.project, mockedCodelist.project)
        Assertions.assertEquals(codelist.description, mockedCodelist.description)
    }

    @Test
    fun list() {
        Mockito
            .`when`(codelistRepository.listAllCodelists(projectId))
            .thenReturn(codelists)

        val mockedCodelists: List<Codelist> = codelistService.list(projectRef)

        Assertions.assertEquals(codelists[0].title, mockedCodelists[0].title)
        Assertions.assertEquals(codelists[0].description, mockedCodelists[0].description)
        Assertions.assertEquals(codelists[0].project, mockedCodelists[0].project)
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

        val form = codelistForm

        val mockedCodelist: Codelist =
            codelistService.create(
                arrangeSetup.codelist.project!!.ref,
                form
            )

        Assertions.assertNotNull(mockedCodelist)
        Assertions.assertEquals(form.title, mockedCodelist.title)
        Assertions.assertEquals(form.description, mockedCodelist.description)
    }

    @Test
    fun delete() {
        Mockito
            .`when`(
                codelistRepository.deleteById(anyLong())
            )
            .thenReturn(true)

        Mockito.`when`(codelistRepository.findByRef(anyLong(), anyString())).thenReturn(codelist)

        val result = codelistService.delete(projectRef, codelistRef)

        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result)
        verify(codelistRepository).deleteById(1L)
    }

    @Test
    fun update() {
        Mockito
            .`when`(codelistRepository.findByRef(projectId, codelistRef))
            .thenReturn(arrangeSetup.codelist)

        val form = updatedCodelistForm

        val mockedCodelist: Codelist = codelistService.update(
            projectRef,
            codelistRef,
            form
        )

        Assertions.assertNotNull(mockedCodelist)
        Assertions.assertEquals(form.title, mockedCodelist.title)
        Assertions.assertEquals(form.description, mockedCodelist.description)
    }
}