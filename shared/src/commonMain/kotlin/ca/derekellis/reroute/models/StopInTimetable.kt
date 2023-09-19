package ca.derekellis.reroute.models

import kotlinx.serialization.Serializable

/**
 * The ordering of a stop in a specified route variant's timetable
 *
 * @param stopId Refers to the ID of a [Stop]
 * @param routeVariantId Refers to the GTFS ID of a [Route]
 * @param index The ordering in which this stop appears in the route's timetable
 */
@Serializable
data class StopInTimetable(
  val stopId: String,
  val routeVariantId: String,
  val index: Int,
)
