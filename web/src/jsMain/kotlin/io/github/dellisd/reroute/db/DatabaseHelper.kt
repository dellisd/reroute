package io.github.dellisd.reroute.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.sqljs.initSqlDriver
import io.github.dellisd.reroute.di.AppScope
import kotlinx.coroutines.await
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class DatabaseHelper {
    private var database: RerouteDatabase? = null

    suspend fun initDatabase() {
        if (database == null) {
            database = initSqlDriver(RerouteDatabase.Schema).await().createDatabase()
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
