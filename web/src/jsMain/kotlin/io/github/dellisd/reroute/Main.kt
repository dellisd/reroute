package io.github.dellisd.reroute

import io.github.dellisd.reroute.di.AppComponent
import io.github.dellisd.reroute.di.create
import org.jetbrains.compose.web.renderComposable

fun main() {
    val component = AppComponent::class.create()

    renderComposable(rootElementId = "root") {
        component.application()
    }
}
