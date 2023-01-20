package org.kravbank.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kravbank.dao.ProductForm
import org.kravbank.domain.Product
import org.kravbank.domain.Project
import org.kravbank.domain.Requirement
import org.kravbank.domain.RequirementVariant
import org.kravbank.repository.ProductRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.utils.TestSetup
import org.kravbank.utils.TestSetup.Arrange.products
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

class ProductServiceTest {

    private val projectRepository: ProjectRepository = mock(ProjectRepository::class.java)
    private val productRepository: ProductRepository = mock(ProductRepository::class.java)
    private val requirementVariantRepository: RequirementVariantRepository =
        mock(RequirementVariantRepository::class.java)

    private val productService = ProductService(
        productRepository = productRepository,
        projectRepository = projectRepository,
        requirementVariantRepository = requirementVariantRepository
    )

    private val arrangeSetup = TestSetup.Arrange

    private lateinit var createForm: ProductForm
    private lateinit var updateForm: ProductForm
    private lateinit var requirementVariant: RequirementVariant
    private lateinit var requirement: Requirement
    private lateinit var product: Product
    private lateinit var project: Project


    @BeforeEach
    fun setUp() {
        arrangeSetup.start()


        requirement = arrangeSetup.requirement
        requirementVariant = arrangeSetup.requirementVariant
        products = arrangeSetup.products
        product = arrangeSetup.product
        project = arrangeSetup.project
        updateForm = arrangeSetup.updatedProductForm
        createForm = ProductForm().fromEntity(product)

        `when`(projectRepository.findByRef(project.ref)).thenReturn(project)
        `when`(productRepository.findByRef(project.id, product.ref)).thenReturn(product)
        `when`(requirementVariantRepository.findByRef(requirement.id, requirementVariant.ref)).thenReturn(
            requirementVariant
        )
        `when`(productRepository.listAllProducts(project.id)).thenReturn(products)

    }

    @Test
    fun get() {
        val response = productService.get(project.ref, product.ref)

        val entity: Product = response

        assertEquals(product.title, entity.title)
        assertEquals(product.id, entity.id)
        assertEquals(product.project, entity.project)
        assertEquals(product.description, entity.description)

    }

    @Test
    fun list() {
        val response = productService.list(project.ref)

        val entity: List<Product> = response

        assertNotNull(response)
        assertFalse(entity.isEmpty())
        val firstObjectInList = entity[0]
        assertEquals(products[0].title, firstObjectInList.title)
        assertEquals(products[0].description, firstObjectInList.description)
        assertEquals(products[0].project, firstObjectInList.project)

    }

    @Test
    fun create() {
        doNothing()
            .`when`(productRepository)
            .persist(ArgumentMatchers.any(Product::class.java))

        `when`(productRepository.isPersistent(ArgumentMatchers.any(Product::class.java)))
            .thenReturn(true)

        val response = productService.create(project.ref, createForm)

        val entity: Product = response

        assertNotNull(entity)
        assertEquals(createForm.title, entity.title)
        assertEquals(createForm.description, entity.description)
    }

    @Test
    fun delete() {
        productService.delete(
            project.ref,
            product.ref
        )

        verify(productRepository).deleteById(product.id)
    }

    @Test
    fun update() {

        val response = productService.update(
            project.ref,
            product.ref,
            updateForm
        )

        val entity: Product = response

        assertNotNull(entity)
        assertEquals(updateForm.title, entity.title)
        assertEquals(updateForm.description, entity.description)
    }
}