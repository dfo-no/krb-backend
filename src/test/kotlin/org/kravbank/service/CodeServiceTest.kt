package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.TestSetup
import org.kravbank.domain.Code
import org.kravbank.repository.CodeRepository
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class CodeServiceTest {

    @InjectMock
    lateinit var codeRepository: CodeRepository

    @Inject
    lateinit var codeService: CodeService

    val arrangeSetup = TestSetup.Arrange

    @BeforeEach
    fun setUp() {
        arrangeSetup.start()
    }

    @Test
    fun get() {
        Mockito
            .`when`(
                codeRepository
                    .findByRef(arrangeSetup.codelist_codeId, arrangeSetup.code_codelistRef)
            ).thenReturn(arrangeSetup.code)

        val mockedCode: Code =
            codeService.get(arrangeSetup.project_codeRef, arrangeSetup.codelist_codeRef, arrangeSetup.code_codelistRef)

        Assertions.assertEquals(arrangeSetup.code.title, mockedCode.title)
        Assertions.assertEquals(arrangeSetup.code.id, mockedCode.id)
        Assertions.assertEquals(arrangeSetup.code.description, mockedCode.description)
    }

    @Test
    fun list() {
        Mockito.`when`(
            codeRepository
                .listAllCodes(arrangeSetup.codelist_codeId)
        ).thenReturn(arrangeSetup.codes)

        val mockedCodes: List<Code> = codeService.list(arrangeSetup.project_codeRef, arrangeSetup.codelist_codeRef)

        Assertions.assertEquals(arrangeSetup.code.title, mockedCodes[0].title)
        Assertions.assertEquals(arrangeSetup.code.id, mockedCodes[0].id)
        Assertions.assertEquals(arrangeSetup.code.description, mockedCodes[0].description)
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

        val mockedCode: Code =
            codeService.create(arrangeSetup.project_codeRef, arrangeSetup.codelist_codeRef, arrangeSetup.codeForm)

        Assertions.assertNotNull(mockedCode)
        Assertions.assertEquals(arrangeSetup.newCode.title, mockedCode.title)
        Assertions.assertEquals(arrangeSetup.newCode.description, mockedCode.description)
    }

    @Test
    fun delete() {
        //TODO("Kommer tilbake her senere")
    }

    @Test
    fun update() {
        Mockito
            .`when`(
                codeRepository
                    .findByRef(
                        arrangeSetup.codelist_codeId,
                        arrangeSetup.code_codelistRef
                    )
            ).thenReturn(arrangeSetup.code)

        val mockedCode: Code = codeService.update(
            arrangeSetup.project_codeRef,
            arrangeSetup.codelist_codeRef,
            arrangeSetup.code_codelistRef,
            arrangeSetup.updatedCodeForm
        )

        Assertions.assertNotNull(mockedCode)
        Assertions.assertEquals(arrangeSetup.updatedCodeForm.title, mockedCode.title)
        Assertions.assertEquals(arrangeSetup.updatedCodeForm.description, mockedCode.description)
    }
}