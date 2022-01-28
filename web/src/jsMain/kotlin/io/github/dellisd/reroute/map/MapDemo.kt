package io.github.dellisd.reroute.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import geojson.GeoJsonObject
import io.github.dellisd.reroute.RerouteConfig
import io.github.dellisd.reroute.data.LngLat
import io.github.dellisd.reroute.map.compose.MapboxMap
import io.github.dellisd.reroute.map.compose.rememberMapboxState
import io.github.dellisd.reroute.map.ui.MapViewModel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.hsl
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

typealias MapDemo = @Composable () -> Unit

@Composable
@Inject
fun MapDemo(viewModel: MapViewModel) {
    val scope = rememberCoroutineScope()
    val mapState = rememberMapboxState(center = LngLat(-75.7181, 45.3922), zoom = 11.0)

    val data by viewModel.stopData.collectAsState(null)
    val targetStop by viewModel.targetStop.collectAsState(null)

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
            hash = true,
            containerAttrs = {
                style {
                    height(100.vh)
                    width(100.vw)
                }
            }
        ) {
            data?.let { safeData ->
                GeoJsonSource("stops", data = JSON.parse(safeData)) {
                    CircleLayer("stop-circles") {
                        circleColor(hsl(4.1, 89.6, 58.4))
                        circleRadius(5)
                    }
                }
            }
        }
    }
}
