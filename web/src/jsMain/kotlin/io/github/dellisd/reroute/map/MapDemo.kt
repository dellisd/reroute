package io.github.dellisd.reroute.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import geojson.GeoJsonObject
import io.github.dellisd.reroute.RerouteConfig
import io.github.dellisd.reroute.data.LngLat
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Pre
import org.jetbrains.compose.web.dom.Text

const val dataSetA =
    """{ "type": "FeatureCollection", "features": [ { "type": "Feature", "properties": {}, "geometry": { "type": "Polygon", "coordinates": [ [ [ -75.69700241088867, 45.41478022574541 ], [ -75.70472717285156, 45.408875366805994 ], [ -75.69588661193848, 45.40616374516014 ], [ -75.68962097167969, 45.41038176702929 ], [ -75.69219589233398, 45.41381620931187 ], [ -75.69700241088867, 45.41478022574541 ] ] ] } } ] }"""
const val dataSetB =
    """{ "type": "FeatureCollection", "features": [ { "type": "Feature", "properties": {}, "geometry": { "type": "Polygon", "coordinates": [ [ [ -75.73584079742432, 45.38856517078434 ], [ -75.74159145355225, 45.38479764051108 ], [ -75.7336950302124, 45.38238628935689 ], [ -75.72893142700195, 45.387178748844136 ], [ -75.73476791381836, 45.38485792297213 ], [ -75.73584079742432, 45.38856517078434 ] ] ] } } ] }"""

@Composable
fun MapDemo() {
    var currentStyle by remember { mutableStateOf("mapbox://styles/mapbox/navigation-day-v1") }

    var dataSetLetter by remember { mutableStateOf("A") }
    var dataSet by remember { mutableStateOf<GeoJsonObject>(JSON.parse(dataSetA)) }

    var showLayer by remember { mutableStateOf(true) }
    var showSource by remember { mutableStateOf(true) }

    var layerFillColor by remember { mutableStateOf("#FF0000") }
    var layerUseColor by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    val mapState = rememberMapboxState()

    Div {
        Button(attrs = {
            onClick {
                currentStyle = if (currentStyle == "mapbox://styles/mapbox/navigation-day-v1") {
                    "mapbox://styles/mapbox/navigation-night-v1"
                } else {
                    "mapbox://styles/mapbox/navigation-day-v1"
                }
            }
        }) {
            Text("Toggle Style")
        }

        Button(attrs = {
            onClick {
                mapState.center = LngLat(-75.70506, 45.40732)
            }
        }) {
            Text("Set Location")
        }

        Button(attrs = {
            onClick {
                scope.launch {
                    mapState.flyTo(center = LngLat(-75.70506, 45.40732), zoom = 12.0) {
                        duration = 2000
                    }
                    console.log("Pan ended")
                    console.log("New center: ${mapState.center}")
                }
            }
        }) {
            Text("Fly To")
        }

        Button(attrs = {
            onClick {
                scope.launch {
                    mapState.zoomIn()
                }
            }
        }) {
            Text("Zoom IN")
        }

        Button(attrs = {
            onClick {
                scope.launch {
                    mapState.zoomOut()
                }
            }
        }) {
            Text("Zoom OUT")
        }
        Button(attrs = {
            onClick {
                if (dataSetLetter == "A") {
                    dataSetLetter = "B"
                    dataSet = JSON.parse(dataSetB)
                } else {
                    dataSetLetter = "A"
                    dataSet = JSON.parse(dataSetA)
                }
            }
        }) {
            Text("Toggle Dataset")
        }
        Button(attrs = {
            onClick {
                showLayer = !showLayer
            }
        }) {
            Text("Toggle Layer")
        }
        Button(attrs = {
            onClick {
                showSource = !showSource
            }
        }) {
            Text("Toggle Source")
        }
        Button(attrs = {
            onClick {
                layerFillColor = (if (layerFillColor == "#FF0000") "#0000FF" else "#FF0000")
            }
        }) {
            Text("Toggle Fill Color")
        }
        Button(attrs = {
            onClick {
                layerUseColor = !layerUseColor
            }
        }) {
            Text("Toggle Use Fill Color")
        }

        MapboxMap(
            accessToken = RerouteConfig.MAPBOX_ACCESS_KEY,
            style = currentStyle,
            state = mapState,
            containerAttrs = {
                style {
                    height(720.px)
                    width(1280.px)
                }
            }
        ) {
            if (showSource) {
                GeoJsonSource("test", data = dataSet) {
                    if (showLayer) {
                        FillLayer("test-layer") {
                            if (layerUseColor) {
                                fillColor(layerFillColor)
                            }
                        }
                    }
                }
            }
        }

        Div {
            Div { Text("Current Style: $currentStyle") }
            Div { Text("Showing Layer: $showLayer") }
            Div { Text("Showing Source (and Layer): $showSource") }
            Div {
                Text("Rendering:")
                Pre { Text(if (dataSetLetter == "A") dataSetA else dataSetB) }
            }
        }
    }
}
