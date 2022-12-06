package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.domain.Product
import org.kravbank.repository.ProductRepository
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.product
import org.kravbank.utils.TestSetup.Arrange.productForm
import org.kravbank.utils.TestSetup.Arrange.products
import org.kravbank.utils.TestSetup.Arrange.updatedProductForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class ProductServiceTest {

    @InjectMock
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var productService: ProductService

    private final val arrangeSetup = TestSetup.Arrange

    private final val projectId: Long = arrangeSetup.project_productId

    private final val productRef: String = arrangeSetup.product_projectRef

    private final val projectRef: String = arrangeSetup.project_productRef


    @BeforeEach
    fun setUp() {

        arrangeSetup.start()

    }

    @Test
    fun get() {
        Mockito
            .`when`(
                productRepository.findByRef(projectId, productRef)
            ).thenReturn(product)

        val mockedProduct: Product =
            productService.get(projectRef, productRef)

        Assertions.assertEquals(product.title, mockedProduct.title)
        Assertions.assertEquals(product.id, mockedProduct.id)
        Assertions.assertEquals(product.project, mockedProduct.project)
        Assertions.assertEquals(product.description, mockedProduct.description)
    }

    @Test
    fun list() {
        Mockito.`when`(
            productRepository.listAllProducts(projectId)
        ).thenReturn(products)

        val mockedProducts: List<Product> = productService.list(projectRef)

        Assertions.assertEquals(products[0].title, mockedProducts[0].title)
        Assertions.assertEquals(products[0].description, mockedProducts[0].description)
        Assertions.assertEquals(products[0].project, mockedProducts[0].project)
    }

    @Test
    fun create() {
        Mockito
            .doNothing()
            .`when`(productRepository)
            .persist(ArgumentMatchers.any(Product::class.java))

        Mockito
            .`when`(productRepository.isPersistent(ArgumentMatchers.any(Product::class.java)))
            .thenReturn(true)

        val form = productForm

        val mockedProduct: Product =
            productService.create(product.project!!.ref, form)

        Assertions.assertNotNull(mockedProduct)
        Assertions.assertEquals(form.title, mockedProduct.title)
        Assertions.assertEquals(form.description, mockedProduct.description)
    }

    @Test
    fun delete() {
        //TODO("Kommer tilbake her senere")
    }

    @Test
    fun update() {
        Mockito
            .`when`(
                productRepository.findByRef(
                    projectId,
                    productRef
                )
            ).thenReturn(product)

        val form = updatedProductForm

        val mockedProduct: Product = productService.update(
            projectRef,
            productRef,
            form
        )

        Assertions.assertNotNull(mockedProduct)
        Assertions.assertEquals(form.title, mockedProduct.title)
        Assertions.assertEquals(form.description, mockedProduct.description)
    }
}