package ca.derekellis.reroute.realtime

import kotlinx.serialization.Serializable

@Serializable
data class NextRoute(
  val number: String,
  val directionId: Int,
  val direction: String,
  val heading: String,
  val trips: List<NextTrip>,
)
