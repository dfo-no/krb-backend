package org.kravbank.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.kravbank.dao.ProductRefForm
import org.kravbank.dao.RequirementVariantForm
import org.kravbank.domain.Product
import org.kravbank.domain.Project
import org.kravbank.domain.RequirementVariant
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.*
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENTVARIANT_BADREQUEST_CREATE
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequirementVariantService(
    val requirementVariantRepository: RequirementVariantRepository,
    val needRepository: NeedRepository,
    val requirementRepository: RequirementRepository,
    val projectRepository: ProjectRepository,
    val productRepository: ProductRepository

) {


    private val objectMapper = jacksonObjectMapper()

    @Throws(BackendException::class)
    fun get(
        projectRef: String,
        needRef: String,
        requirementRef: String,
        requirementVariantRef: String
    ): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundNeed = needRepository.findByRef(foundProject.id, needRef)

        val foundRequirement = requirementRepository.findByRef(foundProject.id, foundNeed.id, requirementRef)

        return requirementVariantRepository.findByRef(foundRequirement.id, requirementVariantRef)
    }

    fun list(
        projectRef: String,
        needRef: String,
        requirementRef: String
    ): List<RequirementVariant> {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundNeed = needRepository.findByRef(foundProject.id, needRef)

        val foundRequirement = requirementRepository.findByRef(foundProject.id, foundNeed.id, requirementRef)

        return requirementVariantRepository.listAllRequirementVariants(foundRequirement.id)
    }


    @Throws(BackendException::class)
    fun create(
        projectRef: String,
        needRef: String,
        requirementRef: String,
        newRequirementVariant: RequirementVariantForm
    ): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundNeed = needRepository.findByRef(foundProject.id, needRef)

        val foundRequirement = requirementRepository.findByRef(foundProject.id, foundNeed.id, requirementRef)

        val serializedProductsFromBody = objectMapper.writeValueAsString(newRequirementVariant.products)

        val productsExists = validateAndFilterExistingProducts(foundProject, serializedProductsFromBody)


        return RequirementVariantForm().toEntity(newRequirementVariant).apply {
            requirement = foundRequirement
            if (productsExists.first) products = productsExists.second

        }.also {
            requirementVariantRepository.persistAndFlush(it)
            if (!requirementVariantRepository.isPersistent(it))
                throw BadRequestException(
                    REQUIREMENTVARIANT_BADREQUEST_CREATE
                )
        }
    }

    fun deserializeProducts(serialized: String): MutableList<ProductRefForm>? =
        objectMapper.readValue<MutableList<ProductRefForm>>(
            serialized,
            objectMapper.typeFactory
                .constructCollectionType(
                    MutableList::class.java,
                    ProductRefForm::class.java
                )
        )

    fun validateAndFilterExistingProducts(project: Project, str: String):
            Pair<Boolean, MutableList<Product>> {

        // Get all existing products
        val existingProducts = productRepository.listAllProducts(project.id)

        // Deserialize the product list
        val deserializedProductList = deserializeProducts(str)

        // Initialize a new list for matching products
        val matchingProducts = mutableListOf<Product>()

        // Iterate over each deserialized product
        var allProductsExist = true
        deserializedProductList?.forEach { refForm ->
            val matchingProduct = existingProducts.find { it.ref == refForm.ref }
            if (matchingProduct != null) {
                matchingProducts.add(matchingProduct)
            } else {
                allProductsExist = false
            }
        }

        // Return the list of matching products and the boolean marker
        return Pair(allProductsExist, matchingProducts)
    }

    fun delete(
        projectRef: String,
        needRef: String,
        requirementRef: String,
        requirementVariantRef: String
    ): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundNeed = needRepository.findByRef(foundProject.id, needRef)

        val foundRequirement = requirementRepository.findByRef(foundProject.id, foundNeed.id, requirementRef)

        val foundRequirementVariant =
            requirementVariantRepository.findByRef(foundRequirement.id, requirementVariantRef)

        return try {
            requirementVariantRepository.deleteById(foundRequirementVariant.id)
            foundRequirementVariant
        } catch (e: Exception) {
            throw BackendException("Failed to delete requirement variant")
        }
    }

    @Throws(BackendException::class)
    fun update(
        projectRef: String,
        needRef: String,
        requirementRef: String,
        requirementVariantRef: String,
        updatedReqVariant: RequirementVariantForm
    ): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundNeed = needRepository.findByRef(foundProject.id, needRef)

        val foundRequirement = requirementRepository.findByRef(foundProject.id, foundNeed.id, requirementRef)

        val foundReqVariant = requirementVariantRepository.findByRef(foundRequirement.id, requirementVariantRef)

        return RequirementVariantForm().toEntity(updatedReqVariant).apply {
            ref = foundReqVariant.ref
        }.also {
            requirementVariantRepository.updateRequirementVariant(foundReqVariant.id, it)
        }
    }
}