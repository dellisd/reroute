package io.github.dellisd.reroute.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import kotlinx.coroutines.await
import me.tatarka.inject.annotations.Inject

@Inject
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
