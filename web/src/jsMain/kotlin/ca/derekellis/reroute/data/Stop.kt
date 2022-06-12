package ca.derekellis.reroute.data

data class Stop(
    val id: String,
    val code: String,
    val name: String,
    val desc: String?,
    val location: LngLat,
    val zoneId: Int?,
    val url: String?,
    val locationType: Int
)
