package org.kravbank.service

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.TestSetup
import org.kravbank.domain.Product
import org.kravbank.repository.ProductRepository
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

@QuarkusTest
internal class ProductServiceTest {

    @InjectMock
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var productService: ProductService

    val arrangeSetup = TestSetup.Arrange

    @BeforeEach
    fun setUp() {
        arrangeSetup.start()
    }

    @Test
    fun get() {
        Mockito
            .`when`(
                productRepository
                    .findByRef(arrangeSetup.project_productId, arrangeSetup.product_projectRef)
            ).thenReturn(arrangeSetup.product)

        val mockedProduct: Product =
            productService.get(arrangeSetup.project_productRef, arrangeSetup.product_projectRef)

        Assertions.assertEquals(arrangeSetup.product.title, mockedProduct.title)
        Assertions.assertEquals(arrangeSetup.product.id, mockedProduct.id)
        Assertions.assertEquals(arrangeSetup.product.project, mockedProduct.project)
        Assertions.assertEquals(arrangeSetup.product.description, mockedProduct.description)
    }

    @Test
    fun list() {
        Mockito.`when`(
            productRepository
                .listAllProducts(arrangeSetup.project_productId)
        ).thenReturn(arrangeSetup.products)

        val mockedProducts: List<Product> = productService.list(arrangeSetup.project_productRef)

        Assertions.assertEquals(arrangeSetup.product.title, mockedProducts[0].title)
        Assertions.assertEquals(arrangeSetup.product.description, mockedProducts[0].description)
        Assertions.assertEquals(arrangeSetup.product.project, mockedProducts[0].project)
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

        val mockedProduct: Product =
            productService.create(arrangeSetup.product.project!!.ref, arrangeSetup.productForm)

        Assertions.assertNotNull(mockedProduct)
        Assertions.assertEquals(arrangeSetup.newProduct.title, mockedProduct.title)
        Assertions.assertEquals(arrangeSetup.newProduct.description, mockedProduct.description)
    }

    @Test
    fun delete() {
        //TODO("Kommer tilbake her senere")
    }

    @Test
    fun update() {
        Mockito
            .`when`(
                productRepository
                    .findByRef(arrangeSetup.project_productId, arrangeSetup.product_projectRef)
            ).thenReturn(arrangeSetup.product)

        val mockedProduct: Product = productService.update(
            arrangeSetup.project_productRef,
            arrangeSetup.product_projectRef,
            arrangeSetup.updatedProductForm
        )

        Assertions.assertNotNull(mockedProduct)
        Assertions.assertEquals(arrangeSetup.updatedProductForm.title, mockedProduct.title)
        Assertions.assertEquals(arrangeSetup.updatedProductForm.description, mockedProduct.description)
    }
}