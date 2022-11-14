package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.RequirementVariantForm
import org.kravbank.domain.*
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.resource.RequirementVariantResource
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
internal class RequirementVariantResourceMockTest {

    @InjectMock
    lateinit var requirementVariantRepository: RequirementVariantRepository

    @Inject
    lateinit var requirementVariantResource: RequirementVariantResource

    //entity
    var project: Project = Project()
    var code: Code = Code()
    var publication: Publication = Publication()
    var product: Product = Product()
    var requirement: Requirement = Requirement()
    var need: Need = Need()

    var reqVariant: RequirementVariant = RequirementVariant()

    //lists
    var codes: MutableList<Code> = mutableListOf()
    var codelists: MutableList<Codelist> = mutableListOf()
    var requirements: MutableList<Requirement> = mutableListOf()
    var needs: MutableList<Need> = mutableListOf()
    var publications: MutableList<Publication> = mutableListOf()
    var products: MutableList<Product> = mutableListOf()
    var reqVariants: MutableList<RequirementVariant> = mutableListOf()

    val time: LocalDateTime = LocalDateTime.of(2010, 10, 10, 10, 10)


    //arrange
    val projectId = 2L
    val projectRef = "aaa4db69-edb2-431f-855a-4368e2bcddd1"
    val requirementId = 12L
    val requirementRef = "req1b69-edb2-431f-855a-4368e2bcddd1"
    val reqVariantId = 14L
    val reqVariantRef = "rvrv1b69-edb2-431f-855a-4368e2bcddd1"

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

        product.id = 999L
        product.ref = "98870ds9fgsdfklmklklds"
        product.title = "Produkt tittel"
        product.description = "Produkt beskrivelse"
        product.project = project
        product.requirementvariant = reqVariant

        need = Need()
        need.ref = "need2b69-edb2-431f-855a-4368e2bcddd1"
        need.id = 123L
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
        products.add(product)

    }

    @Test
    fun getProduct_OK() {
        //mock
        Mockito
            .`when`(requirementVariantRepository.findByRef(requirementId, reqVariantRef))
            .thenReturn(reqVariant)

        val response: Response =
            requirementVariantResource.getRequirementVariant(projectRef, requirementRef, reqVariantRef)
        val entity: RequirementVariant = RequirementVariantForm().toEntity(response.entity as RequirementVariantForm)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals("Ny instruksjon", entity.instruction)
        assertEquals("Req variant beskrivelse", entity.description)
        assertEquals("Tekst", entity.requirementText)
        assertEquals(true, entity.useProduct)
        assertEquals(false, entity.useQualification)
        assertEquals(true, entity.useSpesification)
        //assertEquals(project, entity.project) //  json ignore
        //assertEquals(reqVariants, entity.requirementvariants) // json ignore
    }

    /*
        @Test
        fun getProduct_KO() {
            //arrange
            val projectId = 3L
            val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
            val productId = 5L
            val productRef = "req1b69-edb2-431f-855a-4368e2bcddd1"
            val reqVariantId = 14
            val reqVariantRef = "rvrv1b69-edb2-431f-855a-4368e2bcddd1"

            //mock
            Mockito
                .`when`(requirementVariantRepository.findByRef(projectId, productRef))
                .thenThrow(NotFoundException("Requirement not found!"))
            try {
                requirementVariantResource.getProduct(projectRef, productRef).entity as NotFoundException
            } catch (e: Exception) {
                //assert

                print(e.message)
                assertEquals("Requirement not found!", e.message)
            }
        }
    */

    @Test
    fun listProducts_OK() {

        //mock
        Mockito
            .`when`(requirementVariantRepository.listAllRequirementVariants(requirementId))
            .thenReturn(reqVariants)

        val response: Response = requirementVariantResource.listRequirementVariants(projectRef, requirementRef)
        val entity: List<RequirementVariantForm> = response.entity as List<RequirementVariantForm>

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals("Ny instruksjon", entity[0].instruction)
        assertEquals("Req variant beskrivelse", entity[0].description)
        assertEquals("Tekst", entity[0].requirementText)
        assertEquals(true, entity[0].useProduct)
        assertEquals(false, entity[0].useQualification)
        assertEquals(true, entity[0].useSpesification)
    }


    @Test
    fun createRequirement_OK() {
        //mock
        Mockito
            .doNothing()
            .`when`(requirementVariantRepository)
            .persist(ArgumentMatchers.any(RequirementVariant::class.java))

        Mockito
            .`when`(requirementVariantRepository.isPersistent(ArgumentMatchers.any(RequirementVariant::class.java)))
            .thenReturn(true)

        //map
        val form = RequirementVariantForm().fromEntity(reqVariant)

        val response: Response = requirementVariantResource.createRequirementVariant(projectRef, requirementRef, form)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status);
    }

    /*
        @Test
        fun createRequirement_KO() {
            assertFalse(true)
        }


     */

    @Test
    fun deleteProduct_OK() {
        //mock
        Mockito
            .`when`(requirementVariantRepository.deleteRequirementVariant(requirementId, reqVariantRef))
            .thenReturn(reqVariant)

        val response: Response =
            requirementVariantResource.deleteRequirementVariant(projectRef, requirementRef, reqVariantRef)

        //assert
        assertNotNull(response)
        assertEquals("tfghjda67765hjbnknmbkljsakl", response.entity.toString())

    }

    /*
        @Test
        fun deleteRequirement_KO() {
            //arrange
            val projectId = 3L
            val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
            val productId = 5L
            val productRef = "req1b69-edb2-431f-855a-4368e2bcddd1"
            val reqVariantId = 14
            val reqVariantRef = "rvrv1b69-edb2-431f-855a-4368e2bcddd1"

            Mockito
                .`when`(requirementVariantRepository.findByRef(productId, productRef))
                .thenThrow(NotFoundException("Product not found"))

            try {
                requirementVariantResource.deleteProduct(projectRef, productRef).entity as NotFoundException
            } catch (e: Exception) {
                //print(e.message)
                assertEquals("Product not found", e.message)
            }
        }

    */
    @Test
    fun updateProduct_OK() {
        //arrange
        val form = RequirementVariantForm()
        form.ref = reqVariantRef
        form.instruction = "Oppdatert instruksjon"
        form.description = "Oppdatert beskrivelse"

        //mock
        Mockito
            .`when`(requirementVariantRepository.findByRef(requirementId, reqVariantRef))
            .thenReturn(reqVariant)

        val response: Response =
            requirementVariantResource.updateRequirementVariant(projectRef, requirementRef, reqVariantRef, form)
        val entity: RequirementVariant = RequirementVariantForm().toEntity(response.entity as RequirementVariantForm)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals("Oppdatert instruksjon", entity.instruction)
        assertEquals("Oppdatert beskrivelse", entity.description)
    }

    /*


    @Test
    fun updateRequirement_KO() {

        //arrange
        val projectId = 3L
        val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
        val requirementID = 8L
        val productRef = "zzz4db69-edb2-431f-855a-4368e2bcddd1"
        val ref = "dsfdsgs<'fåowi39543tdsf"

        val form = ProductForm()
        form.title = "Oppdatert tittel"
        form.description = "Oppdatert beskrivelse"

        Mockito
            .`when`(requirementVariantRepository.findByRef(projectId, productRef))
            .thenThrow(BadRequestException("Product not found"))

        try {
            requirementVariantResource.updateProduct(projectRef, productRef, form).entity as NotFoundException
        } catch (e: Exception) {
            assertEquals("Product not found", e.message)
        }
    }

    */
}