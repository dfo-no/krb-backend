package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProductForm
import org.kravbank.domain.*
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.ProductRepository
import org.kravbank.resource.ProductResource
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.BadRequestException
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class ProductResourceMockTest {

    @InjectMock
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var productResource: ProductResource

    //entity
    var project: Project = Project()
    var publication: Publication = Publication()
    var product: Product = Product()
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
    val projectId = 3L
    val projectRef = "bbb4db69-edb2-431f-855a-4368e2bcddd1"
    val productRef = "edb4db69-edb2-431f-855a-4368e2bcddd1"

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
            .`when`(productRepository.findByRef(projectId, productRef))
            .thenReturn(product)

        val response: Response = productResource.getProduct(projectRef, productRef)
        val entity: Product = ProductForm().toEntity(response.entity as ProductForm)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals("Produkt tittel", entity.title)
        assertEquals("Produkt beskrivelse", entity.description)
        //assertEquals(project, entity.project) // Forelder /barn entitet blir gjemt av json ignore. Vurderer alternativer
        //assertEquals(reqVariants, entity.requirementvariants) // Forelder /barn entitet blir gjemt av json ignore. Vurderer alternativer
    }

    @Test
    fun listProducts_OK() {
        //mock
        Mockito
            .`when`(productRepository.listAllProducts(projectId))
            .thenReturn(products)

        val response: Response = productResource.listProducts(projectRef)

        // val entity =
        //   listOf(response.entity).filterIsInstance<ProductForm>()
        //.takeIf { it.size == listOf(response.entity).size }!!

        @Suppress("UNCHECKED_CAST")
        val entity: List<ProductForm> = response.entity as List<ProductForm>

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals("Produkt tittel", entity[0].title)
        assertEquals("Produkt beskrivelse", entity[0].description)
    }

    @Test
    fun createRequirement_OK() {
        //mock
        Mockito
            .doNothing()
            .`when`(productRepository)
            .persist(ArgumentMatchers.any(Product::class.java))

        Mockito
            .`when`(productRepository.isPersistent(ArgumentMatchers.any(Product::class.java)))
            .thenReturn(true)

        val form = ProductForm().fromEntity(product)
        form.requirementVariantRef = "rvrv1b69-edb2-431f-855a-4368e2bcddd1"

        val response: Response = productResource.createProduct(projectRef, form)

        //assert
        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }


    @Test
    fun updateProduct_OK() {
        val form = ProductForm()
        form.ref = productRef
        form.title = "Oppdatert tittel"
        form.description = "Oppdatert beskrivelse"

        Mockito
            .`when`(productRepository.findByRef(projectId, productRef))
            .thenReturn(product)

        val response: Response = productResource.updateProduct(projectRef, productRef, form)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)

        val entity: Product = ProductForm().toEntity(response.entity as ProductForm)
        assertEquals("Oppdatert tittel", entity.title)
        assertEquals("Oppdatert beskrivelse", entity.description)
    }

    @Test
    fun updateRequirement_KO() {
        val form = ProductForm()
        form.title = "Oppdatert tittel"
        form.description = "Oppdatert beskrivelse"

        Mockito
            .`when`(productRepository.findByRef(projectId, productRef))
            .thenThrow(BadRequestException("Product not found"))

        try {
            productResource.updateProduct(projectRef, productRef, form).entity as NotFoundException
        } catch (e: Exception) {
            assertEquals("Product not found", e.message)
        }
    }

    /*

    Todo:
         KO-testene kan være nyttig for å teste at feilmeldingene som kastes, behandles på riktig måte.
         Kommer tilbake til den når jeg finner ut av hvorfor mocking ikke gir riktig verdi / ikke-null


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
                .`when`(productRepository.findByRef(projectId, productRef))
                .thenThrow(NotFoundException("Requirement not found!"))
            try {
                productResource.getProduct(projectRef, productRef).entity as NotFoundException
            } catch (e: Exception) {
                //assert

                print(e.message)
                assertEquals("Requirement not found!", e.message)
            }
        }

    @Test
    fun createRequirement_KO() {
        assertFalse(true)
    }




    @Test
    fun deleteRequirement_KO() {
        //mock
        Mockito
            .`when`(productRepository.findByRef(productId, productRef))
            .thenThrow(NotFoundException("Product not found"))

        try {
            productResource.deleteProduct(projectRef, productRef).entity as NotFoundException
        } catch (e: Exception) {
            //print(e.message)
            assertEquals("Product not found", e.message)
        }
    }


*/


    /*
        todo:
          Denne testen er nyttig, men kommer tilbake til den når jeg får løst enten med
          1. mock og assert med bool retur fra repo.
          eller
          2. endre fra bool til entity retur fra repo

        @Test
        fun deleteProduct_OK() {
            Mockito
                .`when`(productRepository.deleteProduct(productId))
                .thenReturn(true)

            val response: Response = productResource.deleteProduct(projectRef, productRef)

            assertNotNull(response)
            // assertEquals()
            assertEquals("98870ds9fgsdfklmklklds", response.entity.toString())

        }


         */

}