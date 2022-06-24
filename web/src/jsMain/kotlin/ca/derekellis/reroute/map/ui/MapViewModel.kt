package ca.derekellis.reroute.map.ui

import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.map.MapInteractionsManager
import ca.derekellis.reroute.utils.jsObject
import geojson.Feature
import geojson.FeatureCollection
import geojson.Point
import geojson.Position
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import kotlin.js.Json

@AppScope
@Inject
class MapViewModel(private val dataSource: DataSource, private val interactionsManager: MapInteractionsManager) {
    val targetStop = interactionsManager.targetStop

    val stopData = flow {
        emitAll(dataSource.getStops().map { list ->
            val features = list.map { stop ->
                jsObject<Feature> {
                    type = "Feature"
                    geometry = jsObject<Point> {
                        type = "Point"
                        coordinates = stop.position.coordinates.toTypedArray().unsafeCast<Position>()
                        properties = jsObject<dynamic> {
                            this.name = stop.name
                            this.code = stop.code
                            this.id = stop.id
                        }.unsafeCast<Json?>()
                    }
                }
            }

            jsObject<FeatureCollection> {
                type = "FeatureCollection"
                this.features = features.toTypedArray()
            }
        })
    }
}
