package io.github.dellisd.reroute.map.ui

import io.github.dellisd.reroute.data.DataSource
import io.github.dellisd.reroute.data.Stop
import io.github.dellisd.reroute.db.DatabaseHelper
import io.github.dellisd.reroute.di.AppScope
import io.github.dellisd.reroute.map.MapInteractionsManager
import io.github.dellisd.spatialk.geojson.dsl.feature
import io.github.dellisd.spatialk.geojson.dsl.featureCollection
import io.github.dellisd.spatialk.geojson.dsl.point
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
class MapViewModel(private val dataSource: DataSource, private val interactionsManager: MapInteractionsManager) {
    val targetStop = interactionsManager.targetStop

    val stopData = flow {
        emitAll(dataSource.getStops().map { list ->
            featureCollection {
                list.forEach { stop ->
                    +feature {
                        geometry = point(stop.location.longitude, stop.location.latitude)
                        properties {
                            "name" to stop.name
                            "code" to stop.code
                            "id" to stop.id
                        }
                    }
                }
            }.json
        })
    }
}