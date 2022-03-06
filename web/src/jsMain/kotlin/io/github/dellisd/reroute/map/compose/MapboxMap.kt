@file:Suppress("FunctionName")

package io.github.dellisd.reroute.map.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.github.dellisd.reroute.data.LngLat
import io.github.dellisd.reroute.data.LngLatBounds
import io.github.dellisd.reroute.utils.jsObject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import mapbox.AnimationOptions
import mapbox.CameraOptions
import mapbox.FitBoundsOptions
import mapbox.PaddingOptions
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement
import kotlin.coroutines.resume

private fun LngLat.toArray(): Array<Double> = arrayOf(longitude, latitude)

private fun LngLatBounds.toArray(): Array<Double> =
    arrayOf(southwest.longitude, southwest.latitude, northeast.longitude, northeast.latitude)

class MapboxState(
    internal val initialCenter: LngLat = LngLat(0.0, 0.0),
    internal val initialZoom: Double = 0.0,
    internal val initialBearing: Double = 0.0,
    internal val initialPitch: Double = 0.0
) {
    internal var map: mapbox.Map? = null
        set(value) {
            if (value != null) initMap()
            field = value
        }

    /**
     * Performs deferred initialization of the map with the initial state
     */
    private fun initMap() {
        center = initialCenter
        zoom = initialZoom
        bearing = initialBearing
        pitch = initialPitch
    }

    var center: LngLat
        get() = map!!.getCenter().let { LngLat(it.lng.toDouble(), it.lat.toDouble()) }
        set(value) {
            map?.setCenter(value.toArray())
        }

    var zoom: Double
        get() = map!!.getZoom().toDouble()
        set(value) {
            map?.setZoom(value)
        }

    var bearing: Double
        get() = map!!.getBearing().toDouble()
        set(value) {
            map?.setBearing(bearing)
        }

    var padding: PaddingOptions
        get() = map!!.getPadding()
        set(value) {
            map?.setPadding(value)
        }

    var pitch: Double
        get() = map!!.getPitch().toDouble()
        set(value) {
            map?.setPitch(value)
        }

    suspend fun panBy(x: Double, y: Double, options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {
        map?.panBy(arrayOf(x, y), options?.let(::jsObject))
    }

    suspend fun panTo(location: LngLat, options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {
        map?.panTo(location.toArray(), options?.let(::jsObject))
    }

    suspend fun zoomTo(zoom: Double, options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {
        map?.zoomTo(zoom, options?.let(::jsObject))
    }

    suspend fun zoomIn(options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {
        map?.zoomIn(options?.let(::jsObject))
    }

    suspend fun zoomOut(options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {
        map?.zoomOut(options?.let(::jsObject))
    }

    suspend fun rotateTo(bearing: Double, options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {
        map?.rotateTo(bearing, options?.let(::jsObject))
    }

    suspend fun resetNorth(options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {
        map?.resetNorth(options?.let(::jsObject))
    }

    suspend fun resetNorthPitch(options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {
        map?.resetNorthPitch(options?.let(::jsObject))
    }

    suspend fun snapToNorth(options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {
        map?.snapToNorth(options?.let(::jsObject))
    }

    fun cameraForBounds(bounds: LngLatBounds, options: (CameraOptions.() -> Unit)? = null): CameraOptions? {
        return map?.cameraForBounds(bounds.toArray(), options?.let(::jsObject)).unsafeCast<CameraOptions?>()
    }

    suspend fun fitBounds(bounds: LngLatBounds, options: (FitBoundsOptions.() -> Unit)? = null) = doMoveAnimation {
        map?.fitBounds(bounds, options?.let(::jsObject))
    }

    suspend fun easeTo(
        center: LngLat = this.center,
        zoom: Double = this.zoom,
        bearing: Double = this.bearing,
        pitch: Double = this.pitch,
        padding: PaddingOptions = this.padding,
        options: (AnimationOptions.() -> Unit)? = null
    ) = doMoveAnimation {
        map?.easeTo(jsObject {
            this.center = center.toArray()
            this.zoom = zoom
            this.bearing = bearing
            this.pitch = pitch
            this.padding = padding

            options?.invoke(this)
        })
    }

    suspend fun flyTo(
        center: LngLat = this.center,
        zoom: Double = this.zoom,
        bearing: Double = this.bearing,
        pitch: Double = this.pitch,
        padding: PaddingOptions = this.padding,
        options: (AnimationOptions.() -> Unit)? = null
    ) = doMoveAnimation {
        map?.flyTo(jsObject {
            this.center = center.toArray()
            this.zoom = zoom
            this.bearing = bearing
            this.pitch = pitch
            this.padding = padding

            options?.invoke(this)
        })
    }

    // TODO: fitScreenCoordinates, jumpTo, easeTo, flyTo

    /**
     * Runs the given [block], assuming that the [block] triggers a move animation on the [map].
     * Suspends until the move animation has ended.
     */
    private suspend fun doMoveAnimation(block: () -> Unit) {
        var listener: (Any) -> Unit
        var resumed = false
        suspendCancellableCoroutine<Unit> { continuation ->
            block()
            listener = { _ ->
                // If the animation is interrupted, the "moveend" listener may fire a second time before the cancellation is complete
                if (!resumed) {
                    continuation.resume(Unit)
                }
                resumed = true
            }
            map?.on("moveend", listener)

            continuation.invokeOnCancellation {
                map?.stop()
                map?.off("moveend", listener)
            }
        }
    }
}

@Composable
fun MapboxMap(
    accessToken: String,
    style: String,
    state: MapboxState = rememberMapboxState(),
    hash: Boolean = false,
    containerAttrs: AttrsScope<HTMLDivElement>.() -> Unit = {},
    events: EventsScope.() -> Unit = {},
    sources: @Composable MapScope.() -> Unit
) {
    var map: mapbox.Map? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()

    Div(attrs = {
        this.containerAttrs()

        ref {
            map = mapbox.Map(jsObject {
                this.accessToken = accessToken
                container = it
                this.style = style
                this.hash = hash

                this.center = state.initialCenter.toArray()
                this.zoom = state.initialZoom
                this.bearing = state.initialBearing
                this.pitch = state.initialPitch
            })
            console.dir(map!!)
            map?.resize()

            state.map = map

            var sourceScopeJob: Job? = null
            map?.on("style.load") {
                sourceScopeJob?.cancel()
                sourceScopeJob = scope.launch {
                    // https://github.com/mapbox/mapbox-gl-js/issues/2268#issuecomment-401979967
                    while (map?.isStyleLoaded() == false) {
                        delay(100)
                    }
                    console.log("on style loaded")
                    applySources(map!!, sources)
                    EventsScope(map!!).events()
                }
            }


            onDispose { }
        }
    })

    DisposableEffect(style) {
        map?.setStyle(style)
        onDispose { }
    }
}


@Composable
fun rememberMapboxState(
    center: LngLat = LngLat(0.0, 0.0),
    zoom: Double = 0.0,
    bearing: Double = 0.0,
    pitch: Double = 0.0
): MapboxState {
    return remember { MapboxState(center, zoom, bearing, pitch) }
}
