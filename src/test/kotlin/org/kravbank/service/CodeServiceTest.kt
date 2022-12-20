package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.Code
import org.kravbank.repository.CodeRepository
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.code
import org.kravbank.utils.TestSetup.Arrange.codeForm
import org.kravbank.utils.TestSetup.Arrange.codelist
import org.kravbank.utils.TestSetup.Arrange.codes
import org.kravbank.utils.TestSetup.Arrange.project
import org.kravbank.utils.TestSetup.Arrange.updatedCodeForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.verify
import javax.inject.Inject

@QuarkusTest
internal class CodeServiceTest {

    @InjectMock
    lateinit var codeRepository: CodeRepository

    @Inject
    lateinit var codeService: CodeService

    private val arrangeSetup = TestSetup.Arrange

    private val codelistId: Long = arrangeSetup.codelist_codeId

    private val codelistRef: String = arrangeSetup.codelist_codeRef

    private val codeRef: String = arrangeSetup.code_codelistRef

    private val projectRef: String = arrangeSetup.project_codeRef


    @BeforeEach
    fun setUp() {

        arrangeSetup.start()
    }

    @Test
    fun get() {
        Mockito
            .`when`(
                codeRepository
                    .findByRef(codelistId, codeRef)
            ).thenReturn(code)

        val mockedCode: Code =
            codeService.get(projectRef, codelistRef, codeRef)

        Assertions.assertEquals(code.title, mockedCode.title)
        Assertions.assertEquals(code.id, mockedCode.id)
        Assertions.assertEquals(code.description, mockedCode.description)
    }

    @Test
    fun list() {
        Mockito.`when`(
            codeRepository
                .listAllCodes(codelistId)
        ).thenReturn(codes)

        val mockedCodes: List<Code> = codeService.list(projectRef, codelistRef)

        Assertions.assertEquals(codes[0].title, mockedCodes[0].title)
        Assertions.assertEquals(codes[0].id, mockedCodes[0].id)
        Assertions.assertEquals(codes[0].description, mockedCodes[0].description)
    }

    @Test
    fun create() {
        Mockito
            .doNothing()
            .`when`(codeRepository)
            .persist(ArgumentMatchers.any(Code::class.java))

        Mockito
            .`when`(codeRepository.isPersistent(ArgumentMatchers.any(Code::class.java)))
            .thenReturn(true)

        val form = codeForm

        val mockedCode: Code =
            codeService.create(
                projectRef,
                codelistRef,
                form
            )

        Assertions.assertNotNull(mockedCode)
        Assertions.assertEquals(form.title, mockedCode.title)
        Assertions.assertEquals(form.description, mockedCode.description)
    }

    @Test
    fun delete() {
        codeService.delete(project.ref, codelist.ref, code.ref)

        verify(codeRepository).deleteById(code.id)
    }

    @Test
    fun update() {
        Mockito
            .`when`(
                codeRepository
                    .findByRef(
                        codelistId,
                        codeRef
                    )
            ).thenReturn(code)

        val form = updatedCodeForm

        val mockedCode: Code = codeService.update(
            projectRef,
            codelistRef,
            codeRef,
            form
        )

        Assertions.assertNotNull(mockedCode)
        Assertions.assertEquals(form.title, mockedCode.title)
        Assertions.assertEquals(form.description, mockedCode.description)
    }
}