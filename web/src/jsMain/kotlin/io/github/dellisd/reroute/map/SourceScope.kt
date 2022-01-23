package io.github.dellisd.reroute.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.dellisd.reroute.map.compose.LayerNode
import io.github.dellisd.reroute.map.compose.MapNodeApplier
import io.github.dellisd.reroute.utils.jsObject
import mapbox.FillLayer

class SourceScope(private val sourceId: String, private val mapRef: mapbox.Map) {
    @Composable
    fun FillLayer(id: String, style: @Composable FillLayerScope.() -> Unit = {}) {
        val styleScope = FillLayerScope(id, mapRef)
        ComposeNode<LayerNode, MapNodeApplier>(
            factory = {
                LayerNode(id, jsObject<FillLayer> {
                    type = "fill"
                    source = sourceId
                    this.id = id
                })
            },
            update = {
                set(id) { this.id = id }
            },
            content = { styleScope.style() }
        )
    }
}
