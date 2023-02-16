package org.kravbank.resource.mock

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProductForm
import org.kravbank.domain.Product
import org.kravbank.domain.Project
import org.kravbank.domain.Requirement
import org.kravbank.domain.RequirementVariant
import org.kravbank.lang.BadRequestException
import org.kravbank.lang.NotFoundException
import org.kravbank.repository.ProductRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.resource.ProductResource
import org.kravbank.service.ProductService
import org.kravbank.utils.Messages.RepoErrorMsg.PRODUCT_BADREQUEST_CREATE
import org.kravbank.utils.Messages.RepoErrorMsg.PRODUCT_NOTFOUND
import org.kravbank.utils.TestSetup
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.ws.rs.core.Response


class ProductResourceMockTest {


    private val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private val productRepository: ProductRepository = mock(ProductRepository::class.java)
    private val requirementVariantRepository: RequirementVariantRepository =
        mock(RequirementVariantRepository::class.java)

    private val productService = ProductService(
        productRepository = productRepository,
        projectRepository = projectRepository,
        requirementVariantRepository = requirementVariantRepository
    )

    private val productResource = ProductResource(productService)

    private val arrangeSetup = TestSetup()

    private lateinit var updatedProductForm: ProductForm
    private lateinit var requirementVariant: RequirementVariant
    private lateinit var requirement: Requirement
    private lateinit var product: Product
    private lateinit var products: MutableList<Product>
    private lateinit var project: Project
    private lateinit var createForm: ProductForm


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()


        updatedProductForm = arrangeSetup.updatedProductForm
        requirement = arrangeSetup.requirement
        requirementVariant = arrangeSetup.requirementVariant
        product = arrangeSetup.product
        products = arrangeSetup.products
        project = arrangeSetup.project
        createForm = ProductForm().fromEntity(product)


        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(productRepository.findByRef(project.id, product.ref)).thenReturn(product)
        `when`(requirementVariantRepository.findByRef(requirement.id, requirementVariant.ref)).thenReturn(
            requirementVariant
        )
        `when`(productRepository.listAllProducts(project.id)).thenReturn(products)

    }


    @Test
    fun getProduct_OK() {
        val response = productResource.getProduct(project.ref, product.ref)

        val entity = ProductForm().toEntity(response)

        assertNotNull(response)
        assertEquals(product.title, entity.title)
        assertEquals(product.description, entity.description)
    }


    @Test
    fun listProducts_OK() {
        val response = productResource.listProducts(project.ref)

        assertNotNull(response)
        assertFalse(response.isEmpty())
        val firstObjectInList = response[0]

        assertEquals(product.title, firstObjectInList.title)
        assertEquals(product.description, firstObjectInList.description)

    }


    @Test
    fun createProduct_OK() {
        doNothing()
            .`when`(productRepository)
            .persist(ArgumentMatchers.any(Product::class.java))

        `when`(productRepository.isPersistent(ArgumentMatchers.any(Product::class.java)))
            .thenReturn(true)

        val response: Response = productResource.createProduct(project.ref, createForm)

        assertNotNull(response)
        assertEquals(Response.Status.CREATED.statusCode, response.status)

    }


    @Test
    fun updateProduct_OK() {
        val response = productResource.updateProduct(project.ref, product.ref, updatedProductForm)

        val entity: Product = ProductForm().toEntity(response)

        assertNotNull(response)
        assertEquals(updatedProductForm.title, entity.title)
        assertEquals(updatedProductForm.description, entity.description)

    }


    @Test
    fun deleteProduct_OK() {

        val response: Response = productResource.deleteProduct(project.ref, product.ref)

        assertNotNull(response)
        assertEquals(product.ref, response.entity)
        verify(productRepository).deleteById(product.id)
    }


    /**
     *
     * KO's of relevant exceptions.
     *
     */


    @Test
    fun getProduct_KO() {
        `when`(productRepository.findByRef(project.id, product.ref))
            .thenThrow(NotFoundException(PRODUCT_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            productResource.getProduct(
                project.ref,
                product.ref,
            )
        }
        assertEquals(PRODUCT_NOTFOUND, exception.message)

    }


    @Test
    fun createProduct_KO() {
        val exception = assertThrows(BadRequestException::class.java) {
            productResource.createProduct(
                project.ref,
                createForm,
            )
        }
        assertEquals(PRODUCT_BADREQUEST_CREATE, exception.message)
    }


    @Test
    fun updateProduct_KO() {
        `when`(productRepository.findByRef(project.id, product.ref))
            .thenThrow(NotFoundException(PRODUCT_NOTFOUND))

        val exception = assertThrows(NotFoundException::class.java) {
            productResource.updateProduct(
                project.ref,
                product.ref,
                updatedProductForm
            )
        }
        assertEquals(PRODUCT_NOTFOUND, exception.message)
    }

}