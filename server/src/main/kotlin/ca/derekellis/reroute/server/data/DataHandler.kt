package ca.derekellis.reroute.server.data

import ca.derekellis.kgtfs.domain.model.Calendar
import ca.derekellis.kgtfs.domain.model.StopId
import ca.derekellis.kgtfs.dsl.Gtfs
import ca.derekellis.kgtfs.ext.TripSequence
import ca.derekellis.kgtfs.ext.lineString
import ca.derekellis.kgtfs.ext.uniqueTripSequences
import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.models.RouteAtStop
import ca.derekellis.reroute.models.Stop
import ca.derekellis.reroute.models.StopInTimetable
import ca.derekellis.reroute.models.TransitDataBundle
import ca.derekellis.reroute.server.ServerConfig
import ca.derekellis.reroute.server.di.RerouteScope
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import me.tatarka.inject.annotations.Inject
import org.jgrapht.alg.cycle.CycleDetector
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.traverse.TopologicalOrderIterator
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
@RerouteScope
class DataHandler(private val config: ServerConfig) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val cacheDir: Path = Files.createTempDirectory("reroute")
    private val gtfs by lazy {
        requireNotNull(config.source)
        Gtfs(
            source = config.source,
            dbPath = (cacheDir / "gtfs.db").toString()
        )
    }
    private val cachePrepLock = Mutex()

    val dataFile = cacheDir / "data.json"

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
        val stops = stops.getAll().associateBy { it.id }
        val trips = trips.getAll().associateBy { it.id }
        val calendars = calendar.allOnDate().map(Calendar::serviceId).toSet()
        // Get unique sequences
        val sequences = uniqueTripSequences(calendars)

        // Group sequences by route and direction
        val grouped = sequences.groupBy {
            val trip = trips.getValue(it.trips.keys.first())
            "${it.gtfsId.value}-${trip.directionId}"
        }.toList()

        val processedRoutes = grouped.flatMap { (key, value) ->
            val route = routes.getById(value.first().gtfsId)!!
            value.mapIndexed { i, sequence ->
                val trip = trips.getValue(sequence.trips.keys.first())
                val id = "$key#$i"
                // TODO: Develop a better way to extract headsign values
                Route(
                    id,
                    route.id.value,
                    route.shortName!!,
                    trip.headsign!!,
                    trip.directionId!!,
                    sequence.trips.size,
                    trip.shape!!.lineString()
                ) to sequence.sequence.mapIndexed { index, stopId -> RouteAtStop(stopId.value, id, index) }
            }
        }

        // Build graph of each route+direction and topological ordering
        val timetable = grouped.flatMap { (_, tripSequences) ->
            val routeId = tripSequences.first().gtfsId
            buildSequenceGraph(tripSequences).mapIndexed { i, id -> StopInTimetable(id, routeId.value, i) }
        }

        val processedStops = stops.values
            .asSequence()
            .filter { it.latitude != null && it.name != null }
            .map { stop -> stop.copy(name = stop.name?.titleCase()) }
            .map { stop ->
                val position = stop.longitude?.let { lng -> stop.latitude?.let { lat -> Position(lng, lat) } }
                Stop(stop.id.value, stop.code, stop.name!!, position!!)
            }
            .toList()

        TransitDataBundle(
            processedStops,
            processedRoutes.map { (route) -> route },
            processedRoutes.flatMap { (_, stops) -> stops },
            timetable
        )
    }

    private fun buildSequenceGraph(sequences: List<TripSequence>): List<String> {
        val graph = DefaultDirectedGraph<StopId, DefaultEdge>(DefaultEdge::class.java)
        sequences.forEach {
            val inserted = mutableSetOf<StopId>()
            it.sequence.zipWithNext { a, b ->
                graph.addVertex(a)
                if (graph.addVertex(b)) {
                    graph.addEdge(a, b)
                } else if (b in inserted) {
                    // Avoid creating cycles
                    val new = StopId("${b.value}#")
                    graph.addVertex(new)
                    graph.addEdge(a, new)
                }

                inserted.add(a)
                inserted.add(b)
            }
        }

        val cycleDetector = CycleDetector(graph)
        if (cycleDetector.detectCycles()) {
            val sequence = sequences.first()
            throw IllegalStateException("Cycle detected in route ${sequence.uniqueId}#${sequence.hash}")
        }

        val iterator = TopologicalOrderIterator(graph)
        return iterator.asSequence().map { it.value.removeSuffix("#") }.toList()
    }

    /**
     * TODO: Handle edge cases
     * e.g. "Riverside 1A", or "Hillcrest H.S."
     */
    private fun String.titleCase() = lowercase()
        .split(" ")
        .joinToString(separator = " ") { word -> word.replaceFirstChar(Char::uppercase) }
}
