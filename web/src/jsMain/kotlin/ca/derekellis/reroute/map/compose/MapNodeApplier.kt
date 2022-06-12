package ca.derekellis.reroute.map.compose

import androidx.compose.runtime.AbstractApplier
import io.github.dellisd.reroute.map.compose.ExpressionNode
import io.github.dellisd.reroute.map.compose.ExpressionType
import io.github.dellisd.reroute.map.compose.LayerNode
import io.github.dellisd.reroute.map.compose.MapNode
import io.github.dellisd.reroute.map.compose.RootNode
import io.github.dellisd.reroute.map.compose.SourceNode
import mapbox.FillLayer
import mapbox.FillLayout
import mapbox.FillPaint
import mapbox.GeoJSONSource

internal class MapNodeApplier(private val mapRef: mapbox.Map) : AbstractApplier<MapNode>(RootNode()) {
    override fun onClear() {
        console.log("on clear")
        (root as? RootNode)?.run {
            children.forEach { sourceNode ->
                sourceNode.layers.forEach {
                    mapRef.removeLayer(it.layer.id)
                }
                mapRef.removeSource(sourceNode.id)
            }
            children.clear()
        }
    }

    override fun insertBottomUp(index: Int, instance: MapNode) {
        when (val node = current) {
            is SourceNode -> {
                if (instance !is LayerNode) throw IllegalStateException("Node must be a LayerNode")

                if (mapRef.getLayer(instance.id) == null) {
                    val (paintProps, layoutProps) = instance.styleProps

                    mapRef.addLayer(instance.layer.unsafeCast<FillLayer>().apply {
                        paint = paintProps.unsafeCast<FillPaint>()
                        layout = layoutProps.unsafeCast<FillLayout>()
                    })
                    node.layers.add(index, instance)
                } else {
                    console.warn("Layer ${instance.id} already exists")
                }
            }
            is LayerNode -> {
                if (instance !is ExpressionNode) throw IllegalStateException("Node must be an ExpressionNode")
                node.props.add(index, instance)
            }
            else -> {}
        }
    }

    override fun insertTopDown(index: Int, instance: MapNode) {
        when (val node = current) {
            is RootNode -> {
                if (instance !is SourceNode) throw IllegalStateException("Node must be a SourceNode")

                if (mapRef.getSource(instance.id) == null) {
                    mapRef.addSource(instance.id, instance.source.unsafeCast<GeoJSONSource>())
                    node.children.add(index, instance)
                } else {
                    console.warn("Source ${instance.id} already exists")
                }
            }
            else -> {}
        }
    }

    override fun move(from: Int, to: Int, count: Int) {}

    override fun remove(index: Int, count: Int) {
        when (val node = current) {
            is SourceNode -> {
                mapRef.removeLayer(node.layers[index].id)
                node.layers.removeAt(index)
            }
            is LayerNode -> {
                val prop = node.props[index]

                if (mapRef.getLayer(node.id) == null) {
                    console.warn("Layer ${node.id} does not exist, cannot update style")
                } else {
                    if (prop.type == ExpressionType.PAINT) {
                        mapRef.setPaintProperty(node.id, prop.name, null)
                    } else {
                        mapRef.setLayoutProperty(node.id, prop.name, null)
                    }
                }
            }
            is RootNode -> {
                val source = node.children[index]
                source.layers.forEach { mapRef.removeLayer(it.id) }
                mapRef.removeSource(source.id)

                node.children.removeAt(index)
            }
            else -> {}
        }
    }
}
