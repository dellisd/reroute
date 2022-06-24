package ca.derekellis.reroute.data

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import ca.derekellis.reroute.db.DatabaseHelper
import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.models.Stop
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class SqlJsDataSource(private val withDatabase: DatabaseHelper) : DataSource {
    override suspend fun getStop(id: String): Stop? = withDatabase { database ->
        database.stopQueries.getById(id, StopMapper).awaitAsOneOrNull()
    }

    override suspend fun searchStops(query: String): List<Stop> {
        if (query.isEmpty()) return emptyList()

        return withDatabase { database ->
            database.stopSearchQueries.search(query, StopMapper).awaitAsList().take(5)
        }
    }

    override suspend fun getStops(): Flow<List<Stop>> = withDatabase { database ->
        database.stopQueries
            .getAll(StopMapper)
            .asFlow()
            .mapToList()
    }

    override suspend fun getStopByCode(code: String): Flow<List<Stop>> = withDatabase { database ->
        database.stopQueries
            .getByCode(code, StopMapper)
            .asFlow()
            .mapToList()
    }

    override suspend fun getRoutesAtStop(code: String): Flow<List<Route>> = withDatabase { database ->
        database.stopQueries
            .getRoutesByStopCode(code, RouteMapper)
            .asFlow()
            .mapToList()
    }

    companion object {
        private val StopMapper = { id: String, code: String?, name: String, lat: Double, lon: Double ->
            Stop(id, code, name, Position(lon, lat))
        }

        private val RouteMapper = { id: String, gtfsId: String, name: String, headsign: String, directionId: Int, weight: Int ->
            Route(id, gtfsId, name, headsign, directionId, weight)
        }
    }
}
