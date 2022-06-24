package ca.derekellis.reroute.server.data

import app.cash.sqldelight.db.SqlCursor
import ca.derekellis.kgtfs.dsl.Gtfs
import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.models.RouteAtStop
import ca.derekellis.reroute.models.Stop
import ca.derekellis.reroute.models.TransitDataBundle
import ca.derekellis.reroute.server.di.ServerScope
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import me.tatarka.inject.annotations.Inject
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.notExists
import kotlin.io.path.outputStream

/**
 * TODO: Parameterize the GTFS data source
 */
@Inject
@ServerScope
class DataHandler {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val cacheDir: Path = Files.createTempDirectory("reroute")
    private val gtfs = Gtfs(
        source = "https://www.octranspo.com/files/google_transit.zip",
        dbPath = (cacheDir / "gtfs.db").toString()
    )
    private val cachePrepLock = Mutex()

    private val dataFile = cacheDir / "data.json"

    init {
        logger.debug("Writing data files to $cacheDir")
    }

    /**
     * TODO: Handle cache invalidation, somehow
     */
    @OptIn(ExperimentalSerializationApi::class)
    suspend fun getDataFile(): Path {
        cachePrepLock.withLock {
            if (dataFile.notExists()) {
                logger.info("Data file does not exist, generating one")
                val data = prepData()

                Json.encodeToStream(data, dataFile.outputStream())
            }
        }

        return dataFile
    }

    private suspend fun prepData(): TransitDataBundle = gtfs {
        val calendars = calendar.allOnDate()
        val args = createArguments(calendars.size)

        val stops = stops.getAll()
            .filter { it.latitude != null && it.name != null }
            .map { stop ->
                val position = stop.longitude?.let { lng -> stop.latitude?.let { lat -> Position(lng, lat) } }
                Stop(stop.id.value, stop.code, stop.name!!, position!!)
            }

        //language=SQLite
        val routeQueryResults = rawQuery("""
            SELECT R.route_id as gtfs_id, route_short_name, trip_headsign, direction_id, COUNT(*) as weight
            FROM Trip
                     JOIN Route R on Trip.route_id = R.route_id
            WHERE Trip.service_id IN $args
            GROUP BY Trip.route_id, trip_headsign, direction_id;
        """.trimIndent(), mapper = { cursor ->
            cursor.map {
                Route(
                    "",
                    it.getString(0)!!,
                    it.getString(1)!!,
                    it.getString(2)!!,
                    it.getLong(3)!!.toInt(),
                    it.getLong(4)!!.toInt()
                )
            }
        }, parameters = calendars.size, binders = {
            calendars.forEachIndexed { i, calendar -> bindString(i + 1, calendar.serviceId.value) }
        })

        val routes = routeQueryResults
            .groupBy { "${it.name}-${it.directionId}" }
            .mapValues { (key, values) -> values.mapIndexed { i, route -> route.copy(id = "$key#$i") } }
            .flatMap { (_, values) -> values }
        val routesIndex = routes.associateBy { "${it.gtfsId}-${it.directionId}-${it.headsign}" }

        //language=SQLite
        val mappingQueryResults = rawQuery("""
            SELECT DISTINCT stop_id, route_id, direction_id, trip_headsign
            FROM StopTime
                     JOIN Trip T on StopTime.trip_id = T.trip_id
            WHERE T.service_id IN $args;
        """.trimIndent(), mapper = { cursor ->
            cursor.map {
                val routeKey = "${it.getString(1)}-${it.getLong(2)}-${it.getString(3)}"
                RouteAtStop(it.getString(0)!!, routesIndex.getValue(routeKey).id)
            }
        }, parameters = calendars.size, binders = {
            calendars.forEachIndexed { i, calendar -> bindString(i + 1, calendar.serviceId.value) }
        })

        TransitDataBundle(stops, routes, mappingQueryResults)
    }

    private fun <T> SqlCursor.map(mapper: (SqlCursor) -> T): List<T> = buildList {
        while (next()) {
            add(mapper(this@map))
        }
    }

    private fun createArguments(count: Int): String {
        if (count == 0) return "()"

        return buildString(count + 2) {
            append("(?")
            repeat(count - 1) {
                append(",?")
            }
            append(')')
        }
    }
}
