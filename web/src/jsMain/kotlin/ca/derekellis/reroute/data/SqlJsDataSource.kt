package ca.derekellis.reroute.data

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import ca.derekellis.reroute.db.DatabaseHelper
import ca.derekellis.reroute.di.AppScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import kotlin.coroutines.CoroutineContext

@Inject
@AppScope
class SqlJsDataSource(private val withDatabase: DatabaseHelper) : DataSource {
    override suspend fun getStop(id: String): Stop? = withDatabase { database ->
        database.stopsQueries.getById(id).awaitAsOneOrNull()?.let {
            Stop(
                it.id,
                it.code,
                it.name,
                it.desc,
                LngLat(it.lon, it.lat),
                it.zone_id?.toInt(),
                it.url,
                it.location_type.toInt()
            )
        }
    }

    override suspend fun searchStops(query: String): List<Stop> {
        if (query.isEmpty()) return emptyList()

        return withDatabase { database ->
            database.stopSearchQueries.search(query).awaitAsList().map {
                Stop(
                    it.id,
                    it.code,
                    it.name,
                    it.desc,
                    LngLat(it.lon, it.lat),
                    it.zone_id?.toInt(),
                    it.url,
                    it.location_type.toInt()
                )
            }
        }.take(5)
    }

    override suspend fun getStops(): Flow<List<Stop>> = withDatabase { database ->
        database.stopsQueries
            .getAll { id, code, name, desc, lat, lon, zone_id, url, location_type ->
                Stop(
                    id,
                    code,
                    name,
                    desc,
                    LngLat(lon, lat),
                    zone_id?.toInt(),
                    url,
                    location_type.toInt()
                )
            }
            .asFlow()
            .mapToList()
    }

    override suspend fun getStopByCode(code: String): Flow<List<Stop>> = withDatabase { database ->
        database.stopsQueries
            .getByCode(code) { id, code, name, desc, lat, lon, zone_id, url, location_type ->
                Stop(
                    id,
                    code,
                    name,
                    desc,
                    LngLat(lon, lat),
                    zone_id?.toInt(),
                    url,
                    location_type.toInt()
                )
            }
            .asFlow()
            .mapToList()
    }

    /**
     * TODO: Remove this once the next snapshot is built
     */
    private fun <T : Any> Flow<Query<T>>.mapToList(
        context: CoroutineContext = Dispatchers.Default
    ): Flow<List<T>> = map {
        withContext(context) {
            it.awaitAsList()
        }
    }
}
