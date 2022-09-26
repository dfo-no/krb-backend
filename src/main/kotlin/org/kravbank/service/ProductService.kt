package org.kravbank.service

import org.kravbank.domain.Product
import org.kravbank.form.product.ProductForm
import org.kravbank.repository.ProductRepository
import org.kravbank.utils.product.ProductMapper
import java.lang.IllegalArgumentException
import java.net.URI
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class ProductService(val productRepository: ProductRepository, val projectService: ProjectService) {
    fun listProducts(): List<Product>  = productRepository.listAll()

    fun listProductsFromService(projectRef : String) : Response {
        return try {
            if (projectService.refExists(projectRef)) {
                //list proudcts by project ref
                val projectProductList = projectService.getProjectByRefCustomRepo(projectRef)!!.products
                //convert to array of form
                val productsFormList = ArrayList<ProductForm>()
                for (p in projectProductList) productsFormList.add(ProductMapper().fromEntity(p))
                //returns the custom product form
                Response.ok(productsFormList).build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        }catch (e: Exception) {
            throw IllegalArgumentException("GET list of products failed")
        }
    }

    fun getProduct(id: Long): Optional<Product>? = productRepository.findByIdOptional(id)
    fun getProductByRefCustomRepo (ref: String): Product = productRepository.findByRef(ref)

    fun getProductByRefFromService (projectRef: String, productRef: String) : Response {

        if (refExists(productRef) && projectService.refExists(projectRef)) {

            val project = projectService.getProjectByRefCustomRepo(projectRef)!!
            val product = project.products.find { products ->
                products.ref == productRef
            }
            val productMapper = ProductMapper().fromEntity(product!!)

            return Response.ok(productMapper).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    fun createProduct(product: Product) = productRepository.persist(product) //persist and flush vs persist

    fun createProductFromService (projectRef: String, product: ProductForm): Response {
        //adds a product to relevant project
        try {
            val productMapper = ProductMapper().toEntity(product)
            if (projectService.refExists(projectRef)) {
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

    fun updateProduct(id: Long, product: Product) {
        productRepository.update("title = ?1, description = ?2 where id= ?3",
            product.title,
            product.description,
            id)
    }
}