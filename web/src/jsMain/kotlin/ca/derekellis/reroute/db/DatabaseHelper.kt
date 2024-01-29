package ca.derekellis.reroute.db

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.async.coroutines.await
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.async.coroutines.awaitMigrate
import app.cash.sqldelight.async.coroutines.awaitQuery
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import ca.derekellis.reroute.data.RerouteClient
import ca.derekellis.reroute.di.AppScope
import com.soywiz.klock.DateTime
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import org.w3c.dom.Worker
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Inject
@AppScope
class DatabaseHelper(private val worker: Worker, private val client: RerouteClient) {
  private val driver = WebWorkerDriver(worker)
  private val database: RerouteDatabase = RerouteDatabase(
    driver,
    MetadataAdapter = Metadata.Adapter(DateTimeAdapter),
    RouteAdapter = Route.Adapter(StringListAdapter(Json)),
    RouteVariantAdapter = RouteVariant.Adapter(IntColumnAdapter, IntColumnAdapter, LineStringAdapter),
    RouteVariantAtStopAdapter = RouteVariantAtStop.Adapter(IntColumnAdapter),
  )

  private var initialized by atomic(false)
  private val createLock = Mutex()

  suspend fun initDatabase() = createLock.withLock {
    if (!initialized) {
      migrateIfNeeded()
      loadData()
      initialized = true
    }
  }

  suspend operator fun <R> invoke(block: suspend (RerouteDatabase) -> R): R {
    initDatabase()
    return block(database)
  }

  private suspend fun migrateIfNeeded() {
    val oldVersion =
      driver.awaitQuery(null, "PRAGMA $VERSION_PRAGMA", mapper = { cursor ->
        if (cursor.next().value) {
          cursor.getLong(0)
        } else {
          null
        }
      }, 0) ?: 0L

    val newVersion = RerouteDatabase.Schema.version

    if (oldVersion == 0L) {
      RerouteDatabase.Schema.awaitCreate(driver)
      driver.await(null, "PRAGMA $VERSION_PRAGMA=$newVersion", 0)
    } else if (oldVersion < newVersion) {
      RerouteDatabase.Schema.awaitMigrate(driver, oldVersion, newVersion)
      driver.await(null, "PRAGMA $VERSION_PRAGMA=$newVersion", 0)
    }
  }

  // TODO: Move this out of the database helper when the driver supports batching
  private suspend fun loadData() {
    val metadata: DateTime? = database.metadataQueries.get().awaitAsOneOrNull()
    val shouldUpdate = metadata == null || metadata < client.getDataBundleDate()
    if (!shouldUpdate) return

    database.transaction {
      database.metadataQueries.clear()
      worker.sendMessage("load_data")
    }

    if (metadata == null) {
      database.metadataQueries.insert(Metadata(DateTime.now()))
    } else {
      database.metadataQueries.update(DateTime.now())
    }
  }

  private var messageCounter = -1

  private suspend fun Worker.sendMessage(actionString: String) = suspendCancellableCoroutine<Unit> { continuation ->
    val id = messageCounter--
    val messageListener = object : EventListener {
      override fun handleEvent(event: Event) {
        if (event.asDynamic().data.id == id) {
          removeEventListener("message", this)
          continuation.resume(Unit)
        }
      }
    }

    val errorListener = object : EventListener {
      override fun handleEvent(event: Event) {
        removeEventListener("error", this)
        continuation.resumeWithException(RuntimeException(JSON.stringify(event)))
      }
    }

    addEventListener("message", messageListener)
    addEventListener("error", errorListener)

    val action = actionString
    postMessage(js("""{id: id, action: action}"""))

    continuation.invokeOnCancellation {
      addEventListener("message", messageListener)
      addEventListener("error", errorListener)
    }
  }

  companion object {
    private const val VERSION_PRAGMA = "user_version"
  }
}
