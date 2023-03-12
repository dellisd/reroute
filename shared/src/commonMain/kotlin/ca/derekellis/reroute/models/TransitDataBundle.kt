package ca.derekellis.reroute.models

import kotlinx.serialization.Serializable

@Serializable
data class TransitDataBundle(
  val stops: List<Stop>,
  val routes: List<Route>,
  val routesAtStops: List<RouteAtStop>,
  val timetable: List<StopInTimetable>,
)
