package org.kravbank.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import org.kravbank.domain.Code
import org.kravbank.domain.Codelist
import org.kravbank.form.CodelistForm
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CodelistRepository: PanacheRepository<Codelist> {

}