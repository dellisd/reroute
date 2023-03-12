package ca.derekellis.reroute.data

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import ca.derekellis.reroute.db.DatabaseHelper
import ca.derekellis.reroute.db.RerouteDatabase
import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.models.Stop
import io.github.dellisd.spatialk.geojson.LineString
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class SqlJsDataSource(private val withDatabase: DatabaseHelper) : DataSource {
  override fun getStop(id: String): Flow<Stop?> = withDatabaseFlow { database ->
    database.stopQueries.getById(id, StopMapper).awaitAsOneOrNull()
  }

  override fun searchStops(query: String): Flow<List<Stop>> = withDatabaseFlow { database ->
    if (query.isEmpty()) return@withDatabaseFlow emptyList()

    return@withDatabaseFlow (database.stopSearchQueries.search(query, StopMapper).awaitAsList().take(5))
  }

  override fun getStops(): Flow<List<Stop>> = withDatabaseFlowFlatten { database ->
    database.stopQueries
      .getAll(StopMapper)
      .asFlow()
      .mapToList(Dispatchers.Main)
  }

  override fun getStopByCode(code: String): Flow<List<Stop>> = withDatabaseFlowFlatten { database ->
    database.stopQueries
      .getByCode(code, StopMapper)
      .asFlow()
      .mapToList(Dispatchers.Main)
  }

  override fun getRoutesAtStop(code: String): Flow<List<Route>> = withDatabaseFlowFlatten { database ->
    database.stopQueries
      .getRoutesByStopCode(code, RouteMapper)
      .asFlow()
      .mapToList(Dispatchers.Main)
  }

  private fun <T> withDatabaseFlow(block: suspend (database: RerouteDatabase) -> T): Flow<T> =
    flow { withDatabase { emit(block(it)) } }

  private fun <T> withDatabaseFlowFlatten(block: suspend (database: RerouteDatabase) -> Flow<T>): Flow<T> =
    flow { withDatabase { emitAll(block(it)) } }

  companion object {
    private val StopMapper = { id: String, code: String?, name: String, lat: Double, lon: Double ->
      Stop(id, code, name, Position(lon, lat))
    }

    private val RouteMapper =
      { id: String, gtfsId: String, name: String, headsign: String, directionId: Int, weight: Int, shape: LineString ->
        Route(id, gtfsId, name, headsign, directionId, weight, shape)
      }
  }
}
