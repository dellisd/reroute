package ca.derekellis.reroute.server.config

import kotlinx.serialization.Serializable

@Serializable
data class OcTranspoCredentials(val appId: String, val apiKey: String)
