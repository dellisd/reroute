package io.github.dellisd.reroute.map.ui

import geojson.Feature
import geojson.FeatureCollection
import geojson.Point
import io.github.dellisd.reroute.data.DataSource
import io.github.dellisd.reroute.di.AppScope
import io.github.dellisd.reroute.map.MapInteractionsManager
import io.github.dellisd.reroute.utils.jsObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject
import kotlin.js.Json

@AppScope
@Inject
class MapViewModel(private val dataSource: DataSource, private val interactionsManager: MapInteractionsManager) {
    val targetStop = interactionsManager.targetStop

    // TODO: Figure out if passing a scope into the VM like this is a good idea
    fun stopData(scope: CoroutineScope) = flow {
        emitAll(dataSource.getStops().map { list ->
            val features = list.map { stop ->
                jsObject<Feature> {
                    type = "Feature"
                    geometry = jsObject<Point> {
                        type = "Point"
                        coordinates = arrayOf(stop.location.longitude, stop.location.latitude)
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
    }.stateIn(scope, SharingStarted.Eagerly, null)
}
