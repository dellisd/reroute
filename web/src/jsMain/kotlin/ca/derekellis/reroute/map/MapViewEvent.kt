package ca.derekellis.reroute.map

sealed interface MapViewEvent

data class StopClick(val code: String) : MapViewEvent
