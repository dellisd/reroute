package ca.derekellis.reroute.models

import kotlinx.serialization.Serializable

/**
 * A mapping between a stop and a route variant that serves that stop.
 *
 * @param stopId Refers to the ID of a [Stop]
 * @param routeVariantId Refers to the ID of a [RouteVariant].
 * @param index The ordering in which this stop appears in this particular route variation
 */
@Serializable
data class RouteVariantsAtStop(
  val stopId: String,
  val routeVariantId: String,
  val index: Int,
)
