package io.github.dellisd.reroute.map.compose

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
}

internal class ExpressionNode(var name: String, var expression: dynamic, val type: ExpressionType) : MapNode()

internal enum class ExpressionType {
    LAYOUT,
    PAINT;
}
