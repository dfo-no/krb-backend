package org.kravbank.api.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.kravbank.api.CodelistResource
import org.kravbank.domain.Code
import org.kravbank.domain.Codelist
import org.kravbank.domain.Project
import org.kravbank.repository.CodelistRepository
import org.kravbank.utils.form.codelist.CodelistForm
import org.kravbank.utils.mapper.codelist.CodelistMapper
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
internal class CodelistResourceTestTMock {

    @InjectMock
    lateinit var codelistRepository: CodelistRepository
    @Inject
    lateinit var codelistResource: CodelistResource

    var codelist: Codelist = Codelist ()
    var codelists: MutableList<Codelist> = mutableListOf()
    var project: Project = Project()
    var codes: MutableList<Code>  = mutableListOf()
    var code: Code = Code()


    @BeforeEach
    fun setUp() {

        //arrange
        project = Project()
        project.title = "første prosjekt"
        project.description = "første prosjekt beskrivelse"
        project.version = 2
        project.publishedDate = LocalDateTime.now()
        project.ref ="ccc5db69-edb2-431f-855a-4368e2bcddd1"
        project.id = 120

        code = Code()
        code.title = "Tittel kode"
        code.description = "beskrivelse kode"
        code.codelist = codelist

        codes.add(code)

        codelist = Codelist()
        codelist.title = "Første codelist"
        codelist.description = "første codelist beskrivelse"
        codelist.ref = "hello243567"
        codelist.project = project
        codelist.codes = codes
        codelist.id = (1L)
    }

    @Test
    fun getCodelistByRef() {



    }

    @Test
    fun listCodelists() {
        codelists.add(codelist)

        val id = 1L
        val ref = "ccc4db69-edb2-431f-855a-4368e2bcddd1"

        //mock
        Mockito.`when`(codelistRepository.listAllCodelists(id)).thenReturn(codelists)
        val response: Response = codelistResource.listCodelists(ref)

        //mapper response fra DTO til entitets liste
        val entity: MutableList<Codelist> = mutableListOf()
        for (r in response.entity as List<CodelistForm>) entity.add(CodelistMapper().toEntity(r))

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        // println("entity ::::>>>${entity.get(0).title}")
        assertFalse(entity.isEmpty())
        assertEquals("Første codelist", entity.get(0).title)
        assertEquals("første codelist beskrivelse", entity.get(0).description)
    }

    @Test
    fun createCodelist() {
    }

    @Test
    fun deleteCodelist() {
    }

    @Test
    fun updateCodelist() {
    }

    @Test
    fun getCodelistService() {
    }
}