package org.kravbank.resource.mock

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
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
import org.kravbank.utils.TestSetup.Arrange.products
import org.kravbank.utils.TestSetup.Arrange.updatedProductForm
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.ws.rs.core.Response

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
internal class ProductResourceMockTest {


    private final val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private final val productRepository: ProductRepository = mock(ProductRepository::class.java)
    private final val requirementVariantRepository: RequirementVariantRepository =
        mock(RequirementVariantRepository::class.java)

    private final val productService = ProductService(
        productRepository = productRepository,
        projectRepository = projectRepository,
        requirementVariantRepository = requirementVariantRepository
    )

    val productResource = ProductResource(productService)

    private val arrangeSetup = TestSetup.Arrange

    private lateinit var updateProductForm: ProductForm
    private lateinit var requirementVariant: RequirementVariant
    private lateinit var requirement: Requirement
    private lateinit var product: Product
    private lateinit var project: Project
    private lateinit var createForm: ProductForm


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()


        updateProductForm = arrangeSetup.updatedProductForm
        requirement = arrangeSetup.requirement
        requirementVariant = arrangeSetup.requirementVariant
        product = arrangeSetup.product
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
        assertEquals(updateProductForm.title, entity.title)
        assertEquals(updateProductForm.description, entity.description)

    }


    @Test
    fun deleteProduct_OK() {

        //To check that delete timestamp is asserted, we need to call real method
        `when`(productRepository.delete(product)).thenCallRealMethod()

        val response: Response = productResource.deleteProduct(project.ref, product.ref)

        assertNotNull(response)
        assertEquals(product.ref, response.entity)
        verify(productRepository).delete(product)

        //soft deleted method adds deleted timestamp
        assertNotNull(product.deletedDate)

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