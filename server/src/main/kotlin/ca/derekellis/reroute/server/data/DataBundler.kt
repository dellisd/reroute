package ca.derekellis.reroute.server.data

import ca.derekellis.kgtfs.ExperimentalKgtfsApi
import ca.derekellis.kgtfs.GtfsDb
import ca.derekellis.kgtfs.GtfsDbScope
import ca.derekellis.kgtfs.csv.ServiceId
import ca.derekellis.kgtfs.csv.StopId
import ca.derekellis.kgtfs.csv.Trip
import ca.derekellis.kgtfs.csv.TripId
import ca.derekellis.kgtfs.ext.TripSequence
import ca.derekellis.kgtfs.ext.lineString
import ca.derekellis.kgtfs.ext.uniqueTripSequences
import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.models.RouteVariant
import ca.derekellis.reroute.models.RouteVariantsAtStop
import ca.derekellis.reroute.models.Stop
import ca.derekellis.reroute.models.TransitDataBundle
import io.github.dellisd.spatialk.geojson.Position
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jgrapht.alg.cycle.CycleDetector
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.traverse.TopologicalOrderIterator

@OptIn(ExperimentalKgtfsApi::class)
class DataBundler(private val gtfs: GtfsDb) {
  fun assembleDataBundle(): TransitDataBundle = gtfs.query {
    val stops = Stops.selectAll().map(Stops.Mapper).associateBy { it.id }
    val trips = Trips.selectAll().map(Trips.Mapper).associateBy { it.id }
    val calendars = Calendars.selectAll().map { ServiceId(it[Calendars.serviceId]) }.toSet()
    // Get unique sequences
    val sequences = uniqueTripSequences(calendars)

    // Group sequences by route and direction
    val grouped = sequences.groupBy {
      val trip = trips.getValue(it.trips.keys.first())
      "${it.gtfsId.value}-${trip.directionId}"
    }.toList()

    val processedRoutes = processRoutes(grouped, trips)

    val processedStops = stops.values
      .asSequence()
      .filter { it.latitude != null && it.name != null }
      .map { stop -> stop.copy(name = stop.name?.titleCase()) }
      .map { stop ->
        val position = stop.longitude?.let { lng -> stop.latitude?.let { lat -> Position(lng, lat) } }
        Stop(stop.id.value, stop.code ?: stop.id.value, stop.name!!, position!!)
      }
      .toList()

    val uniqueRoutes = processedRoutes.map { (route) -> route }.distinctBy { it.gtfsId }
    val variants = processedRoutes.flatMap { (_, variants) -> variants }

    val groupedVariants = variants.groupBy { it.gtfsId }
    val routes = uniqueRoutes.map { route ->
      val destinations = groupedVariants.getValue(route.gtfsId)
        .groupBy { it.directionId }
        .toSortedMap().values
        .map { it.maxBy(RouteVariant::weight).headsign }

      route.copy(destinations = destinations)
    }

    TransitDataBundle(
      stops = processedStops,
      routes = routes,
      routeVariants = variants,
      routesAtStops = processedRoutes.flatMap { (_, _, stops) -> stops },
    )
  }

  context(GtfsDbScope)
  private fun processRoutes(
    grouped: List<Pair<String, List<TripSequence>>>,
    trips: Map<TripId, Trip>,
  ) = grouped.map { (key, value) ->
    val route = Routes.select { Routes.id eq value.first().gtfsId.value }.map(Routes.mapper).single()
    val variants = mutableListOf<RouteVariant>()
    val sequences = mutableListOf<RouteVariantsAtStop>()

    value.forEachIndexed { i, sequence ->
      val trip = trips.getValue(sequence.trips.keys.first())
      val id = "$key#$i"
      // TODO: Develop a better way to extract headsign values
      val shape = Shapes.select { Shapes.id eq trip.shapeId!!.value }.map(Shapes.Mapper)

      sequences += sequence.sequence.mapIndexed { index, stopId ->
        RouteVariantsAtStop(stopId.value, id, index)
      }
      variants += RouteVariant(
        id,
        route.id.value,
        trip.directionId!!,
        trip.headsign!!,
        sequence.trips.size,
        shape.lineString(),
      )
    }

    val destinations = variants
      .groupBy { it.directionId }
      .toSortedMap()
      .map { (_, subset) ->
        subset.maxBy { it.weight }.headsign
      }

    Triple(
      Route(route.id.value, route.shortName!!, destinations),
      variants,
      sequences,
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
    check(!cycleDetector.detectCycles()) {
      val sequence = sequences.first()
      "Cycle detected in route ${sequence.uniqueId}#${sequence.hash}"
    }

    val iterator = TopologicalOrderIterator(graph)
    return iterator.asSequence().map { it.value.removeSuffix("#") }.toList()
  }

  private fun String.titleCase(): String {
    val array = toCharArray()
    // Uppercase first letter.
    array[0] = Character.toUpperCase(array[0])

    // Uppercase all letters that follow a whitespace character, or a number
    for (i in 1 until array.size) {
      if (Character.isWhitespace(array[i - 1]) || Character.isDigit(array[i - 1])) {
        array[i] = Character.toUpperCase(array[i])
      } else {
        array[i] = Character.toLowerCase(array[i])
      }
    }

    return String(array)
  }
}
