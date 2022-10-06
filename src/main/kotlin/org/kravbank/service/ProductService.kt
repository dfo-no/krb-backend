package org.kravbank.service

import org.kravbank.domain.Product
import org.kravbank.domain.Project
import org.kravbank.exception.BackendException
import org.kravbank.utils.form.product.ProductForm
import org.kravbank.utils.form.product.ProductFormUpdate
import org.kravbank.repository.ProductRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.utils.mapper.product.ProductMapper
import java.lang.IllegalArgumentException
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class ProductService(val productRepository: ProductRepository, val projectService: ProjectService, val projectRepository: ProjectRepository)  {

    @Throws(BackendException::class)
    fun list(projectRef: String): Response  {
                //list proudcts by project ref
                val foundProjectProducts = projectRepository.findByRef(projectRef).products
                //convert to array of form
                val productsFormList = ArrayList<ProductForm>()
                for (p in foundProjectProducts) productsFormList.add(ProductMapper().fromEntity(p))
                //returns the custom product form
              return  Response.ok(productsFormList).build()
    }

    fun getProduct(id: Long): Optional<Product>? = productRepository.findByIdOptional(id)
    fun getProductByRefCustomRepo(ref: String): Product = productRepository.findByRef(ref)

    fun getProductByRefFromService(projectRef: String, productRef: String): Response {

        if (refExists(productRef) && projectService.refExists(projectRef)) {

            val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            val product = project.products.find { products ->
                products.ref == productRef
            }
            val productMapper = org.kravbank.utils.mapper.product.ProductMapper().fromEntity(product!!)

            return Response.ok(productMapper).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    fun createProduct(product: Product) = productRepository.persist(product) //persist and flush vs persist

    fun createProductFromService(projectRef: String, product: ProductForm): Response {
        //adds a product to relevant project
        try {

            val productMapper = ProductMapper().toEntity(product)
            if (projectService.refExists(projectRef)) {
                productRepository.persistAndFlush(productMapper)
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                project.products.add(productMapper)
                projectService.updateProject(project.id, project)

            } else {
                return Response.status(Response.Status.NOT_FOUND).build()
            }
            return if (productMapper.isPersistent) {
                Response.created(URI.create("/api/v1/projects/$projectRef/products" + product.ref)).build();
            } else {
                Response.status(Response.Status.BAD_REQUEST).build()
            }
        } catch (e: Exception) {
            //return Response.status(Response.Status.BAD_REQUEST).build()
            throw IllegalArgumentException("POST product failed")
        }
    }

    fun exists(id: Long): Boolean = productRepository.count("id", id) == 1L

    fun refExists(ref: String): Boolean = productRepository.count("ref", ref) == 1L


    fun deleteProduct(id: Long) = productRepository.deleteById(id)

    fun deleteProductFromService(projectRef: String, productRef: String): Response {

        /**
         * todo
         * færre repokall for å få slettet
         * teller: 4
         *
         */
        return try {
            //repo 2
            if (projectService.refExists(projectRef) && refExists(productRef)) {
                //repo 3
                val project = projectService.getProjectByRefCustomRepo(projectRef)!!
                val product = project.products.find { product -> product.ref == productRef }
                //repo 4
                val deleted = productRepository.deleteById(product!!.id)
                if (deleted) {
                    project.products.remove(product)
                    Response.noContent().build()
                } else {
                    Response.status(Response.Status.BAD_REQUEST).build()
                }
            } else
                Response.status(Response.Status.NOT_FOUND).build()
        } catch (e: Exception) {
            throw IllegalArgumentException("DELETE product failed!")
        }
    }


    fun updateProductFromService(projectRef: String, productRef: String, product: ProductFormUpdate): Response {
        if (projectService.refExists(projectRef) && refExists(productRef)){
            // if product exists in this project
            val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            //val foundProduct = getProductByRefCustomRepo(productRef)
            val productInProject = project.products.find { prod -> prod.ref == productRef }
            val productMapper = org.kravbank.utils.mapper.product.ProductUpdateMapper().toEntity(product)

            //if (product.project.ref == project.ref)

            return if (productInProject != null) {
                productRepository.update(
                    "title = ?1, description = ?2 where id= ?3",
                    productMapper.title,
                    productMapper.description,
                    //product.deletedDate,
                    productInProject.id
                )
                Response.ok(product).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }
}