package ca.derekellis.reroute.data

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import ca.derekellis.reroute.db.DatabaseHelper
import ca.derekellis.reroute.db.Metadata
import ca.derekellis.reroute.db.StopAtRoute
import ca.derekellis.reroute.db.StopInTimetable
import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.models.Stop
import com.soywiz.klock.DateTime
import me.tatarka.inject.annotations.Inject
import ca.derekellis.reroute.db.Route as DbRoute
import ca.derekellis.reroute.db.Stop as DbStop

@AppScope
@Inject
class ResourceLoader(private val withDatabase: DatabaseHelper, private val client: RerouteClient) {
    suspend fun loadStopsToDatabase() {
        val metadata: DateTime? = withDatabase { database -> database.metadataQueries.get().awaitAsOneOrNull() }
        val shouldUpdate = metadata == null || metadata < client.getDataBundleDate()
        if (!shouldUpdate) return

        val data = client.getDataBundle()
        withDatabase { database ->
            database.transaction {
                data.stops.forEach { database.stopQueries.insert(it.toDb()) }
            }

            database.transaction {
                data.routes.forEach { database.routeQueries.insert(it.toDb()) }
                data.routesAtStops.forEach { database.stopAtRouteQueries.insert(StopAtRoute(it.stopId, it.routeId, it.index)) }
                data.timetable.forEach { database.stopInTimetableQueries.insert(StopInTimetable(it.stopId, it.routeId, it.index)) }
            }

            database.metadataQueries.clear()
            val newMetadata = Metadata(DateTime.now())
            database.metadataQueries.insert(newMetadata)
        }
    }

    private fun Stop.toDb(): DbStop {
        return DbStop(id, code, name, position.latitude, position.longitude)
    }

    private fun Route.toDb(): DbRoute {
        return DbRoute(id, gtfsId, name, headsign, directionId, weight, shape)
    }
}
