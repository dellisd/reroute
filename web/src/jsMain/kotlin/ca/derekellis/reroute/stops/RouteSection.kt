package ca.derekellis.reroute.stops

import ca.derekellis.reroute.realtime.NextTrip

data class RouteSection(
  val gtfsId: String,
  val identifier: String,
  val name: String,
  val directionId: Int,
  val nextTrips: List<NextTrip>?,
)
