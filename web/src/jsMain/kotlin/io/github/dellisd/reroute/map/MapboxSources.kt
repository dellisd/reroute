package io.github.dellisd.reroute.map

import androidx.compose.runtime.AbstractApplier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.DefaultMonotonicFrameClock
import androidx.compose.runtime.Recomposer
import geojson.GeoJsonObject
import io.github.dellisd.reroute.utils.jsObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mapbox.FillLayer
import mapbox.GeoJSONSource
import mapbox.GeoJSONSourceRaw
import mapbox.Layer
import mapbox.Source

class MapboxSourceScope(private val mapRef: mapbox.Map) {
    /**
     * TODO: Add other GeoJSONSource parameters
     */
    @Composable
    fun geoJsonSource(id: String, data: GeoJsonObject, layers: @Composable LayerScope.() -> Unit) {
        val layerScope = LayerScope(id)

        ComposeNode<Node.SourceNode, SourceApplier>(
            factory = {
                Node.SourceNode(id, jsObject<GeoJSONSourceRaw> {
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

    inner class LayerScope(private val sourceId: String) {
        @Composable
        fun fillLayer(id: String) {
            ComposeNode<Node.LayerNode, SourceApplier>(
                factory = {
                    Node.LayerNode(id, jsObject<FillLayer> {
                        type = "fill"
                        source = sourceId
                        this.id = id
                    })
                },
                update = {
                    set(id) { this.id = id }
                }
            )
        }
    }
}

fun CoroutineScope.applySources(mapRef: mapbox.Map, content: @Composable MapboxSourceScope.() -> Unit) {
    val recomposer = Recomposer(coroutineContext)
    val composition = Composition(SourceApplier(mapRef), recomposer)

    launch(DefaultMonotonicFrameClock) {
        recomposer.runRecomposeAndApplyChanges()
    }

    val sourceScope = MapboxSourceScope(mapRef)

    composition.setContent {
        sourceScope.content()
    }
}

private sealed class Node {
    class Root : Node() {
        val children = mutableListOf<SourceNode>()
    }

    class SourceNode(var id: String, var source: Source) : Node() {
        val layers: MutableList<LayerNode> = mutableListOf()
    }

    class LayerNode(var id: String, var layer: Layer) : Node()
}

private class SourceApplier(private val mapRef: mapbox.Map) : AbstractApplier<Node>(Node.Root()) {
    override fun onClear() {
        console.log("on clear")
        (root as? Node.Root)?.run {
            children.forEach { sourceNode ->
                sourceNode.layers.forEach {
                    mapRef.removeLayer(it.layer.id)
                }
                mapRef.removeSource(sourceNode.id)
            }
            children.clear()
        }
    }

    override fun insertBottomUp(index: Int, instance: Node) {
        // Ignored
    }

    override fun insertTopDown(index: Int, instance: Node) {
        when (val node = current) {
            is Node.Root -> {
                if (instance !is Node.SourceNode) throw IllegalStateException("Node must be SourceNode")

                if (mapRef.getSource(instance.id) == null) {
                    mapRef.addSource(instance.id, instance.source.unsafeCast<GeoJSONSource>())
                    node.children.add(index, instance)
                } else {
                    console.warn("Source ${instance.id} already exists")
                }
            }
            is Node.SourceNode -> {
                if (instance !is Node.LayerNode) throw IllegalStateException("Node must be LayerNode")

                if (mapRef.getLayer(instance.id) == null) {
                    mapRef.addLayer(instance.layer.unsafeCast<FillLayer>())
                    node.layers.add(index, instance)
                } else {
                    console.warn("Layer ${instance.id} already exists")
                }
            }
            else -> {}
        }

    }

    override fun move(from: Int, to: Int, count: Int) {
        console.log("move")
    }

    override fun remove(index: Int, count: Int) {
        when (val node = current) {
            is Node.SourceNode -> {
                mapRef.removeLayer(node.layers[index].id)
                node.layers.removeAt(index)
            }
            is Node.Root -> {
                val source = node.children[index]
                source.layers.forEach { mapRef.removeLayer(it.id) }
                mapRef.removeSource(source.id)

                node.children.removeAt(index)
            }
            else -> {}
        }
    }
}
