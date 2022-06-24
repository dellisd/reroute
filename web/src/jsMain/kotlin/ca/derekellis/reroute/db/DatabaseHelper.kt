package ca.derekellis.reroute.db

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.async.coroutines.await
import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.async.coroutines.awaitMigrate
import app.cash.sqldelight.async.coroutines.awaitQuery
import app.cash.sqldelight.driver.sqljs.worker.JsWorkerSqlDriver
import ca.derekellis.reroute.di.AppScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.tatarka.inject.annotations.Inject
import org.w3c.dom.Worker

@Inject
@AppScope
class DatabaseHelper {
    //language=JavaScript
    private val driver =
        JsWorkerSqlDriver(js("""new Worker(new URL("./worker.js", import.meta.url))""").unsafeCast<Worker>())
    private val database: RerouteDatabase = RerouteDatabase(
        driver,
        MetadataAdapter = Metadata.Adapter(DateTimeAdapter),
        RouteAdapter = Route.Adapter(IntColumnAdapter, IntColumnAdapter)
    )

    private val createLock = Mutex()

    suspend fun initDatabase() = createLock.withLock {
        migrateIfNeeded()
    }

    suspend operator fun <R> invoke(block: suspend (RerouteDatabase) -> R): R {
        initDatabase()
        return block(database)
    }

    private suspend fun migrateIfNeeded() {
        val oldVersion =
            driver.awaitQuery(null, "PRAGMA $versionPragma", mapper = { cursor ->
                if (cursor.next()) {
                    cursor.getLong(0)?.toInt()
                } else {
                    null
                }
            }, 0) ?: 0

        val newVersion = RerouteDatabase.Schema.version

        if (oldVersion == 0) {
            RerouteDatabase.Schema.awaitCreate(driver)
            driver.await(null, "PRAGMA $versionPragma=$newVersion", 0)
        } else if (oldVersion < newVersion) {
            RerouteDatabase.Schema.awaitMigrate(driver, oldVersion, newVersion)
            driver.await(null, "PRAGMA $versionPragma=$newVersion", 0)
        }
    }

    companion object {
        private const val versionPragma = "user_version"
    }
}
