package ca.derekellis.reroute.realtime

import io.github.dellisd.spatialk.geojson.Position
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class NextTrip(
  val destination: String,
  val startTime: LocalTime?,
  val adjustedScheduleTime: LocalTime,
  val adjustmentAge: Duration,
  val lastTripOfSchedule: Boolean,
  val busType: String,
  val hasBikeRack: Boolean,
  val punctuality: Int,
  val position: Position? = null,
  val gpsSpeed: Float? = null,
)
