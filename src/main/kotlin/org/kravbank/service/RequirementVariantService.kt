package org.kravbank.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.kravbank.dao.ProductRefForm
import org.kravbank.dao.RequirementVariantForm
import org.kravbank.domain.Product
import org.kravbank.domain.Project
import org.kravbank.domain.RequirementVariant
import org.kravbank.lang.BackendException
import org.kravbank.lang.BadRequestException
import org.kravbank.repository.ProductRepository
import org.kravbank.repository.ProjectRepository
import org.kravbank.repository.RequirementRepository
import org.kravbank.repository.RequirementVariantRepository
import org.kravbank.utils.Messages.RepoErrorMsg.REQUIREMENTVARIANT_BADREQUEST_CREATE
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RequirementVariantService(
    val requirementVariantRepository: RequirementVariantRepository,
    val requirementRepository: RequirementRepository,
    val projectRepository: ProjectRepository,
    val productRepository: ProductRepository

) {


    private val objectMapper = jacksonObjectMapper()

    @Throws(BackendException::class)
    fun get(projectRef: String, requirementRef: String, requirementVariantRef: String): RequirementVariant {
        val project = projectRepository.findByRef(projectRef)

        val foundRequirement = requirementRepository.findByRef(project.id, requirementRef)

        return requirementVariantRepository.findByRef(foundRequirement.id, requirementVariantRef)
    }

    fun list(projectRef: String, requirementRef: String): List<RequirementVariant> {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)

        return requirementVariantRepository.listAllRequirementVariants(foundRequirement.id)
    }


    @Throws(BackendException::class)
    fun create(
        projectRef: String,
        requirementRef: String,
        newRequirementVariant: RequirementVariantForm
    ): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)

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

        val existingProducts = productRepository.listAllProducts(project.id)

        val deserializedProductList = deserializeProducts(str)

        val matchingProducts = mutableListOf<Product>()

        var allProductsExist = true
        deserializedProductList?.forEach { refForm ->
            val matchingProduct = existingProducts.find { it.ref == refForm.ref }
            if (matchingProduct != null) {
                matchingProducts.add(matchingProduct)
            } else {
                allProductsExist = false
            }
        }

        return Pair(allProductsExist, matchingProducts)
    }

    fun delete(projectRef: String, requirementRef: String, requirementVariantRef: String): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)

        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)

        val foundRequirementVariant = requirementVariantRepository.findByRef(foundRequirement.id, requirementVariantRef)

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
        requirementRef: String,
        requirementVariantRef: String,
        updatedReqVariant: RequirementVariantForm
    ): RequirementVariant {
        val foundProject = projectRepository.findByRef(projectRef)
        val foundRequirement = requirementRepository.findByRef(foundProject.id, requirementRef)
        val foundReqVariant = requirementVariantRepository.findByRef(foundRequirement.id, requirementVariantRef)

        val update = RequirementVariantForm().toEntity(updatedReqVariant)
        requirementVariantRepository.updateRequirementVariant(foundReqVariant.id, update)
        return update.apply { ref = foundReqVariant.ref }
    }
}