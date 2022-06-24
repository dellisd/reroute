package ca.derekellis.reroute.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.softwork.routingcompose.Router
import ca.derekellis.reroute.RerouteConfig
import ca.derekellis.reroute.data.LngLat
import ca.derekellis.reroute.map.compose.MapboxMap
import ca.derekellis.reroute.map.compose.rememberMapboxState
import ca.derekellis.reroute.map.ui.MapViewModel
import geojson.Feature
import kotlinx.coroutines.flow.onEach
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
    val mapState = rememberMapboxState(center = LngLat(-75.7181, 45.3922), zoom = 11.0)

    val data by remember { viewModel.stopData }
        .onEach { /* This is required to force collection?? */ }
        .collectAsState(null)
    val targetStop by viewModel.targetStop.collectAsState(null)
    val router = Router.current

    LaunchedEffect(targetStop) {
        targetStop?.let {
            val position = it.position
            mapState.flyTo(center = LngLat(position.longitude, position.latitude), zoom = 16.0)
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
