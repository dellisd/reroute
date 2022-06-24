package ca.derekellis.reroute.stops.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.softwork.routingcompose.Router
import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.stops.StopModel
import ca.derekellis.reroute.stops.StopsViewModel
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.right
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Text

object StopsStyleSheet : StyleSheet() {
    val infoPanelContainer by style {
        width(500.px)
        height(100.percent)
        background("#FFFFFF")
        position(Position.Fixed)
        right(0.px)
        top(0.px)
        padding(0.5.em)
        overflowY("auto")
    }
}

typealias Stops = @Composable (String) -> Unit

@Inject
@Composable
fun Stops(viewModel: StopsViewModel, stopCode: String) {
    val model by viewModel.stop(stopCode).collectAsState(null)

    LaunchedEffect(model) {
        model?.let { (stop) -> viewModel.goTo(stop) }
    }

    Style(StopsStyleSheet)
    InfoPanel(model)
}

@Composable
fun InfoPanel(model: StopModel?) {
    val router = Router.current
    Div(attrs = {
        classes(StopsStyleSheet.infoPanelContainer)
    }) {
        Button(attrs = {
            onClick { router.navigate("/") }
        }) {
            Text("Close")
        }
        if (model == null) {
            Text("Not Found!")
        } else {
            val (stop, routes) = model
            H1 {
                Text(stop.name)
            }
            stop.code?.let { code ->
                H2 { Text(code) }
            }
            Div {
                Text(stop.id)
            }
            Hr()
            routes.forEach { routes -> RouteInfo(routes) }
        }
    }
}

@Composable
fun RouteInfo(variants: List<Route>) {
    val top = variants.first()
    val rest = variants.drop(1)

    Div {
        H3 {
            Text("${top.name} ${top.headsign}")
        }
        rest.forEach { route ->
            Div {
                Text(route.headsign)
            }
        }
    }
}
