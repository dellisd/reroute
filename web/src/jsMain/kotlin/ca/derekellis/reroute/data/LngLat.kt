package ca.derekellis.reroute.data

data class LngLat(val longitude: Double, val latitude: Double)

data class LngLatBounds(val southwest: LngLat, val northeast: LngLat) {
  constructor(west: Double, south: Double, east: Double, north: Double) : this(
    LngLat(west, south),
    LngLat(east, north),
  )
}
