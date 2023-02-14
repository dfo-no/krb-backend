package org.kravbank.service

import io.quarkus.panache.common.Sort
import org.kravbank.domain.Project
import org.kravbank.frontend.Bank
import org.kravbank.repository.ProjectRepository
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class BankService {

    @Inject
    private lateinit var projectRepository: ProjectRepository

    fun get(
        pageSize: Int,
        page: Int,
        fieldName: String,
        order: String
    ): MutableList<Bank> {

        /**
         * Generate banks
         *
         * A new list with banks, compatible with today's frontend types from cosmos database
         *
         */

        val banks = ArrayList<Bank>()

        val response: MutableList<Project> =
            if (order.lowercase() == "desc")
                projectRepository.findAll(Sort.descending(fieldName))
                    .page<Project>(page, pageSize)
                    .list()
            else {
                projectRepository.findAll(Sort.ascending(fieldName))
                    .page<Project>(page, pageSize)
                    .list()
            }

        response.forEach {
            banks.add(
                Bank(
                    id = it.ref, // set by project ref
                    title = it.title,
                    description = it.description,
                    needs = it.needs,
                    codelist = it.codelist,
                    products = it.products,
                    publications = it.publications,
                    type = "bank",
                    version = it.publications.lastOrNull()?.version,
                    publishedDate = it.publications.lastOrNull()?.date.toString()
                )
            )
        }
        return banks
    }
}