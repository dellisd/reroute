package ca.derekellis.reroute.map.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.DefaultMonotonicFrameClock
import androidx.compose.runtime.Recomposer
import geojson.GeoJsonObject
import io.github.dellisd.reroute.map.compose.SourceNode
import ca.derekellis.reroute.utils.jsObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mapbox.GeoJSONSource
import mapbox.GeoJSONSourceRaw

@Suppress("FunctionName")
class MapScope(private val mapRef: mapbox.Map) {
    /**
     * TODO: Add other GeoJSONSource parameters
     */
    @Composable
    fun GeoJsonSource(id: String, data: GeoJsonObject, layers: @Composable SourceScope.() -> Unit) {
        val layerScope = SourceScope(id, mapRef)

        ComposeNode<SourceNode, MapNodeApplier>(
            factory = {
                SourceNode(id, jsObject<GeoJSONSourceRaw> {
                    type = "geojson"
                    this.data = data
                })
            },
            update = {
                set(id) { this.id = id }
                set(data) {
                    (this.source.unsafeCast<GeoJSONSource>()).data = data
                    mapRef.getSource(id)?.setData(data)

                    return@set
                }
            },
            content = { layerScope.layers() }
        )
    }
}

fun CoroutineScope.applySources(mapRef: mapbox.Map, content: @Composable MapScope.() -> Unit) {
    val recomposer = Recomposer(coroutineContext)
    val composition = Composition(MapNodeApplier(mapRef), recomposer)

    launch(DefaultMonotonicFrameClock) {
        recomposer.runRecomposeAndApplyChanges()
    }

    val sourceScope = MapScope(mapRef)

    composition.setContent {
        sourceScope.content()
    }
}
