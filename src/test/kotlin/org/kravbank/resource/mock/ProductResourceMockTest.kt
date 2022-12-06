package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.quarkus.test.security.TestSecurity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProductForm
import org.kravbank.domain.Product
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.ProductRepository
import org.kravbank.resource.ProductResource
import org.kravbank.utils.ErrorMessage.RepoError.PRODUCT_NOTFOUND
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.product
import org.kravbank.utils.TestSetup.Arrange.productForm
import org.kravbank.utils.TestSetup.Arrange.products
import org.kravbank.utils.TestSetup.Arrange.reqVariant_productRef
import org.kravbank.utils.TestSetup.Arrange.updatedProductForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class ProductResourceMockTest {

    @InjectMock
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var productResource: ProductResource

    private final val arrangeSetup = TestSetup.Arrange

    private val projectId: Long = arrangeSetup.project_productId
    private val projectRef: String = arrangeSetup.project_productRef
    private val productRef: String = arrangeSetup.product_projectRef

    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }

    @Test
    fun getProduct_OK() {
        Mockito
            .`when`(productRepository.findByRef(projectId, productRef))
            .thenReturn(product)

        val response: Response = productResource.getProduct(projectRef, productRef)

        val entity: Product = ProductForm().toEntity(response.entity as ProductForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertEquals(product.title, entity.title)
        assertEquals(product.description, entity.description)
    }

    @Test
    fun listProducts_OK() {
        Mockito
            .`when`(productRepository.listAllProducts(projectId))
            .thenReturn(products)

        val response: Response = productResource.listProducts(projectRef)

        val entity: List<ProductForm> = response.entity as List<ProductForm>

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertNotNull(response.entity)
        assertFalse(entity.isEmpty())
        assertEquals(product.title, entity[0].title)
        assertEquals(product.description, entity[0].description)
    }

    @Test
    fun createRequirement_OK() {
        Mockito
            .doNothing()
            .`when`(productRepository)
            .persist(ArgumentMatchers.any(Product::class.java))

        Mockito
            .`when`(productRepository.isPersistent(ArgumentMatchers.any(Product::class.java)))
            .thenReturn(true)

        val form = productForm
        form.requirementVariantRef = reqVariant_productRef

        val response: Response = productResource.createProduct(projectRef, productForm)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)
    }


    @Test
    fun updateProduct_OK() {
        Mockito
            .`when`(productRepository.findByRef(projectId, productRef))
            .thenReturn(product)

        val form = updatedProductForm

        val response: Response = productResource.updateProduct(projectRef, productRef, form)

        val entity: Product = ProductForm().toEntity(response.entity as ProductForm)

        assertNotNull(response)
        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals(form.title, entity.title)
        assertEquals(form.description, entity.description)
    }

    @Test
    fun updateRequirement_KO() {
        Mockito
            .`when`(productRepository.findByRef(projectId, productRef))
            .thenThrow(NotFoundException(PRODUCT_NOTFOUND))

        val form = updatedProductForm

        try {
            productResource.updateProduct(
                projectRef,
                productRef,
                form
            ).entity as NotFoundException

        } catch (e: Exception) {
            assertEquals(PRODUCT_NOTFOUND, e.message)
        }
    }

    @Test
    fun getProduct_KO() {
        Mockito
            .`when`(productRepository.findByRef(projectId, productRef))
            .thenThrow(NotFoundException(PRODUCT_NOTFOUND))

        try {

            productResource.getProduct(
                projectRef,
                productRef
            ).entity as NotFoundException

        } catch (e: Exception) {
            assertEquals(PRODUCT_NOTFOUND, e.message)
        }
    }


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

}