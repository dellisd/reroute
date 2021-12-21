package io.github.dellisd.reroute.data

import io.github.dellisd.reroute.db.DatabaseHelper
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

    override suspend fun searchStops(query: String): List<Stop> = withDatabase { database ->
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
    }
}
