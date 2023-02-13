package org.kravbank.service

import org.kravbank.frontend.Bank
import org.kravbank.repository.ProjectRepository
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class BankService {

    @Inject
    private lateinit var projectRepository: ProjectRepository

    fun get(): ArrayList<Bank> {

        /**
         * Generate banks
         *
         * A new list with banks, compatible with today's frontend types from cosmos database
         *
         */

        val banks = ArrayList<Bank>()

        projectRepository.listAll().forEach {
            banks.add(
                Bank(
                    id = UUID.randomUUID().toString(),
                    title = it.title,
                    description = it.description,
                    needs = it.needs,
                    codelist = it.codelist,
                    products = it.products,
                    publications = it.publications,
                    projectId = it.ref,
                    tags = null,
                    version = null, // TODO : reuse?
                    type = null,
                    sourceOriginal = null,
                    inheritedBanks = null,
                    sourceRel = null,
                    publishedDate = null,
                    deletedDate = null, //TODO : soft delete?
                )
            )
        }
        return banks
    }
}