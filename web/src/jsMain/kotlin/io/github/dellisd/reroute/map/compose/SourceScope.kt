@file:Suppress("FunctionName")

package io.github.dellisd.reroute.map.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.dellisd.reroute.utils.jsObject

class SourceScope(private val sourceId: String, private val mapRef: mapbox.Map) {
    @Composable
    private fun <T : LayerScope> Layer(id: String, type: String, scope: T, style: @Composable T.() -> Unit) {
        ComposeNode<LayerNode, MapNodeApplier>(
            factory = {
                LayerNode(id, jsObject<mapbox.BackgroundLayer> {
                    this.type = type
                    this.source = sourceId
                    this.id = id
                })
            },
            update = {
                set(id) { this.id = id }
            },
            content = { scope.style() }
        )
    }

    @Composable
    fun BackgroundLayer(id: String, style: @Composable BackgroundLayerScope.() -> Unit = {}) {
        val styleScope = BackgroundLayerScope(id, mapRef)
        Layer(id, "background", styleScope, style)
    }

    @Composable
    fun FillLayer(id: String, style: @Composable FillLayerScope.() -> Unit = {}) {
        val styleScope = FillLayerScope(id, mapRef)
        Layer(id, "fill", styleScope, style)
    }

    @Composable
    fun LineLayer(id: String, style: @Composable LineLayerScope.() -> Unit = {}) {
        val styleScope = LineLayerScope(id, mapRef)
        Layer(id, "line", styleScope, style)
    }

    @Composable
    fun SymbolLayer(id: String, style: @Composable SymbolLayerScope.() -> Unit = {}) {
        val styleScope = SymbolLayerScope(id, mapRef)
        Layer(id, "symbol", styleScope, style)
    }

    @Composable
    fun CircleLayer(id: String, style: @Composable CircleLayerScope.() -> Unit = {}) {
        val styleScope = CircleLayerScope(id, mapRef)
        Layer(id, "circle", styleScope, style)
    }
}
