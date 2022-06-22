package io.github.dellisd.reroute.map.compose

import ca.derekellis.reroute.utils.jsObject
import mapbox.Layer
import mapbox.Source

internal sealed class MapNode

internal class RootNode : MapNode() {
    val children = mutableListOf<SourceNode>()
}

internal class SourceNode(var id: String, var source: Source) : MapNode() {
    val layers: MutableList<LayerNode> = mutableListOf()
}

internal class LayerNode(var id: String, var layer: Layer) : MapNode() {
    val props: MutableList<ExpressionNode> = mutableListOf()

    val styleProps: Pair<dynamic, dynamic>
        get() {
            val groups = props.groupBy { it.type }
            val paintProps = groups[ExpressionType.PAINT]?.let {
                val obj = jsObject<dynamic> {  }
                it.forEach { prop ->
                    obj[prop.name] = prop.expression
                }
                obj
            } ?: jsObject {  }

            val layoutProps = groups[ExpressionType.LAYOUT]?.let {
                val obj = jsObject<dynamic> {  }
                it.forEach { prop ->
                    obj[prop.name] = prop.expression
                }
                obj
            } ?: jsObject {  }

            return Pair(paintProps, layoutProps)
        }
}

internal class ExpressionNode(var name: String, var expression: dynamic, val type: ExpressionType) : MapNode()

internal enum class ExpressionType {
    LAYOUT,
    PAINT;
}
