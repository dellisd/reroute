package ca.derekellis.reroute.stops.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import app.softwork.routingcompose.Router
import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.stops.StopModel
import ca.derekellis.reroute.stops.StopsViewModel
import ca.derekellis.reroute.ui.InfoPanel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Text

object StopsStyleSheet : StyleSheet() {
}

typealias Stops = @Composable (String) -> Unit

@Inject
@Composable
fun Stops(viewModel: StopsViewModel, stopCode: String) {
    val model by viewModel.stop(stopCode).collectAsState(null)
    val scope = rememberCoroutineScope()
    val router = Router.current

    LaunchedEffect(model) {
        model?.let { (stop) -> viewModel.goTo(stop) }
    }

    Style(StopsStyleSheet)
    InfoPanel(onClose = {
        scope.launch {
            viewModel.goTo(null)
            router.navigate("/")
        }
    }) {
        StopContent(model)
    }
}

@Composable
fun StopContent(model: StopModel?) {
    val router = Router.current
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
