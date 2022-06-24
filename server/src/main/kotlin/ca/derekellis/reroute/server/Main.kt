package ca.derekellis.reroute.server

import ca.derekellis.reroute.server.di.ServerComponent
import ca.derekellis.reroute.server.di.create
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing

fun main() {
    val component = ServerComponent::class.create()

    embeddedServer(Netty, port = 8888) {
        install(ContentNegotiation) {
            json()
        }
        install(AutoHeadResponse)

        routing {
            component.dataRoute.routes()
        }
    }.start(wait = true)
}
