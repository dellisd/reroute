package ca.derekellis.reroute.server

import kotlinx.serialization.Serializable

@Serializable
data class ServerConfig(
    val source: String? = null
)
