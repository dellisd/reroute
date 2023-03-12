package ca.derekellis.reroute.models

import kotlinx.serialization.Serializable

/**
 * The ordering of a stop in a specified route's timetable
 *
 * @param stopId Refers to the ID of a [Stop]
 * @param routeId Refers to the GTFS ID of a [Route]
 * @param index The ordering in which this stop appears in the route's timetable
 */
@Serializable
data class StopInTimetable(
  val stopId: String,
  val routeId: String,
  val index: Int,
)
