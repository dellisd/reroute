package ca.derekellis.reroute.db

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.sqljs.initSqlDriver
import ca.derekellis.reroute.di.AppScope
import kotlinx.coroutines.await
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class DatabaseHelper {
    private var database: RerouteDatabase? = null

    suspend fun initDatabase() {
        if (database == null) {
            database = initSqlDriver().await().also { RerouteDatabase.Schema.awaitCreate(it) }.createDatabase()
        }
    }

    suspend operator fun <R> invoke(block: suspend (RerouteDatabase) -> R): R {
        initDatabase()
        return block(database!!)
    }

    private fun SqlDriver.createDatabase(): RerouteDatabase = RerouteDatabase(
        driver = this
    )
}
