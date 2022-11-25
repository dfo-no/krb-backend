package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.RequirementForm
import org.kravbank.domain.*
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.RequirementRepository
import org.kravbank.resource.RequirementResource
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.BadRequestException
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class RequirementResourceMockTest {

    @InjectMock
    lateinit var requirementRepository: RequirementRepository

    @Inject
    lateinit var requirementResource: RequirementResource

    //entity
    var project: Project = Project()
    var publication: Publication = Publication()
    var requirement: Requirement = Requirement()
    var need: Need = Need()
    var reqVariant: RequirementVariant = RequirementVariant()

    //lists
    var codelists: MutableList<Codelist> = mutableListOf()
    var requirements: MutableList<Requirement> = mutableListOf()
    var needs: MutableList<Need> = mutableListOf()
    var publications: MutableList<Publication> = mutableListOf()
    var products: MutableList<Product> = mutableListOf()
    var reqVariants: MutableList<RequirementVariant> = mutableListOf()

    //arrange
    val projectId = 2L
    val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
    val requirementRef = "req1b69-edb2-431f-855a-4368e2bcddd1"

    @BeforeEach
    fun setUp() {

        //arrange
        project = Project()
        project.title = "første prosjekt"
        project.description = "første prosjekt beskrivelse"
        project.ref = "ccc5db69-edb2-431f-855a-4368e2bcddd1"
        project.id = 120
        project.codelist = codelists
        project.publications = publications
        project.requirements = requirements
        project.needs = needs
        project.products = products

        need = Need()
        need.ref = "need2b69-edb2-431f-855a-4368e2bcddd1"
        need.id = 122L
        need.title = "tittel"
        need.description = "desv"

        requirement = Requirement()
        requirement.ref = "23chgvjkhty87"
        requirement.project = project
        requirement.id = 500L
        requirement.need = need
        requirement.title = "Requirement tittel"
        requirement.description = "Requirement beskrivelse"
        requirement.requirementvariants = reqVariants

        reqVariant = RequirementVariant()
        reqVariant.requirement = requirement
        reqVariant.id = 400L
        reqVariant.ref = "tfghjda67765hjbnknmbkljsakl"
        reqVariant.description = "Req variant beskrivelse"
        reqVariant.requirementText = "Tekst"
        reqVariant.useQualification = false
        reqVariant.useSpesification = true
        reqVariant.useProduct = true
        reqVariant.instruction = "Ny instruksjon"
        reqVariant.product = products

        publication = Publication()

        requirements.add(requirement)
        reqVariants.add(reqVariant)
    }

    @Test
    fun getRequirement_OK() {
        //mock
        Mockito
            .`when`(requirementRepository.findByRef(projectId, requirementRef))
            .thenReturn(requirement)

        val response: Response = requirementResource.getRequirement(projectRef, requirementRef)
        val entity: Requirement = RequirementForm().toEntity(response.entity as RequirementForm)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals("Requirement tittel", entity.title)
        assertEquals("Requirement beskrivelse", entity.description)
    }

    @Test
    fun getRequirement_KO() {

        //mock
        Mockito
            .`when`(requirementRepository.findByRef(projectId, requirementRef))
            .thenThrow(NotFoundException("Requirement not found!"))
        try {
            requirementResource.getRequirement(projectRef, requirementRef).entity as NotFoundException
        } catch (e: Exception) {
            //assert
            assertEquals("Requirement not found!", e.message)
        }
    }

    @Test
    fun listRequirements_OK() {
        //mock
        Mockito.`when`(requirementRepository.listAllRequirements(projectId)).thenReturn(requirements)

        val response: Response = requirementResource.listRequirements(projectRef)

        // val entity = listOf(response.entity).filterIsInstance<RequirementForm>()
        //.takeIf { it.size == listOf(response.entity).size }!!

        @Suppress("UNCHECKED_CAST")
        val entity: List<RequirementForm> = response.entity as List<RequirementForm>

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals("Requirement tittel", entity[0].title)
        assertEquals("Requirement beskrivelse", entity[0].description)
    }

    @Test
    fun createRequirement_OK() {
        //mock
        Mockito
            .doNothing()
            .`when`(requirementRepository).persist(ArgumentMatchers.any(Requirement::class.java))
        Mockito
            .`when`(requirementRepository.isPersistent(ArgumentMatchers.any(Requirement::class.java)))
            .thenReturn(true)

        val form = RequirementForm().fromEntity(requirement)
        form.needRef = "need210291111-edb2-431f-855a-4368e2bcddd1"
        val response: Response = requirementResource.createRequirement(projectRef, form)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }

    @Test
    fun deleteRequirement_OK() {
        //mock
        Mockito
            .`when`(requirementRepository.deleteRequirement(projectId, requirementRef))
            .thenReturn(requirement)

        val response: Response = requirementResource.deleteRequirement(projectRef, requirementRef)

        assertNotNull(response)
        assertEquals("23chgvjkhty87", response.entity.toString())
    }

    @Test
    fun deleteRequirement_KO() {
        //mock
        Mockito
            .`when`(requirementRepository.deleteRequirement(projectId, requirementRef))
            .thenThrow(BadRequestException("Bad request! Requirement was not deleted"))
        try {
            requirementResource.deleteRequirement(projectRef, requirementRef).entity as BadRequestException
        } catch (e: Exception) {
            //assert
            assertEquals("Bad request! Requirement was not deleted", e.message)
        }
    }

    @Test
    fun updateRequirement_OK() {
        //arrange
        val form = RequirementForm()
        form.ref = requirementRef
        form.title = "Oppdatert tittel"
        form.description = "Oppdatert beskrivelse"

        Mockito.`when`(requirementRepository.findByRef(projectId, requirementRef))
            .thenReturn(requirement)

        val response: Response = requirementResource.updateRequirement(projectRef, requirementRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)

        val entity: Requirement = RequirementForm().toEntity(response.entity as RequirementForm)
        assertEquals("Oppdatert tittel", entity.title)
        assertEquals("Oppdatert beskrivelse", entity.description)
    }

    @Test
    fun updateRequirement_KO() {
        //arrange
        val form = RequirementForm()
        form.title = "Oppdatert tittel"
        form.description = "Oppdatert beskrivelse"

        Mockito
            .`when`(requirementRepository.findByRef(projectId, requirementRef))
            .thenThrow(BadRequestException("Requirement not found"))

        try {
            requirementResource.updateRequirement(projectRef, requirementRef, form).entity as NotFoundException
        } catch (e: Exception) {
            assertEquals("Requirement not found", e.message)
        }
    }

    /*

      Todo:
         KO-testen kan være nyttig for å teste at feilmeldingene som kastes, behandles på riktig måte.
         Kommer tilbake til den når jeg finner ut av hvorfor mocking ikke gir riktig verdi / ikke-null


    @Test
    fun createRequirement_KO() {
        assertFalse(true)
    }


 */


}