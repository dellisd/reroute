package io.github.dellisd.reroute.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.github.dellisd.reroute.db.DatabaseHelper
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class SqlJsDataSource(private val withDatabase: DatabaseHelper) : DataSource {
    override suspend fun getStop(id: String): Stop? = withDatabase { database ->
        database.stopsQueries.getById(id).executeAsOneOrNull()?.let {
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
            database.stopSearchQueries.search(query).executeAsList().map {
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
}
