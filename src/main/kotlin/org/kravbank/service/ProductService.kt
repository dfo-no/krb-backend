package org.kravbank.service

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
    @Throws(BackendException::class)
    fun list(projectRef: String): Response {
        //list proudcts by project ref
        val foundProjectProducts = projectRepository.findByRef(projectRef).products
        //convert to array of form
        val productsFormList = ArrayList<ProductForm>()
        for (p in foundProjectProducts) productsFormList.add(ProductMapper().fromEntity(p))
        //returns the custom product form
        return Response.ok(productsFormList).build()
    }

    @Throws(BackendException::class)
    fun get(projectRef: String, productRef: String): Response {
        val project = projectRepository.findByRef(projectRef).products.find { products ->
            products.ref == productRef
        }
        Optional.ofNullable(project)
            .orElseThrow { NotFoundException("Product not found by ref $productRef in project by ref $projectRef") }
        val productMapper = ProductMapper().fromEntity(project!!)
        return Response.ok(productMapper).build()
    }

    @Throws(BackendException::class)
    fun create(projectRef: String, product: ProductForm): Response {
        val productMapper = ProductMapper().toEntity(product)
        val project = projectRepository.findByRef(projectRef)
        project.products.add(productMapper)
        projectService.updateProject(project.id, project)
        if (productMapper.isPersistent)
            return Response.created(URI.create("/api/v1/projects/$projectRef/products" + product.ref)).build()
        else throw BadRequestException("Bad request! Did not create product")
    }

    fun delete(projectRef: String, productRef: String): Response {
        val foundProduct = projectRepository.findByRef(projectRef).products.find { products ->
            products.ref == productRef
        }
        Optional.ofNullable(foundProduct)
            .orElseThrow { NotFoundException("Product not found by ref $productRef in project by ref $projectRef") }
        foundProduct!!.delete()
        return Response.noContent().build()
    }

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