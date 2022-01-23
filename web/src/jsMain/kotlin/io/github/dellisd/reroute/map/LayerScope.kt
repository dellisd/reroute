package io.github.dellisd.reroute.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.dellisd.reroute.map.compose.ExpressionNode
import io.github.dellisd.reroute.map.compose.ExpressionType
import io.github.dellisd.reroute.map.compose.MapNodeApplier

enum class Anchor(val value: String) {
    Map("map"),
    Viewport("viewport")
}

enum class Visibility(val value: String) {
    Visible("visible"),
    None("none")
}

abstract class LayerScope(protected val layerId: String, private val mapRef: mapbox.Map) {
    @Composable
    protected fun paint(name: String, value: Any?) {
        ComposeNode<ExpressionNode, MapNodeApplier>(
            factory = {
                ExpressionNode(name, value, ExpressionType.PAINT)
            },
            update = {
                set(value) {
                    this.expression = value
                    mapRef.setPaintProperty(layerId, this.name, value)
                }
            }
        )
    }

    @Composable
    protected fun layout(name: String, value: Any?) {
        ComposeNode<ExpressionNode, MapNodeApplier>(
            factory = {
                ExpressionNode(name, value, ExpressionType.PAINT)
            },
            update = {
                set(value) {
                    this.expression = value
                    mapRef.setLayoutProperty(layerId, this.name, value)
                }
            }
        )
    }

    @Composable
    fun visibility(visibility: Visibility) {
        layout("visibility", visibility.value)
    }
}

class FillLayerScope(layerId: String, mapRef: mapbox.Map) : LayerScope(layerId, mapRef) {
    @Composable
    fun fillAntialias(value: Boolean) {
        paint("fill-antialias", value)
    }

    @Composable
    fun fillColor(color: String) {
        paint("fill-color", color)
    }

    @Composable
    fun fillOpacity(opacity: Double) {
        paint("fill-opacity", opacity)
    }

    @Composable
    fun fillOutlineColor(color: String) {
        paint("fill-outline-color", color)
    }

//    @Composable
//    fun fillPattern(image: dynamic) {
//        paint("fill-pattern", image)
//    }

    @Composable
    fun fillSortKey(key: Double) {
        layout("fill-sort-key", key)
    }

    @Composable
    fun fillTranslate(x: Double, y: Double) {
        paint("fill-translate", arrayOf(x, y))
    }

    @Composable
    fun fillTranslateAnchor(anchor: Anchor) {
        paint("fill-translate-anchor", anchor.value)
    }
}
