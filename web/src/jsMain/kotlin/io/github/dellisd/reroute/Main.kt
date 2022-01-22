package io.github.dellisd.reroute

import io.github.dellisd.reroute.di.AppComponent
import io.github.dellisd.reroute.di.create
import io.github.dellisd.reroute.map.MapDemo
import io.github.dellisd.reroute.ui.AppStylesheet
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

external fun require(module: String): dynamic

fun main() {
    require("mapbox-gl/dist/mapbox-gl.css")
    val component = AppComponent::class.create()

    component.rerouteApplication.init()

    renderComposable(rootElementId = "root") {
        Style(AppStylesheet)

        component.application()
        MapDemo()
    }
}
