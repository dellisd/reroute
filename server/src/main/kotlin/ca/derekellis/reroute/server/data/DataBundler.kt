package ca.derekellis.reroute.server.data

import ca.derekellis.kgtfs.ExperimentalKgtfsApi
import ca.derekellis.kgtfs.GtfsDb
import ca.derekellis.kgtfs.csv.ServiceId
import ca.derekellis.kgtfs.csv.StopId
import ca.derekellis.kgtfs.ext.TripSequence
import ca.derekellis.kgtfs.ext.lineString
import ca.derekellis.kgtfs.ext.uniqueTripSequences
import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.models.RouteAtStop
import ca.derekellis.reroute.models.Stop
import ca.derekellis.reroute.models.StopInTimetable
import ca.derekellis.reroute.models.TransitDataBundle
import io.github.dellisd.spatialk.geojson.Position
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jgrapht.alg.cycle.CycleDetector
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.traverse.TopologicalOrderIterator

class DataBundler {
  @OptIn(ExperimentalKgtfsApi::class)
  fun assembleDataBundle(gtfs: GtfsDb): TransitDataBundle = gtfs.query {
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

    val processedRoutes = grouped.flatMap { (key, value) ->
      val route = Routes.select { Routes.id eq value.first().gtfsId.value }.map(Routes.mapper).single()
      value.mapIndexed { i, sequence ->
        val trip = trips.getValue(sequence.trips.keys.first())
        val id = "$key#$i"
        // TODO: Develop a better way to extract headsign values
        val shape = Shapes.select { Shapes.id eq trip.shapeId!!.value }.map(Shapes.Mapper)
        Route(
          id,
          route.id.value,
          route.shortName!!,
          trip.headsign!!,
          trip.directionId!!,
          sequence.trips.size,
          shape!!.lineString(),
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
      timetable,
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
