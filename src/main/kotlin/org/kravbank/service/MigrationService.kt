import io.quarkus.flyway.FlywayDataSource
import org.flywaydb.core.Flyway
import javax.inject.Inject
import javax.inject.Named

class MigrationService {

    @Inject
    lateinit var flyway: Flyway

    @Inject
    @FlywayDataSource("inventory")
    lateinit var flywayForInventory: Flyway

    @Inject
    @Named("flyway_users")
    lateinit var flywayForUsers: Flyway

    fun checkMigration() {
        flyway.clean()
        flyway.migrate()
        println(flyway.info().current().version.toString())
    }
}

