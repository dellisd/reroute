package ca.derekellis.reroute.realtime

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class RealtimeMessage(
  val stop: String,
  val routes: List<NextRoute>,
  val time: LocalTime,
)
