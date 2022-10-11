package org.kravbank.service

import io.quarkus.cache.CacheResult
import org.kravbank.exception.BackendException
import org.kravbank.exception.BadRequestException
import org.kravbank.exception.NotFoundException
import org.kravbank.utils.form.product.ProductForm
import org.kravbank.utils.form.product.ProductFormUpdate
import org.kravbank.repository.ProductRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.mapper.product.ProductMapper
import org.kravbank.utils.mapper.product.ProductUpdateMapper
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class ProductService(
    val productRepository: ProductRepository,
    val projectService: ProjectService,
    val projectRepository: ProjectRepository
) {

    // @CacheResult(cacheName = "product-cache-get")
    @Throws(BackendException::class)
    fun get(projectRef: String, productRef: String): Response {
        val project = projectRepository.findByRef(projectRef)
        val foundProduct = productRepository.findByRef(project.id, productRef)
        val mappedProduct = ProductMapper().fromEntity(foundProduct)
        return Response.ok(mappedProduct).build()
    }

    //@CacheResult(cacheName = "product-cache-list")
    @Throws(BackendException::class)
    fun list(projectRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundProducts = productRepository.listAllProducts(foundProject.id)

        val productsDTO = ArrayList<ProductForm>()
        for (p in foundProducts) productsDTO.add(ProductMapper().fromEntity(p))
        return Response.ok(productsDTO).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, product: ProductForm): Response {
        val project = projectRepository.findByRef(projectRef)
        product.project = project

        //println("project: " + project)
        //createProductDTO
        val productMapper = ProductMapper().toEntity(product)
        //println("productMapper: " + productMapper)

        //project.products.add(productMapper)
        // projectService.updateProject(project.id, project)

        productRepository.persistAndFlush(productMapper)

        // if (productMapper.isPersistent)
        return Response.created(URI.create("/api/v1/projects/$projectRef/products/" + product.ref)).build()
        //  else throw BadRequestException("Bad request! Did not create product")
    }

    @Throws(BackendException::class)
    fun delete(projectRef: String, productRef: String): Response {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundProduct = foundProject.products.find { products -> products.ref == productRef }
        Optional.ofNullable(foundProduct)
            .orElseThrow { NotFoundException("Product not found by ref $productRef in project by ref $projectRef") }
        val deleted = foundProject.products.remove(foundProduct)
        if (deleted) {
            projectService.updateProject(foundProject.id, foundProject)
            return Response.noContent().build()
        } else throw BadRequestException("Bad request! Product not deleted")
    }

    @Throws(BackendException::class)
    fun update(projectRef: String, productRef: String, product: ProductFormUpdate): Response {
        val foundProduct = projectRepository.findByRef(projectRef).products.find { products ->
            products.ref == productRef
        }
        Optional.ofNullable(foundProduct)
            .orElseThrow { NotFoundException("Product not found by ref $productRef in project by ref $projectRef") }
        val productMapper = ProductUpdateMapper().toEntity(product)
        productRepository.update(
            "title = ?1, description = ?2 where id= ?3",
            productMapper.title,
            productMapper.description,
            //product.deletedDate,
            foundProduct!!.id
        )
        return Response.ok(product).build()
    }
    //fun exists(id: Long): Boolean = productRepository.count("id", id) == 1L
    //fun refExists(ref: String): Boolean = productRepository.count("ref", ref) == 1L
}