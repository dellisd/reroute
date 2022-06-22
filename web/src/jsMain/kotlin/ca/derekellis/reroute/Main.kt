package ca.derekellis.reroute

import app.softwork.routingcompose.HashRouter
import ca.derekellis.reroute.di.AppComponent
import ca.derekellis.reroute.di.create
import ca.derekellis.reroute.ui.AppStylesheet
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

external fun require(module: String): dynamic

fun main() {
    require("mapbox-gl/dist/mapbox-gl.css")
    val component = AppComponent::class.create()

    component.rerouteApplication.init()

    renderComposable(rootElementId = "root") {
        Style(AppStylesheet)

        HashRouter(initPath = "/") {
            component.mapDemo()
            component.application()

            route("/stops") {
                string { code ->
                    component.stops(code)
                }
            }
        }
    }
}
