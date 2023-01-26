import io.quarkus.liquibase.LiquibaseFactory
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject


@ApplicationScoped
class MigrationService {
    // You can Inject the object if you want to use it manually
    @Inject
    lateinit var liquibaseFactory: LiquibaseFactory

    fun checkMigration() {
        // Get the list of liquibase change set statuses
        liquibaseFactory.createLiquibase().use { liquibase ->
            val status = liquibase.getChangeSetStatuses(
                liquibaseFactory.createContexts(), liquibaseFactory.createLabels()
            )
        }
    }
}
