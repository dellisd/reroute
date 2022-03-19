package io.github.dellisd.reroute.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import app.softwork.routingcompose.Router
import geojson.Feature
import io.github.dellisd.reroute.RerouteConfig
import io.github.dellisd.reroute.data.LngLat
import io.github.dellisd.reroute.map.compose.MapboxMap
import io.github.dellisd.reroute.map.compose.rememberMapboxState
import io.github.dellisd.reroute.map.ui.MapViewModel
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.hsl
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

typealias MapDemo = @Composable () -> Unit

@Composable
@Inject
fun MapDemo(viewModel: MapViewModel) {
    val scope = rememberCoroutineScope()
    val mapState = rememberMapboxState(center = LngLat(-75.7181, 45.3922), zoom = 11.0)

    val data by viewModel.stopData(scope).collectAsState(null)
    val targetStop by viewModel.targetStop.collectAsState(null)
    val router = Router.current

    LaunchedEffect(targetStop) {
        targetStop?.let {
            mapState.flyTo(center = it.location, zoom = 16.0)
        }
    }

    Div {
        MapboxMap(
            accessToken = RerouteConfig.MAPBOX_ACCESS_KEY,
            style = "mapbox://styles/mapbox/navigation-day-v1",
            state = mapState,
            // hash = true,
            containerAttrs = {
                style {
                    height(100.vh)
                    width(100.vw)
                }
            },
            events = {
                onClick(layers = listOf("stop-circles")) {
                    val features = it.asDynamic().features.unsafeCast<Array<Feature>>()
                    val target = features.firstOrNull()
                    val code: String = target?.properties.asDynamic()?.code as String

                    router.navigate("/stops/${code}")
                }
                onMouseEnter(layers = listOf("stop-circles")) {
                    it.target.getCanvas().style.cursor = "pointer"
                }
                onMouseLeave(layers = listOf("stop-circles")) {
                    it.target.getCanvas().style.cursor = ""
                }
            }
        ) {
            data?.let { safeData ->
                GeoJsonSource("stops", data = safeData) {
                    CircleLayer("stop-circles") {
                        circleColor(hsl(4.1, 89.6, 58.4))
                        circleRadius(5)
                    }
                }
            }
        }
    }
}
