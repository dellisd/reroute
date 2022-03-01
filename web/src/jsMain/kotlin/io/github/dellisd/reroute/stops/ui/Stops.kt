package io.github.dellisd.reroute.stops.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.softwork.routingcompose.Router
import io.github.dellisd.reroute.data.Stop
import io.github.dellisd.reroute.stops.StopsViewModel
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.right
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

object StopsStyleSheet : StyleSheet() {
    val infoPanelContainer by style {
        width(350.px)
        height(100.percent)
        background("#FFFFFF")
        position(Position.Fixed)
        right(0.px)
        top(0.px)
    }
}

typealias Stops = @Composable (String) -> Unit

@Inject
@Composable
fun Stops(viewModel: StopsViewModel, stopCode: String) {
    val stop by viewModel.stop(stopCode).collectAsState(null)

    LaunchedEffect(stop) {
        stop?.let { viewModel.goTo(it) }
    }

    Style(StopsStyleSheet)
    InfoPanel(stop)
}

@Composable
fun InfoPanel(stop: Stop?) {
    val router = Router.current
    Div(attrs = {
        classes(StopsStyleSheet.infoPanelContainer)
    }) {
        Button(attrs = {
            onClick { router.navigate("/") }
        }) {
            Text("Close")
        }
        if (stop == null) {
            Text("Not Found!")
        } else {
            Div {
                Text(stop.name)
            }
            Div {
                Text(stop.code)
            }
            Div {
                Text(stop.id)
            }
        }
    }
}
