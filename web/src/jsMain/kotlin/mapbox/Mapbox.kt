@file:JsModule("mapbox-gl")
@file:JsNonModule
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package mapbox

import geojson.Feature
import geojson.FeatureCollection
import geojson.Point
import org.khronos.webgl.ArrayBufferView
import org.khronos.webgl.WebGLContextEvent
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLVideoElement
import org.w3c.dom.ImageBitmap
import org.w3c.dom.ImageData
import org.w3c.dom.Node
import org.w3c.dom.TouchEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import kotlin.js.Json
import kotlin.js.Promise

external var accessToken: String

external var version: String

external var baseApiUrl: String

external var workerCount: Number

external var maxParallelImageRequests: Number

external interface `T$0` {
    var failIfMajorPerformanceCaveat: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external fun supported(options: `T$0` = definedExternally): Boolean

external fun clearStorage(callback: (err: Error) -> Unit = definedExternally)

external fun setRTLTextPlugin(pluginURL: String, callback: (error: Error) -> Unit, deferred: Boolean = definedExternally)

external fun getRTLTextPluginStatus(): String /* "unavailable" | "loading" | "loaded" | "error" */

external fun prewarm()

external fun clearPrewarmedResources()

external interface `T$1` {
    var lng: Number
    var lat: Number
}

external interface `T$2` {
    var lon: Number
    var lat: Number
}

external interface `T$3` {
    @nativeGetter
    operator fun get(`_`: String): dynamic /* Point? | JsTuple<Number, Number> */
    @nativeSetter
    operator fun set(`_`: String, value: Point)
    @nativeSetter
    operator fun set(`_`: String, value: dynamic /* JsTuple<Number, Number> */)
}

external interface DragPanOptions {
    var linearity: Number?
        get() = definedExternally
        set(value) = definedExternally
    var easing: ((t: Number) -> Number)?
        get() = definedExternally
        set(value) = definedExternally
    var deceleration: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maxSpeed: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface InteractiveOptions {
    var around: String? /* "center" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$4` {
    var layers: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var filter: Array<Any>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$5` {
    var sourceLayer: String?
        get() = definedExternally
        set(value) = definedExternally
    var filter: Array<Any>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$6` {
    var diff: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var localIdeographFontFamily: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$7` {
    var width: Number
    var height: Number
    var data: dynamic /* Uint8Array | Uint8ClampedArray */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$8` {
    var pixelRatio: Number?
        get() = definedExternally
        set(value) = definedExternally
    var sdf: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external open class Map(options: MapboxOptions = definedExternally) : Evented {
    open fun addControl(control: Control, position: String /* "top-right" | "top-left" | "bottom-right" | "bottom-left" */ = definedExternally): Map /* this */
    open fun addControl(control: Control): Map /* this */
    open fun addControl(control: IControl, position: String /* "top-right" | "top-left" | "bottom-right" | "bottom-left" */ = definedExternally): Map /* this */
    open fun addControl(control: IControl): Map /* this */
    open fun removeControl(control: Control): Map /* this */
    open fun removeControl(control: IControl): Map /* this */
    open fun hasControl(control: IControl): Boolean
    open fun resize(eventData: EventData = definedExternally): Map /* this */
    open fun getBounds(): LngLatBounds
    open fun getMaxBounds(): LngLatBounds?
    open fun setMaxBounds(lnglatbounds: LngLatBounds = definedExternally): Map /* this */
    open fun setMaxBounds(): Map /* this */
    open fun setMaxBounds(lnglatbounds: Any /* JsTuple<Any, Any> | JsTuple<Number, Number, Number, Number> | JsTuple<Number, Number> */ = definedExternally): Map /* this */
    open fun setMaxBounds(lnglatbounds: LngLat = definedExternally): Map /* this */
    open fun setMaxBounds(lnglatbounds: `T$1` = definedExternally): Map /* this */
    open fun setMaxBounds(lnglatbounds: `T$2` = definedExternally): Map /* this */
    open fun setMinZoom(minZoom: Number? = definedExternally): Map /* this */
    open fun getMinZoom(): Number
    open fun setMaxZoom(maxZoom: Number? = definedExternally): Map /* this */
    open fun getMaxZoom(): Number
    open fun setMinPitch(minPitch: Number? = definedExternally): Map /* this */
    open fun getMinPitch(): Number
    open fun setMaxPitch(maxPitch: Number? = definedExternally): Map /* this */
    open fun getMaxPitch(): Number
    open fun getRenderWorldCopies(): Boolean
    open fun setRenderWorldCopies(renderWorldCopies: Boolean = definedExternally): Map /* this */
    open fun project(lnglat: Any /* JsTuple<Number, Number> */): Point
    open fun project(lnglat: LngLat): Point
    open fun project(lnglat: `T$1`): Point
    open fun project(lnglat: `T$2`): Point
    open fun unproject(point: Point): LngLat
    open fun unproject(point: Any /* JsTuple<Number, Number> */): LngLat
    open fun isMoving(): Boolean
    open fun isZooming(): Boolean
    open fun isRotating(): Boolean
    open fun queryRenderedFeatures(pointOrBox: Point = definedExternally, options: `T$4` /* `T$4` & FilterOptions */ = definedExternally): Array<Feature>
    open fun queryRenderedFeatures(): Array<Feature>
    open fun queryRenderedFeatures(pointOrBox: Point = definedExternally): Array<Feature>
    open fun queryRenderedFeatures(pointOrBox: Any /* JsTuple<Number, Number> | JsTuple<Any, Any> */ = definedExternally, options: `T$4` /* `T$4` & FilterOptions */ = definedExternally): Array<Feature>
    open fun queryRenderedFeatures(pointOrBox: Any /* JsTuple<Number, Number> | JsTuple<Any, Any> */ = definedExternally): Array<Feature>
    open fun querySourceFeatures(sourceID: String, parameters: `T$5` /* `T$5` & FilterOptions */ = definedExternally): Array<Feature>
    open fun setStyle(style: Style, options: `T$6` = definedExternally): Map /* this */
    open fun setStyle(style: Style): Map /* this */
    open fun setStyle(style: String, options: `T$6` = definedExternally): Map /* this */
    open fun setStyle(style: String): Map /* this */
    open fun getStyle(): Style
    open fun setProjection(projection: String): Map
    open fun getProjection(): String
    open fun isStyleLoaded(): Boolean
    open fun addSource(id: String, source: GeoJSONSourceRaw): Map /* this */
    open fun addSource(id: String, source: VideoSourceRaw): Map /* this */
    open fun addSource(id: String, source: ImageSourceRaw): Map /* this */
    open fun addSource(id: String, source: CanvasSourceRaw): Map /* this */
    open fun addSource(id: String, source: VectorSource): Map /* this */
    open fun addSource(id: String, source: RasterSource): Map /* this */
    open fun addSource(id: String, source: RasterDemSource): Map /* this */
    open fun isSourceLoaded(id: String): Boolean
    open fun areTilesLoaded(): Boolean
    open fun removeSource(id: String): Map /* this */
    open fun getSource(id: String): dynamic /* GeoJSONSource | VideoSource | ImageSource | CanvasSource | VectorSourceImpl | RasterSource | RasterDemSource */
    open fun addImage(name: String, image: HTMLImageElement, options: `T$8` = definedExternally)
    open fun addImage(name: String, image: HTMLImageElement)
    open fun addImage(name: String, image: ArrayBufferView, options: `T$8` = definedExternally)
    open fun addImage(name: String, image: ArrayBufferView)
    open fun addImage(name: String, image: `T$7`, options: `T$8` = definedExternally)
    open fun addImage(name: String, image: `T$7`)
    open fun addImage(name: String, image: ImageData, options: `T$8` = definedExternally)
    open fun addImage(name: String, image: ImageData)
    open fun addImage(name: String, image: ImageBitmap, options: `T$8` = definedExternally)
    open fun addImage(name: String, image: ImageBitmap)
    open fun updateImage(name: String, image: HTMLImageElement)
    open fun updateImage(name: String, image: ArrayBufferView)
    open fun updateImage(name: String, image: `T$7`)
    open fun updateImage(name: String, image: ImageData)
    open fun updateImage(name: String, image: ImageBitmap)
    open fun hasImage(name: String): Boolean
    open fun removeImage(name: String)
    open fun loadImage(url: String, callback: (error: Error, result: Any /* HTMLImageElement | ImageBitmap */) -> Unit)
    open fun listImages(): Array<String>
    open fun addLayer(layer: BackgroundLayer, before: String = definedExternally): Map /* this */
    open fun addLayer(layer: BackgroundLayer): Map /* this */
    open fun addLayer(layer: CircleLayer, before: String = definedExternally): Map /* this */
    open fun addLayer(layer: CircleLayer): Map /* this */
    open fun addLayer(layer: FillExtrusionLayer, before: String = definedExternally): Map /* this */
    open fun addLayer(layer: FillExtrusionLayer): Map /* this */
    open fun addLayer(layer: FillLayer, before: String = definedExternally): Map /* this */
    open fun addLayer(layer: FillLayer): Map /* this */
    open fun addLayer(layer: HeatmapLayer, before: String = definedExternally): Map /* this */
    open fun addLayer(layer: HeatmapLayer): Map /* this */
    open fun addLayer(layer: HillshadeLayer, before: String = definedExternally): Map /* this */
    open fun addLayer(layer: HillshadeLayer): Map /* this */
    open fun addLayer(layer: LineLayer, before: String = definedExternally): Map /* this */
    open fun addLayer(layer: LineLayer): Map /* this */
    open fun addLayer(layer: RasterLayer, before: String = definedExternally): Map /* this */
    open fun addLayer(layer: RasterLayer): Map /* this */
    open fun addLayer(layer: SymbolLayer, before: String = definedExternally): Map /* this */
    open fun addLayer(layer: SymbolLayer): Map /* this */
    open fun addLayer(layer: CustomLayerInterface, before: String = definedExternally): Map /* this */
    open fun addLayer(layer: CustomLayerInterface): Map /* this */
    open fun addLayer(layer: SkyLayer, before: String = definedExternally): Map /* this */
    open fun addLayer(layer: SkyLayer): Map /* this */
    open fun moveLayer(id: String, beforeId: String = definedExternally): Map /* this */
    open fun removeLayer(id: String): Map /* this */
    open fun getLayer(id: String): dynamic /* BackgroundLayer | CircleLayer | FillExtrusionLayer | FillLayer | HeatmapLayer | HillshadeLayer | LineLayer | RasterLayer | SymbolLayer | CustomLayerInterface | SkyLayer */
    open fun setFilter(layer: String, filter: Array<Any>? = definedExternally, options: FilterOptions? = definedExternally): Map /* this */
    open fun setFilter(layer: String): Map /* this */
    open fun setFilter(layer: String, filter: Array<Any>? = definedExternally): Map /* this */
    open fun setFilter(layer: String, filter: Boolean? = definedExternally, options: FilterOptions? = definedExternally): Map /* this */
    open fun setFilter(layer: String, filter: Boolean? = definedExternally): Map /* this */
    open fun setLayerZoomRange(layerId: String, minzoom: Number, maxzoom: Number): Map /* this */
    open fun getFilter(layer: String): Array<Any>
    open fun setPaintProperty(layer: String, name: String, value: dynamic, klass: String = definedExternally): Map /* this */
    open fun getPaintProperty(layer: String, name: String): Any
    open fun setLayoutProperty(layer: String, name: String, value: dynamic): Map /* this */
    open fun getLayoutProperty(layer: String, name: String): Any
    open fun setLight(options: Light, lightOptions: Any = definedExternally): Map /* this */
    open fun getLight(): Light
    open fun setTerrain(terrain: TerrainSpecification? = definedExternally): Map /* this */
    open fun getTerrain(): TerrainSpecification?
    open var showTerrainWireframe: Boolean
    open fun queryTerrainElevation(lngLat: Any /* JsTuple<Number, Number> */, options: ElevationQueryOptions = definedExternally): Number?
    open fun queryTerrainElevation(lngLat: Any /* JsTuple<Number, Number> */): Number?
    open fun queryTerrainElevation(lngLat: LngLat, options: ElevationQueryOptions = definedExternally): Number?
    open fun queryTerrainElevation(lngLat: LngLat): Number?
    open fun queryTerrainElevation(lngLat: `T$1`, options: ElevationQueryOptions = definedExternally): Number?
    open fun queryTerrainElevation(lngLat: `T$1`): Number?
    open fun queryTerrainElevation(lngLat: `T$2`, options: ElevationQueryOptions = definedExternally): Number?
    open fun queryTerrainElevation(lngLat: `T$2`): Number?
    open fun setFeatureState(feature: FeatureIdentifier, state: Json)
    open fun setFeatureState(feature: Feature, state: Json)
    open fun getFeatureState(feature: FeatureIdentifier): Json
    open fun getFeatureState(feature: Feature): Json
    open fun removeFeatureState(target: FeatureIdentifier, key: String = definedExternally)
    open fun removeFeatureState(target: FeatureIdentifier)
    open fun removeFeatureState(target: Feature, key: String = definedExternally)
    open fun removeFeatureState(target: Feature)
    open fun getContainer(): HTMLElement
    open fun getCanvasContainer(): HTMLElement
    open fun getCanvas(): HTMLCanvasElement
    open fun loaded(): Boolean
    open fun remove()
    open fun triggerRepaint()
    open var showTileBoundaries: Boolean
    open var showCollisionBoxes: Boolean
    open var showPadding: Boolean
    open var repaint: Boolean
    open fun getCenter(): LngLat
    open fun setCenter(center: Any /* JsTuple<Number, Number> */, eventData: EventData = definedExternally): Map /* this */
    open fun setCenter(center: Any /* JsTuple<Number, Number> */): Map /* this */
    open fun setCenter(center: LngLat, eventData: EventData = definedExternally): Map /* this */
    open fun setCenter(center: LngLat): Map /* this */
    open fun setCenter(center: `T$1`, eventData: EventData = definedExternally): Map /* this */
    open fun setCenter(center: `T$1`): Map /* this */
    open fun setCenter(center: `T$2`, eventData: EventData = definedExternally): Map /* this */
    open fun setCenter(center: `T$2`): Map /* this */
    open fun panBy(offset: Point, options: AnimationOptions = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun panBy(offset: Point): Map /* this */
    open fun panBy(offset: Point, options: AnimationOptions = definedExternally): Map /* this */
    open fun panBy(offset: Any /* JsTuple<Number, Number> */, options: AnimationOptions? = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun panBy(offset: Any /* JsTuple<Number, Number> */): Map /* this */
    open fun panBy(offset: Any /* JsTuple<Number, Number> */, options: AnimationOptions? = definedExternally): Map /* this */
    open fun panTo(lnglat: Any /* JsTuple<Number, Number> */, options: AnimationOptions? = definedExternally, eventdata: EventData = definedExternally): Map /* this */
    open fun panTo(lnglat: Any /* JsTuple<Number, Number> */): Map /* this */
    open fun panTo(lnglat: Any /* JsTuple<Number, Number> */, options: AnimationOptions? = definedExternally): Map /* this */
    open fun panTo(lnglat: LngLat, options: AnimationOptions? = definedExternally, eventdata: EventData = definedExternally): Map /* this */
    open fun panTo(lnglat: LngLat): Map /* this */
    open fun panTo(lnglat: LngLat, options: AnimationOptions = definedExternally): Map /* this */
    open fun panTo(lnglat: `T$1`, options: AnimationOptions = definedExternally, eventdata: EventData = definedExternally): Map /* this */
    open fun panTo(lnglat: `T$1`): Map /* this */
    open fun panTo(lnglat: `T$1`, options: AnimationOptions = definedExternally): Map /* this */
    open fun panTo(lnglat: `T$2`, options: AnimationOptions = definedExternally, eventdata: EventData = definedExternally): Map /* this */
    open fun panTo(lnglat: `T$2`): Map /* this */
    open fun panTo(lnglat: `T$2`, options: AnimationOptions = definedExternally): Map /* this */
    open fun getZoom(): Number
    open fun setZoom(zoom: Number, eventData: EventData = definedExternally): Map /* this */
    open fun zoomTo(zoom: Number, options: AnimationOptions? = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun zoomIn(options: AnimationOptions? = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun zoomOut(options: AnimationOptions? = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun getBearing(): Number
    open fun setBearing(bearing: Number, eventData: EventData = definedExternally): Map /* this */
    open fun getPadding(): PaddingOptions
    open fun setPadding(padding: PaddingOptions, eventData: EventData = definedExternally): Map /* this */
    open fun rotateTo(bearing: Number, options: AnimationOptions? = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun resetNorth(options: AnimationOptions? = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun resetNorthPitch(options: AnimationOptions? = definedExternally, eventData: EventData? = definedExternally): Map /* this */
    open fun snapToNorth(options: AnimationOptions? = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun getPitch(): Number
    open fun setPitch(pitch: Number, eventData: EventData = definedExternally): Map /* this */
    open fun cameraForBounds(bounds: LngLatBounds, options: CameraForBoundsOptions? = definedExternally): Any /* Required<Pick<CameraOptions, String /* "zoom" | "bearing" */>> & `T$20` */
    open fun cameraForBounds(bounds: LngLatBounds): Any /* Required<Pick<CameraOptions, String /* "zoom" | "bearing" */>> & `T$20` */
    open fun cameraForBounds(bounds: Any /* JsTuple<Any, Any> | JsTuple<Number, Number, Number, Number> | JsTuple<Number, Number> */, options: CameraForBoundsOptions? = definedExternally): Any /* Required<Pick<CameraOptions, String /* "zoom" | "bearing" */>> & `T$20` */
    open fun cameraForBounds(bounds: Any /* JsTuple<Any, Any> | JsTuple<Number, Number, Number, Number> | JsTuple<Number, Number> */): Any /* Required<Pick<CameraOptions, String /* "zoom" | "bearing" */>> & `T$20` */
    open fun cameraForBounds(bounds: LngLat, options: CameraForBoundsOptions? = definedExternally): Any /* Required<Pick<CameraOptions, String /* "zoom" | "bearing" */>> & `T$20` */
    open fun cameraForBounds(bounds: LngLat): Any /* Required<Pick<CameraOptions, String /* "zoom" | "bearing" */>> & `T$20` */
    open fun cameraForBounds(bounds: `T$1`, options: CameraForBoundsOptions? = definedExternally): Any /* Required<Pick<CameraOptions, String /* "zoom" | "bearing" */>> & `T$20` */
    open fun cameraForBounds(bounds: `T$1`): Any /* Required<Pick<CameraOptions, String /* "zoom" | "bearing" */>> & `T$20` */
    open fun cameraForBounds(bounds: `T$2`, options: CameraForBoundsOptions? = definedExternally): Any /* Required<Pick<CameraOptions, String /* "zoom" | "bearing" */>> & `T$20` */
    open fun cameraForBounds(bounds: `T$2`): Any /* Required<Pick<CameraOptions, String /* "zoom" | "bearing" */>> & `T$20` */
    open fun fitBounds(bounds: LngLatBounds, options: FitBoundsOptions? = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun fitBounds(bounds: LngLatBounds): Map /* this */
    open fun fitBounds(bounds: LngLatBounds, options: FitBoundsOptions? = definedExternally): Map /* this */
    open fun fitBounds(bounds: Any /* JsTuple<Any, Any> | JsTuple<Number, Number, Number, Number> | JsTuple<Number, Number> */, options: FitBoundsOptions? = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun fitBounds(bounds: Any /* JsTuple<Any, Any> | JsTuple<Number, Number, Number, Number> | JsTuple<Number, Number> */): Map /* this */
    open fun fitBounds(bounds: Any /* JsTuple<Any, Any> | JsTuple<Number, Number, Number, Number> | JsTuple<Number, Number> */, options: FitBoundsOptions? = definedExternally): Map /* this */
    open fun fitBounds(bounds: LngLat, options: FitBoundsOptions = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun fitBounds(bounds: LngLat): Map /* this */
    open fun fitBounds(bounds: LngLat, options: FitBoundsOptions? = definedExternally): Map /* this */
    open fun fitBounds(bounds: `T$1`, options: FitBoundsOptions? = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun fitBounds(bounds: `T$1`): Map /* this */
    open fun fitBounds(bounds: `T$1`, options: FitBoundsOptions? = definedExternally): Map /* this */
    open fun fitBounds(bounds: `T$2`, options: FitBoundsOptions? = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun fitBounds(bounds: `T$2`): Map /* this */
    open fun fitBounds(bounds: `T$2`, options: FitBoundsOptions? = definedExternally): Map /* this */
    open fun fitScreenCoordinates(p0: Point, p1: Point, bearing: Number, options: AnimationOptions /* AnimationOptions & CameraOptions */ = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun fitScreenCoordinates(p0: Point, p1: Point, bearing: Number): Map /* this */
    open fun fitScreenCoordinates(p0: Point, p1: Point, bearing: Number, options: AnimationOptions /* AnimationOptions & CameraOptions */ = definedExternally): Map /* this */
    open fun fitScreenCoordinates(p0: Point, p1: Any /* JsTuple<Number, Number> */, bearing: Number, options: AnimationOptions /* AnimationOptions & CameraOptions */ = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun fitScreenCoordinates(p0: Point, p1: Any /* JsTuple<Number, Number> */, bearing: Number): Map /* this */
    open fun fitScreenCoordinates(p0: Point, p1: Any /* JsTuple<Number, Number> */, bearing: Number, options: AnimationOptions /* AnimationOptions & CameraOptions */ = definedExternally): Map /* this */
    open fun fitScreenCoordinates(p0: Any /* JsTuple<Number, Number> */, p1: Point, bearing: Number, options: AnimationOptions /* AnimationOptions & CameraOptions */ = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun fitScreenCoordinates(p0: Any /* JsTuple<Number, Number> */, p1: Point, bearing: Number): Map /* this */
    open fun fitScreenCoordinates(p0: Any /* JsTuple<Number, Number> */, p1: Point, bearing: Number, options: AnimationOptions /* AnimationOptions & CameraOptions */ = definedExternally): Map /* this */
    open fun fitScreenCoordinates(p0: Any /* JsTuple<Number, Number> */, p1: Any /* JsTuple<Number, Number> */, bearing: Number, options: AnimationOptions /* AnimationOptions & CameraOptions */ = definedExternally, eventData: EventData = definedExternally): Map /* this */
    open fun fitScreenCoordinates(p0: Any /* JsTuple<Number, Number> */, p1: Any /* JsTuple<Number, Number> */, bearing: Number): Map /* this */
    open fun fitScreenCoordinates(p0: Any /* JsTuple<Number, Number> */, p1: Any /* JsTuple<Number, Number> */, bearing: Number, options: AnimationOptions /* AnimationOptions & CameraOptions */ = definedExternally): Map /* this */
    open fun jumpTo(options: CameraOptions, eventData: EventData = definedExternally): Map /* this */
    open fun getFreeCameraOptions(): FreeCameraOptions
    open fun setFreeCameraOptions(options: FreeCameraOptions, eventData: Any = definedExternally): Map /* this */
    open fun easeTo(options: EaseToOptions, eventData: EventData = definedExternally): Map /* this */
    open fun flyTo(options: FlyToOptions, eventData: EventData = definedExternally): Map /* this */
    open fun isEasing(): Boolean
    open fun stop(): Map /* this */
    open fun <T : String> on(type: T, layer: String, listener: (ev: Any) -> Unit): Map /* this */
    open fun on(type: String, layers: Array<String>, listener: (ev: Any) -> Unit): Map
    open fun <T : String> on(type: T, listener: (ev: Any) -> Unit): Map /* this */
    open fun on(type: String, listener: (ev: Any) -> Unit): Map /* this */
    open fun <T : String> once(type: T, layer: String, listener: (ev: Any) -> Unit): Map /* this */
    open fun <T : String> once(type: T, listener: (ev: Any) -> Unit): Map /* this */
    open fun once(type: String, listener: (ev: Any) -> Unit): Map /* this */
    open fun <T : String> once(type: T): Promise<Any>
    open fun <T : String> off(type: T, layer: String, listener: (ev: Any) -> Unit): Map /* this */
    open fun <T : String> off(type: T, listener: (ev: Any) -> Unit): Map /* this */
    open fun off(type: String, listener: (ev: Any) -> Unit): Map /* this */
    open var scrollZoom: ScrollZoomHandler
    open var boxZoom: BoxZoomHandler
    open var dragRotate: DragRotateHandler
    open var dragPan: DragPanHandler
    open var keyboard: KeyboardHandler
    open var doubleClickZoom: DoubleClickZoomHandler
    open var touchZoomRotate: TouchZoomRotateHandler
    open var touchPitch: TouchPitchHandler
    open fun getFog(): Fog?
    open fun setFog(fog: Fog): Map /* this */
}

external interface `T$9` {
    @nativeGetter
    operator fun get(key: String): String?
    @nativeSetter
    operator fun set(key: String, value: String)
}

external interface MapboxOptions {
    var antialias: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var attributionControl: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var bearing: Number?
        get() = definedExternally
        set(value) = definedExternally
    var bearingSnap: Number?
        get() = definedExternally
        set(value) = definedExternally
    var bounds: dynamic /* LngLatBounds? | JsTuple<dynamic, dynamic> | JsTuple<Number, Number, Number, Number> | JsTuple<Number, Number> | LngLat? | `T$1`? | `T$2`? */
        get() = definedExternally
        set(value) = definedExternally
    var boxZoom: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var center: dynamic /* JsTuple<Number, Number> | LngLat? | `T$1`? | `T$2`? */
        get() = definedExternally
        set(value) = definedExternally
    var clickTolerance: Number?
        get() = definedExternally
        set(value) = definedExternally
    var collectResourceTiming: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var crossSourceCollisions: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var container: dynamic /* String | HTMLElement */
        get() = definedExternally
        set(value) = definedExternally
    var cooperativeGestures: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var customAttribution: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var dragPan: dynamic /* Boolean? | DragPanOptions? */
        get() = definedExternally
        set(value) = definedExternally
    var dragRotate: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var doubleClickZoom: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var hash: dynamic /* Boolean? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var fadeDuration: Number?
        get() = definedExternally
        set(value) = definedExternally
    var failIfMajorPerformanceCaveat: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var fitBoundsOptions: FitBoundsOptions?
        get() = definedExternally
        set(value) = definedExternally
    var interactive: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var keyboard: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var locale: `T$9`?
        get() = definedExternally
        set(value) = definedExternally
    var localFontFamily: String?
        get() = definedExternally
        set(value) = definedExternally
    var localIdeographFontFamily: String?
        get() = definedExternally
        set(value) = definedExternally
    var logoPosition: String? /* "top-left" | "top-right" | "bottom-left" | "bottom-right" */
        get() = definedExternally
        set(value) = definedExternally
    var maxBounds: dynamic /* LngLatBounds? | JsTuple<dynamic, dynamic> | JsTuple<Number, Number, Number, Number> | JsTuple<Number, Number> | LngLat? | `T$1`? | `T$2`? */
        get() = definedExternally
        set(value) = definedExternally
    var maxPitch: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maxZoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var minPitch: Number?
        get() = definedExternally
        set(value) = definedExternally
    var minZoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var optimizeForTerrain: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var preserveDrawingBuffer: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var pitch: Number?
        get() = definedExternally
        set(value) = definedExternally
    var pitchWithRotate: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var projection: String?
        get() = definedExternally
        set(value) = definedExternally
    var refreshExpiredTiles: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var renderWorldCopies: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var scrollZoom: dynamic /* Boolean? | InteractiveOptions? */
        get() = definedExternally
        set(value) = definedExternally
    var style: dynamic /* mapboxgl.Style? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var trackResize: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var transformRequest: TransformRequestFunction?
        get() = definedExternally
        set(value) = definedExternally
    var touchZoomRotate: dynamic /* Boolean? | InteractiveOptions? */
        get() = definedExternally
        set(value) = definedExternally
    var touchPitch: dynamic /* Boolean? | InteractiveOptions? */
        get() = definedExternally
        set(value) = definedExternally
    var zoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maxTileCacheSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var accessToken: String?
        get() = definedExternally
        set(value) = definedExternally
    var testMode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external open class FreeCameraOptions(position: MercatorCoordinate = definedExternally, orientation: quat = definedExternally) {
    open var position: MercatorCoordinate?
    open fun lookAtPoint(location: Any /* JsTuple<Number, Number> */, up: vec3 = definedExternally)
    open fun lookAtPoint(location: Any /* JsTuple<Number, Number> */)
    open fun lookAtPoint(location: LngLat, up: vec3 = definedExternally)
    open fun lookAtPoint(location: LngLat)
    open fun lookAtPoint(location: `T$1`, up: vec3 = definedExternally)
    open fun lookAtPoint(location: `T$1`)
    open fun lookAtPoint(location: `T$2`, up: vec3 = definedExternally)
    open fun lookAtPoint(location: `T$2`)
    open fun setPitchBearing(pitch: Number, bearing: Number)
}

external interface RequestParameters {
    var url: String
    var credentials: String? /* "same-origin" | "include" */
        get() = definedExternally
        set(value) = definedExternally
    var headers: Json?
        get() = definedExternally
        set(value) = definedExternally
    var method: String? /* "GET" | "POST" | "PUT" */
        get() = definedExternally
        set(value) = definedExternally
    var collectResourceTiming: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PaddingOptions {
    var top: Number
    var bottom: Number
    var left: Number
    var right: Number
}

external interface FeatureIdentifier {
    var id: dynamic /* String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var source: String
    var sourceLayer: String?
        get() = definedExternally
        set(value) = definedExternally
}

external open class BoxZoomHandler(map: Map) {
    open fun isEnabled(): Boolean
    open fun isActive(): Boolean
    open fun enable()
    open fun disable()
}

external open class ScrollZoomHandler(map: Map) {
    open fun isEnabled(): Boolean
    open fun enable(options: InteractiveOptions = definedExternally)
    open fun disable()
    open fun setZoomRate(zoomRate: Number)
    open fun setWheelZoomRate(wheelZoomRate: Number)
}

external open class DragPanHandler(map: Map) {
    open fun isEnabled(): Boolean
    open fun isActive(): Boolean
    open fun enable(options: DragPanOptions = definedExternally)
    open fun disable()
}

external interface `T$10` {
    var bearingSnap: Number?
        get() = definedExternally
        set(value) = definedExternally
    var pitchWithRotate: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external open class DragRotateHandler(map: Map, options: `T$10` = definedExternally) {
    open fun isEnabled(): Boolean
    open fun isActive(): Boolean
    open fun enable()
    open fun disable()
}

external open class KeyboardHandler(map: Map) {
    open fun isEnabled(): Boolean
    open fun enable()
    open fun disable()
    open fun isActive(): Boolean
    open fun disableRotation()
    open fun enableRotation()
}

external open class DoubleClickZoomHandler(map: Map) {
    open fun isEnabled(): Boolean
    open fun enable()
    open fun disable()
}

external open class TouchZoomRotateHandler(map: Map) {
    open fun isEnabled(): Boolean
    open fun enable(options: InteractiveOptions = definedExternally)
    open fun disable()
    open fun disableRotation()
    open fun enableRotation()
}

external open class TouchPitchHandler(map: Map) {
    open fun enable(options: InteractiveOptions = definedExternally)
    open fun isActive(): Boolean
    open fun isEnabled(): Boolean
    open fun disable()
}

external interface IControl {
    fun onAdd(map: Map): HTMLElement
    fun onRemove(map: Map)
    var getDefaultPosition: (() -> String)?
        get() = definedExternally
        set(value) = definedExternally
}

external open class Control : Evented, IControl {
    override fun onAdd(map: Map): HTMLElement
    override fun onRemove(map: Map)
    override var getDefaultPosition: (() -> String)?
}

external interface `T$11` {
    var showCompass: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showZoom: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var visualizePitch: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external open class NavigationControl(options: `T$11` = definedExternally) : Control

external open class PositionOptions {
    open var enableHighAccuracy: Boolean?
    open var timeout: Number?
    open var maximumAge: Number?
}

external interface `T$12` {
    var positionOptions: PositionOptions?
        get() = definedExternally
        set(value) = definedExternally
    var fitBoundsOptions: FitBoundsOptions?
        get() = definedExternally
        set(value) = definedExternally
    var trackUserLocation: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showAccuracyCircle: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showUserLocation: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showUserHeading: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external open class GeolocateControl(options: `T$12` = definedExternally) : Control {
    open fun trigger(): Boolean
}

external interface `T$13` {
    var compact: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var customAttribution: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
}

external open class AttributionControl(options: `T$13` = definedExternally) : Control

external interface `T$14` {
    var maxWidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var unit: String?
        get() = definedExternally
        set(value) = definedExternally
}

external open class ScaleControl(options: `T$14` = definedExternally) : Control {
    open fun setUnit(unit: String /* "imperial" | "metric" | "nautical" */)
}

external open class FullscreenControl(options: FullscreenControlOptions? = definedExternally) : Control

external interface FullscreenControlOptions {
    var container: HTMLElement?
        get() = definedExternally
        set(value) = definedExternally
}

external open class Popup(options: PopupOptions = definedExternally) : Evented {
    open fun addTo(map: Map): Popup /* this */
    open fun isOpen(): Boolean
    open fun remove(): Popup /* this */
    open fun getLngLat(): LngLat
    open fun setLngLat(lnglat: Any /* JsTuple<Number, Number> */): Popup /* this */
    open fun setLngLat(lnglat: LngLat): Popup /* this */
    open fun setLngLat(lnglat: `T$1`): Popup /* this */
    open fun setLngLat(lnglat: `T$2`): Popup /* this */
    open fun trackPointer(): Popup /* this */
    open fun getElement(): HTMLElement
    open fun setText(text: String): Popup /* this */
    open fun setHTML(html: String): Popup /* this */
    open fun setDOMContent(htmlNode: Node): Popup /* this */
    open fun getMaxWidth(): String
    open fun setMaxWidth(maxWidth: String): Popup /* this */
    open fun addClassName(className: String)
    open fun removeClassName(className: String)
    open fun setOffset(offset: Number? = definedExternally): Popup /* this */
    open fun setOffset(): Popup /* this */
    open fun setOffset(offset: Point? = definedExternally): Popup /* this */
    open fun setOffset(offset: Any /* JsTuple<Number, Number> */ = definedExternally): Popup /* this */
    open fun setOffset(offset: `T$3`? = definedExternally): Popup /* this */
    open fun toggleClassName(className: String)
}

external interface PopupOptions {
    var closeButton: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var closeOnClick: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var closeOnMove: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var focusAfterOpen: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var anchor: String? /* "center" | "left" | "right" | "top" | "bottom" | "top-left" | "top-right" | "bottom-left" | "bottom-right" */
        get() = definedExternally
        set(value) = definedExternally
    var offset: dynamic /* Number? | Point? | JsTuple<Number, Number> | `T$3`? */
        get() = definedExternally
        set(value) = definedExternally
    var className: String?
        get() = definedExternally
        set(value) = definedExternally
    var maxWidth: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Style {
    var bearing: Number?
        get() = definedExternally
        set(value) = definedExternally
    var center: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var fog: Fog?
        get() = definedExternally
        set(value) = definedExternally
    var glyphs: String?
        get() = definedExternally
        set(value) = definedExternally
    var layers: Array<dynamic /* BackgroundLayer | CircleLayer | FillExtrusionLayer | FillLayer | HeatmapLayer | HillshadeLayer | LineLayer | RasterLayer | SymbolLayer | CustomLayerInterface | SkyLayer */>?
        get() = definedExternally
        set(value) = definedExternally
    var metadata: Any?
        get() = definedExternally
        set(value) = definedExternally
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var pitch: Number?
        get() = definedExternally
        set(value) = definedExternally
    var light: Light?
        get() = definedExternally
        set(value) = definedExternally
    var sources: Sources?
        get() = definedExternally
        set(value) = definedExternally
    var sprite: String?
        get() = definedExternally
        set(value) = definedExternally
    var terrain: TerrainSpecification?
        get() = definedExternally
        set(value) = definedExternally
    var transition: Transition?
        get() = definedExternally
        set(value) = definedExternally
    var version: Number
    var zoom: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Transition {
    var delay: Number?
        get() = definedExternally
        set(value) = definedExternally
    var duration: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Light {
    @nativeGetter
    operator fun get(key: String): Transition?
    @nativeSetter
    operator fun set(key: String, value: Transition?)
    var anchor: String? /* "map" | "viewport" */
        get() = definedExternally
        set(value) = definedExternally
    var position: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var color: String?
        get() = definedExternally
        set(value) = definedExternally
    var intensity: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Fog {
    @nativeGetter
    operator fun get(key: String): dynamic? /* Number? | JsTuple<String, Any> */
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Number? | JsTuple<String, Any> */)
    var color: dynamic /* String? | JsTuple<String, Any> */
        get() = definedExternally
        set(value) = definedExternally
    var range: dynamic /* Array<Number>? | JsTuple<String, Any> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface Sources {
    @nativeGetter
    operator fun get(sourceName: String): dynamic /* GeoJSONSourceRaw? | VideoSourceRaw? | ImageSourceRaw? | CanvasSourceRaw? | VectorSource? | RasterSource? | RasterDemSource? */
    @nativeSetter
    operator fun set(sourceName: String, value: GeoJSONSourceRaw)
    @nativeSetter
    operator fun set(sourceName: String, value: VideoSourceRaw)
    @nativeSetter
    operator fun set(sourceName: String, value: ImageSourceRaw)
    @nativeSetter
    operator fun set(sourceName: String, value: CanvasSourceRaw)
    @nativeSetter
    operator fun set(sourceName: String, value: VectorSource)
    @nativeSetter
    operator fun set(sourceName: String, value: RasterSource)
    @nativeSetter
    operator fun set(sourceName: String, value: RasterDemSource)
}

external interface VectorSourceImpl : VectorSource {
    fun setTiles(tiles: Array<String>): VectorSourceImpl
    fun setUrl(url: String): VectorSourceImpl
}

external interface Source {
    var type: String /* "vector" | "raster" | "raster-dem" | "geojson" | "image" | "video" | "canvas" */
}

external interface GeoJSONSourceRaw : Source, GeoJSONSourceOptions {
    override var type: String /* "geojson" */
}

external open class GeoJSONSource(options: GeoJSONSourceOptions = definedExternally) : GeoJSONSourceRaw {
    override var type: String /* "geojson" */
    open fun setData(data: Feature): GeoJSONSource /* this */
    open fun setData(data: FeatureCollection): GeoJSONSource /* this */
    open fun setData(data: String): GeoJSONSource /* this */
    open fun getClusterExpansionZoom(clusterId: Number, callback: (error: Any, zoom: Number) -> Unit): GeoJSONSource /* this */
    open fun getClusterChildren(clusterId: Number, callback: (error: Any, features: Array<Feature>) -> Unit): GeoJSONSource /* this */
    open fun getClusterLeaves(clusterId: Number, limit: Number, offset: Number, callback: (error: Any, features: Array<Feature>) -> Unit): GeoJSONSource /* this */
}

external interface GeoJSONSourceOptions {
    var data: dynamic /* Feature__1<dynamic /* Point | MultiPoint | LineString | MultiLineString | Polygon | MultiPolygon | GeometryCollection */>? | FeatureCollection__1<dynamic /* Point | MultiPoint | LineString | MultiLineString | Polygon | MultiPolygon | GeometryCollection */>? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var maxzoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var attribution: String?
        get() = definedExternally
        set(value) = definedExternally
    var buffer: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tolerance: Number?
        get() = definedExternally
        set(value) = definedExternally
    var cluster: dynamic /* Number? | Boolean? */
        get() = definedExternally
        set(value) = definedExternally
    var clusterRadius: Number?
        get() = definedExternally
        set(value) = definedExternally
    var clusterMaxZoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var clusterMinPoints: Number?
        get() = definedExternally
        set(value) = definedExternally
    var clusterProperties: Any?
        get() = definedExternally
        set(value) = definedExternally
    var lineMetrics: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var generateId: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var promoteId: dynamic /* `T$9`? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var filter: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface VideoSourceRaw : Source, VideoSourceOptions {
    override var type: String /* "video" */
}

external open class VideoSource(options: VideoSourceOptions = definedExternally) : VideoSourceRaw {
    override var type: String /* "video" */
    open fun getVideo(): HTMLVideoElement
    open fun setCoordinates(coordinates: Array<Array<Number>>): VideoSource /* this */
}

external interface VideoSourceOptions {
    var urls: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var coordinates: Array<Array<Number>>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ImageSourceRaw : Source, ImageSourceOptions {
    override var type: String /* "image" */
}

external open class ImageSource(options: ImageSourceOptions = definedExternally) : ImageSourceRaw {
    override var type: String /* "image" */
    open fun updateImage(options: ImageSourceOptions): ImageSource /* this */
    open fun setCoordinates(coordinates: Array<Array<Number>>): ImageSource /* this */
}

external interface ImageSourceOptions {
    var url: String?
        get() = definedExternally
        set(value) = definedExternally
    var coordinates: Array<Array<Number>>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CanvasSourceRaw : Source, CanvasSourceOptions {
    override var type: String /* "canvas" */
}

external open class CanvasSource : CanvasSourceRaw {
    override var type: String /* "canvas" */
    override var coordinates: Array<Array<Number>>
    override var canvas: dynamic /* String | HTMLCanvasElement */
    open fun play()
    open fun pause()
    open fun getCanvas(): HTMLCanvasElement
    open fun setCoordinates(coordinates: Array<Array<Number>>): CanvasSource /* this */
}

external interface CanvasSourceOptions {
    var coordinates: Array<Array<Number>>
    var animate: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var canvas: dynamic /* String | HTMLCanvasElement */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$15` {
    var type: String /* "exponential" */
    var stops: Array<dynamic /* JsTuple<Number, T> */>
}

external interface `T$16` {
    var type: String /* "interval" */
    var stops: Array<dynamic /* JsTuple<Number, T> */>
}

external interface TerrainSpecification {
    var source: String
    var exaggeration: dynamic /* Number? | `T$15`? | `T$16`? | ExpressionSpecification? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface VectorSource : Source {
    override var type: String /* "vector" */
    var url: String?
        get() = definedExternally
        set(value) = definedExternally
    var tiles: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var bounds: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var scheme: String? /* "xyz" | "tms" */
        get() = definedExternally
        set(value) = definedExternally
    var minzoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maxzoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var attribution: String?
        get() = definedExternally
        set(value) = definedExternally
    var promoteId: dynamic /* `T$9`? | String? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface RasterSource : Source {
    override var type: String /* "raster" */
    var url: String?
        get() = definedExternally
        set(value) = definedExternally
    var tiles: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var bounds: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var minzoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maxzoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tileSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var scheme: String? /* "xyz" | "tms" */
        get() = definedExternally
        set(value) = definedExternally
    var attribution: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface RasterDemSource : Source {
    override var type: String /* "raster-dem" */
    var url: String?
        get() = definedExternally
        set(value) = definedExternally
    var tiles: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var bounds: Array<Number>?
        get() = definedExternally
        set(value) = definedExternally
    var minzoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maxzoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tileSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var attribution: String?
        get() = definedExternally
        set(value) = definedExternally
    var encoding: String? /* "terrarium" | "mapbox" */
        get() = definedExternally
        set(value) = definedExternally
}

external open class LngLat(lng: Number, lat: Number) {
    open var lng: Number
    open var lat: Number
    open fun wrap(): LngLat
    open fun toArray(): Array<Number>
    override fun toString(): String
    open fun distanceTo(lngLat: LngLat): Number
    open fun toBounds(radius: Number): LngLatBounds

    companion object {
        fun convert(input: Any /* JsTuple<Number, Number> */): LngLat
        fun convert(input: LngLat): LngLat
        fun convert(input: `T$1`): LngLat
        fun convert(input: `T$2`): LngLat
    }
}

external open class LngLatBounds {
    open var sw: dynamic /* JsTuple<Number, Number> | LngLat | `T$1` | `T$2` */
    open var ne: dynamic /* JsTuple<Number, Number> | LngLat | `T$1` | `T$2` */
    constructor(boundsLike: Any = definedExternally)
    constructor()
    constructor(sw: Any, ne: Any)
    constructor(sw: LngLat, ne: Any)
    constructor(sw: `T$1`, ne: Any)
    constructor(sw: `T$2`, ne: Any)
    open fun setNorthEast(ne: Any /* JsTuple<Number, Number> */): LngLatBounds /* this */
    open fun setNorthEast(ne: LngLat): LngLatBounds /* this */
    open fun setNorthEast(ne: `T$1`): LngLatBounds /* this */
    open fun setNorthEast(ne: `T$2`): LngLatBounds /* this */
    open fun setSouthWest(sw: Any /* JsTuple<Number, Number> */): LngLatBounds /* this */
    open fun setSouthWest(sw: LngLat): LngLatBounds /* this */
    open fun setSouthWest(sw: `T$1`): LngLatBounds /* this */
    open fun setSouthWest(sw: `T$2`): LngLatBounds /* this */
    open fun contains(lnglat: Any /* JsTuple<Number, Number> */): Boolean
    open fun contains(lnglat: LngLat): Boolean
    open fun contains(lnglat: `T$1`): Boolean
    open fun contains(lnglat: `T$2`): Boolean
    open fun extend(obj: Any /* JsTuple<Number, Number> | JsTuple<Any, Any> | JsTuple<Number, Number, Number, Number> */): LngLatBounds /* this */
    open fun extend(obj: LngLat): LngLatBounds /* this */
    open fun extend(obj: `T$1`): LngLatBounds /* this */
    open fun extend(obj: `T$2`): LngLatBounds /* this */
    open fun extend(obj: LngLatBounds): LngLatBounds /* this */
    open fun getCenter(): LngLat
    open fun getSouthWest(): LngLat
    open fun getNorthEast(): LngLat
    open fun getNorthWest(): LngLat
    open fun getSouthEast(): LngLat
    open fun getWest(): Number
    open fun getSouth(): Number
    open fun getEast(): Number
    open fun getNorth(): Number
    open fun toArray(): Array<Array<Number>>
    override fun toString(): String
    open fun isEmpty(): Boolean

    companion object {
        fun convert(input: LngLatBounds): LngLatBounds
        fun convert(input: Any /* JsTuple<Any, Any> */): LngLatBounds
        fun convert(input: LngLat): LngLatBounds
        fun convert(input: `T$1`): LngLatBounds
        fun convert(input: `T$2`): LngLatBounds
    }
}

external open class Point(x: Number, y: Number) {
    open var x: Number
    open var y: Number
    open fun clone(): Point
    open fun add(p: Point): Point
    open fun sub(p: Point): Point
    open fun mult(k: Number): Point
    open fun div(k: Number): Point
    open fun rotate(a: Number): Point
    open fun matMult(m: Number): Point
    open fun unit(): Point
    open fun perp(): Point
    open fun round(): Point
    open fun mag(): Number
    open fun equals(p: Point): Boolean
    open fun dist(p: Point): Number
    open fun distSqr(p: Point): Number
    open fun angle(): Number
    open fun angleTo(p: Point): Number
    open fun angleWidth(p: Point): Number
    open fun angleWithSep(x: Number, y: Number): Number

    companion object {
        fun convert(a: Point): Point
        fun convert(a: Any /* JsTuple<Number, Number> */): Point
    }
}

external open class MercatorCoordinate(x: Number, y: Number, z: Number = definedExternally) {
    open var x: Number
    open var y: Number
    open var z: Number?
    open fun toAltitude(): Number
    open fun toLngLat(): LngLat
    open fun meterInMercatorCoordinateUnits(): Number

    companion object {
        fun fromLngLat(lngLatLike: Any /* JsTuple<Number, Number> */, altitude: Number = definedExternally): MercatorCoordinate
        fun fromLngLat(lngLatLike: LngLat, altitude: Number = definedExternally): MercatorCoordinate
        fun fromLngLat(lngLatLike: `T$1`, altitude: Number = definedExternally): MercatorCoordinate
        fun fromLngLat(lngLatLike: `T$2`, altitude: Number = definedExternally): MercatorCoordinate
    }
}

external open class Marker : Evented {
    constructor(options: MarkerOptions = definedExternally)
    constructor()
    constructor(element: HTMLElement = definedExternally, options: MarkerOptions = definedExternally)
    constructor(element: HTMLElement = definedExternally)
    open fun addTo(map: Map): Marker /* this */
    open fun remove(): Marker /* this */
    open fun getLngLat(): LngLat
    open fun setLngLat(lngLat: Any /* JsTuple<Number, Number> */): Marker /* this */
    open fun setLngLat(lngLat: LngLat): Marker /* this */
    open fun setLngLat(lngLat: `T$1`): Marker /* this */
    open fun setLngLat(lngLat: `T$2`): Marker /* this */
    open fun getElement(): HTMLElement
    open fun setPopup(popup: Popup = definedExternally): Marker /* this */
    open fun getPopup(): Popup
    open fun togglePopup(): Marker /* this */
    open fun getOffset(): dynamic /* Point | JsTuple<Number, Number> */
    open fun setOffset(offset: Point): Marker /* this */
    open fun setOffset(offset: Any /* JsTuple<Number, Number> */): Marker /* this */
    open fun setDraggable(shouldBeDraggable: Boolean): Marker /* this */
    open fun isDraggable(): Boolean
    open fun getRotation(): Number
    open fun setRotation(rotation: Number): Marker /* this */
    open fun getRotationAlignment(): String /* "map" | "viewport" | "auto" */
    open fun setRotationAlignment(alignment: String /* "map" | "viewport" | "auto" */): Marker /* this */
    open fun getPitchAlignment(): String /* "map" | "viewport" | "auto" */
    open fun setPitchAlignment(alignment: String /* "map" | "viewport" | "auto" */): Marker /* this */
}

external interface MarkerOptions {
    var element: HTMLElement?
        get() = definedExternally
        set(value) = definedExternally
    var offset: dynamic /* Point? | JsTuple<Number, Number> */
        get() = definedExternally
        set(value) = definedExternally
    var anchor: String? /* "center" | "left" | "right" | "top" | "bottom" | "top-left" | "top-right" | "bottom-left" | "bottom-right" */
        get() = definedExternally
        set(value) = definedExternally
    var color: String?
        get() = definedExternally
        set(value) = definedExternally
    var draggable: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clickTolerance: Number?
        get() = definedExternally
        set(value) = definedExternally
    var rotation: Number?
        get() = definedExternally
        set(value) = definedExternally
    var rotationAlignment: String? /* "map" | "viewport" | "auto" */
        get() = definedExternally
        set(value) = definedExternally
    var pitchAlignment: String? /* "map" | "viewport" | "auto" */
        get() = definedExternally
        set(value) = definedExternally
    var scale: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external open class Evented {
    open fun on(type: String, listener: EventedListener): Evented /* this */
    open fun off(type: String = definedExternally, listener: EventedListener = definedExternally): Evented /* this */
    open fun off(): Evented /* this */
    open fun off(type: String = definedExternally): Evented /* this */
    open fun off(type: Any = definedExternally, listener: EventedListener = definedExternally): Evented /* this */
    open fun off(type: Any = definedExternally): Evented /* this */
    open fun once(type: String, listener: EventedListener): Evented /* this */
    open fun fire(type: String, properties: Json = definedExternally): Evented /* this */
}

external interface StyleOptions {
    var transition: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$17` {
    var layer: Layer
    var source: String
    var sourceLayer: String
    var state: Json
}

external interface EventData {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: Any)
}

external open class MapboxEvent<TOrig> {
    open var type: String
    open var target: Map
    open var originalEvent: TOrig
}

external open class MapboxEvent__0 : MapboxEvent<Nothing?>

external open class MapMouseEvent : MapboxEvent<MouseEvent> {
    override var type: String /* "mousedown" | "mouseup" | "click" | "dblclick" | "mousemove" | "mouseover" | "mouseenter" | "mouseleave" | "mouseout" | "contextmenu" */
    open var point: Point
    open var lngLat: LngLat
    open fun preventDefault()
    open var defaultPrevented: Boolean
}

external interface `T$18` {
    var features: Array<Feature>?
        get() = definedExternally
        set(value) = definedExternally
}

external open class MapTouchEvent : MapboxEvent<TouchEvent> {
    override var type: String /* "touchstart" | "touchend" | "touchcancel" */
    open var point: Point
    open var lngLat: LngLat
    open var points: Array<Point>
    open var lngLats: Array<LngLat>
    open fun preventDefault()
    open var defaultPrevented: Boolean
}

external interface `T$19` {
    var features: Array<Feature>?
        get() = definedExternally
        set(value) = definedExternally
}

external open class MapWheelEvent : MapboxEvent<WheelEvent> {
    override var type: String /* "wheel" */
    open fun preventDefault()
    open var defaultPrevented: Boolean
}

external interface MapBoxZoomEvent : MapboxEvent<MouseEvent> {
    override var type: String /* "boxzoomstart" | "boxzoomend" | "boxzoomcancel" */
    var boxZoomBounds: LngLatBounds
}

external interface MapStyleDataEvent : MapboxEvent__0 {
    var dataType: String /* "style" */
}

external interface MapSourceDataEvent : MapboxEvent__0 {
    var dataType: String /* "source" */
    var isSourceLoaded: Boolean
    var source: Source
    var sourceId: String
    var sourceDataType: String /* "metadata" | "content" */
    var tile: Any
    var coord: Coordinate
}

external interface Coordinate {
    var canonical: CanonicalCoordinate
    var wrap: Number
    var key: Number
}

external interface CanonicalCoordinate {
    var x: Number
    var y: Number
    var z: Number
    var key: Number
    fun equals(coord: CanonicalCoordinate): Boolean
}

external interface MapContextEvent : MapboxEvent<WebGLContextEvent> {
    override var type: String /* "webglcontextlost" | "webglcontextrestored" */
}

external open class ErrorEvent : MapboxEvent__0 {
    override var type: String /* "error" */
    open var error: Error
}

external interface FilterOptions {
    var validate: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface AnimationOptions {
    var duration: Number?
        get() = definedExternally
        set(value) = definedExternally
    var easing: ((time: Number) -> Number)?
        get() = definedExternally
        set(value) = definedExternally
    var offset: dynamic /* Point? | JsTuple<Number, Number> */
        get() = definedExternally
        set(value) = definedExternally
    var animate: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var essential: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CameraOptions {
    var center: dynamic /* JsTuple<Number, Number> | LngLat? | `T$1`? | `T$2`? */
        get() = definedExternally
        set(value) = definedExternally
    var zoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var bearing: Number?
        get() = definedExternally
        set(value) = definedExternally
    var pitch: Number?
        get() = definedExternally
        set(value) = definedExternally
    var around: dynamic /* JsTuple<Number, Number> | LngLat? | `T$1`? | `T$2`? */
        get() = definedExternally
        set(value) = definedExternally
    var padding: dynamic /* Number? | PaddingOptions? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface CameraForBoundsOptions : CameraOptions {
    var offset: dynamic /* Point? | JsTuple<Number, Number> */
        get() = definedExternally
        set(value) = definedExternally
    var maxZoom: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$20` {
    var center: `T$1`
}

external interface FlyToOptions : AnimationOptions, CameraOptions {
    var curve: Number?
        get() = definedExternally
        set(value) = definedExternally
    var minZoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var speed: Number?
        get() = definedExternally
        set(value) = definedExternally
    var screenSpeed: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maxDuration: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface EaseToOptions : AnimationOptions, CameraOptions {
    var delayEndEvents: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface FitBoundsOptions : FlyToOptions {
    var linear: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    override var offset: dynamic /* Point? | JsTuple<Number, Number> */
        get() = definedExternally
        set(value) = definedExternally
    var maxZoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    override var maxDuration: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface MapEventType {
    var error: ErrorEvent
    var load: MapboxEvent__0
    var idle: MapboxEvent__0
    var remove: MapboxEvent__0
    var render: MapboxEvent__0
    var resize: MapboxEvent__0
    var webglcontextlost: MapContextEvent
    var webglcontextrestored: MapContextEvent
    var dataloading: dynamic /* MapSourceDataEvent | MapStyleDataEvent */
        get() = definedExternally
        set(value) = definedExternally
    var data: dynamic /* MapSourceDataEvent | MapStyleDataEvent */
        get() = definedExternally
        set(value) = definedExternally
    var tiledataloading: dynamic /* MapSourceDataEvent | MapStyleDataEvent */
        get() = definedExternally
        set(value) = definedExternally
    var sourcedataloading: MapSourceDataEvent
    var styledataloading: MapStyleDataEvent
    var sourcedata: MapSourceDataEvent
    var styledata: MapStyleDataEvent
    var boxzoomcancel: MapBoxZoomEvent
    var boxzoomstart: MapBoxZoomEvent
    var boxzoomend: MapBoxZoomEvent
    var touchcancel: MapTouchEvent
    var touchmove: MapTouchEvent
    var touchend: MapTouchEvent
    var touchstart: MapTouchEvent
    var click: MapMouseEvent
    var contextmenu: MapMouseEvent
    var dblclick: MapMouseEvent
    var mousemove: MapMouseEvent
    var mouseup: MapMouseEvent
    var mousedown: MapMouseEvent
    var mouseout: MapMouseEvent
    var mouseover: MapMouseEvent
    var movestart: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? | WheelEvent? */>
    var move: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? | WheelEvent? */>
    var moveend: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? | WheelEvent? */>
    var zoomstart: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? | WheelEvent? */>
    var zoom: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? | WheelEvent? */>
    var zoomend: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? | WheelEvent? */>
    var rotatestart: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? */>
    var rotate: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? */>
    var rotateend: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? */>
    var dragstart: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? */>
    var drag: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? */>
    var dragend: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? */>
    var pitchstart: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? */>
    var pitch: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? */>
    var pitchend: MapboxEvent<dynamic /* MouseEvent? | TouchEvent? */>
    var wheel: MapWheelEvent
}

external interface MapLayerEventType {
    var click: MapMouseEvent /* MapMouseEvent & `T$18` */
    var dblclick: MapMouseEvent /* MapMouseEvent & `T$18` */
    var mousedown: MapMouseEvent /* MapMouseEvent & `T$18` */
    var mouseup: MapMouseEvent /* MapMouseEvent & `T$18` */
    var mousemove: MapMouseEvent /* MapMouseEvent & `T$18` */
    var mouseenter: MapMouseEvent /* MapMouseEvent & `T$18` */
    var mouseleave: MapMouseEvent /* MapMouseEvent & `T$18` */
    var mouseover: MapMouseEvent /* MapMouseEvent & `T$18` */
    var mouseout: MapMouseEvent /* MapMouseEvent & `T$18` */
    var contextmenu: MapMouseEvent /* MapMouseEvent & `T$18` */
    var touchstart: MapTouchEvent /* MapTouchEvent & `T$19` */
    var touchend: MapTouchEvent /* MapTouchEvent & `T$19` */
    var touchcancel: MapTouchEvent /* MapTouchEvent & `T$19` */
}

external interface Layer {
    @nativeGetter
    operator fun get(key: String): String?
    @nativeSetter
    operator fun set(key: String, value: String?)
    var id: String
    var type: String
    var metadata: Any?
        get() = definedExternally
        set(value) = definedExternally
    var ref: String?
        get() = definedExternally
        set(value) = definedExternally
    var source: dynamic /* String? | GeoJSONSourceRaw? | VideoSourceRaw? | ImageSourceRaw? | CanvasSourceRaw? | VectorSource? | RasterSource? | RasterDemSource? */
        get() = definedExternally
        set(value) = definedExternally
    var minzoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var maxzoom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var interactive: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var filter: Array<Any>?
        get() = definedExternally
        set(value) = definedExternally
    var layout: dynamic /* BackgroundLayout? | FillLayout? | FillExtrusionLayout? | LineLayout? | SymbolLayout? | RasterLayout? | CircleLayout? | HeatmapLayout? | HillshadeLayout? | SkyLayout? */
        get() = definedExternally
        set(value) = definedExternally
    var paint: dynamic /* BackgroundPaint? | FillPaint? | FillExtrusionPaint? | LinePaint? | SymbolPaint? | RasterPaint? | CirclePaint? | HeatmapPaint? | HillshadePaint? | SkyPaint? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface BackgroundLayer : Layer {
    override var type: String /* "background" */
    override var layout: BackgroundLayout?
        get() = definedExternally
        set(value) = definedExternally
    override var paint: BackgroundPaint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CircleLayer : Layer {
    override var type: String /* "circle" */
    override var layout: CircleLayout?
        get() = definedExternally
        set(value) = definedExternally
    override var paint: CirclePaint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface FillExtrusionLayer : Layer {
    override var type: String /* "fill-extrusion" */
    override var layout: FillExtrusionLayout?
        get() = definedExternally
        set(value) = definedExternally
    override var paint: FillExtrusionPaint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface FillLayer : Layer {
    override var type: String /* "fill" */
    override var layout: FillLayout?
        get() = definedExternally
        set(value) = definedExternally
    override var paint: FillPaint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface HeatmapLayer : Layer {
    override var type: String /* "heatmap" */
    override var layout: HeatmapLayout?
        get() = definedExternally
        set(value) = definedExternally
    override var paint: HeatmapPaint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface HillshadeLayer : Layer {
    override var type: String /* "hillshade" */
    override var layout: HillshadeLayout?
        get() = definedExternally
        set(value) = definedExternally
    override var paint: HillshadePaint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LineLayer : Layer {
    override var type: String /* "line" */
    override var layout: LineLayout?
        get() = definedExternally
        set(value) = definedExternally
    override var paint: LinePaint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface RasterLayer : Layer {
    override var type: String /* "raster" */
    override var layout: RasterLayout?
        get() = definedExternally
        set(value) = definedExternally
    override var paint: RasterPaint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SymbolLayer : Layer {
    override var type: String /* "symbol" */
    override var layout: SymbolLayout?
        get() = definedExternally
        set(value) = definedExternally
    override var paint: SymbolPaint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SkyLayer : Layer {
    override var type: String /* "sky" */
    override var layout: SkyLayout?
        get() = definedExternally
        set(value) = definedExternally
    override var paint: SkyPaint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CustomLayerInterface {
    var id: String
    var type: String /* "custom" */
    var renderingMode: String? /* "2d" | "3d" */
        get() = definedExternally
        set(value) = definedExternally
    val onRemove: ((map: Map, gl: WebGLRenderingContext) -> Unit)?
    val onAdd: ((map: Map, gl: WebGLRenderingContext) -> Unit)?
    val prerender: ((gl: WebGLRenderingContext, matrix: Array<Number>) -> Unit)?
    fun render(gl: WebGLRenderingContext, matrix: Array<Number>)
}

external interface StyleFunction {
    var stops: Array<Array<Any>>?
        get() = definedExternally
        set(value) = definedExternally
    var property: String?
        get() = definedExternally
        set(value) = definedExternally
    var base: Number?
        get() = definedExternally
        set(value) = definedExternally
    var type: String? /* "identity" | "exponential" | "interval" | "categorical" */
        get() = definedExternally
        set(value) = definedExternally
    var default: Any?
        get() = definedExternally
        set(value) = definedExternally
    var colorSpace: String? /* "rgb" | "lab" | "hcl" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface Layout {
    var visibility: String? /* "visible" | "none" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface BackgroundLayout : Layout

external interface BackgroundPaint {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: dynamic /* String? | JsTuple<String, Any> */)
    @nativeSetter
    operator fun set(key: String, value: Transition?)
    @nativeSetter
    operator fun set(key: String, value: String?)
}

external interface FillLayout : Layout {
    @nativeGetter
    operator fun get(key: String): dynamic? /* Number? | JsTuple<String, Any> */
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Number? | JsTuple<String, Any> */)
}

external interface FillPaint {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Boolean? | JsTuple<String, Any> */)
    @nativeSetter
    operator fun set(key: String, value: Transition?)
    @nativeSetter
    operator fun set(key: String, value: Array<Number>?)
    @nativeSetter
    operator fun set(key: String, value: String? /* "map" | "viewport" */)
}

external interface FillExtrusionLayout : Layout

external interface FillExtrusionPaint {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Number? | JsTuple<String, Any> */)
    @nativeSetter
    operator fun set(key: String, value: Transition?)
    @nativeSetter
    operator fun set(key: String, value: String? /* "map" | "viewport" */)
    @nativeSetter
    operator fun set(key: String, value: Boolean?)
}

external interface LineLayout : Layout {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: dynamic /* "butt" | "round" | "square" | JsTuple<String, Any> */)
}

external interface LinePaint {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Number? | StyleFunction? | JsTuple<String, Any> */)
    @nativeSetter
    operator fun set(key: String, value: Transition?)
    @nativeSetter
    operator fun set(key: String, value: String? /* "map" | "viewport" */)
}

external interface SymbolLayout : Layout {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: String? /* "point" | "line" | "line-center" */)
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Number? | JsTuple<String, Any> */)
    @nativeSetter
    operator fun set(key: String, value: Boolean?)
    @nativeSetter
    operator fun set(key: String, value: Array<String? /* "center" | "left" | "right" | "top" | "bottom" | "top-left" | "top-right" | "bottom-left" | "bottom-right" */>?)
}

external interface SymbolPaint {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Number? | StyleFunction? | JsTuple<String, Any> */)
    @nativeSetter
    operator fun set(key: String, value: Transition?)
    @nativeSetter
    operator fun set(key: String, value: String? /* "map" | "viewport" */)
}

external interface RasterLayout : Layout

external interface RasterPaint {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Number? | JsTuple<String, Any> */)
    @nativeSetter
    operator fun set(key: String, value: Transition?)
    @nativeSetter
    operator fun set(key: String, value: String? /* "linear" | "nearest" */)
}

external interface CircleLayout : Layout {
    @nativeGetter
    operator fun get(key: String): dynamic? /* Number? | JsTuple<String, Any> */
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Number? | JsTuple<String, Any> */)
}

external interface CirclePaint {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Number? | StyleFunction? | JsTuple<String, Any> */)
    @nativeSetter
    operator fun set(key: String, value: Transition?)
    @nativeSetter
    operator fun set(key: String, value: String? /* "map" | "viewport" */)
}

external interface HeatmapLayout : Layout

external interface HeatmapPaint {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Number? | StyleFunction? | JsTuple<String, Any> */)
    @nativeSetter
    operator fun set(key: String, value: Transition?)
}

external interface HillshadeLayout : Layout

external interface HillshadePaint {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: dynamic /* Number? | JsTuple<String, Any> */)
    @nativeSetter
    operator fun set(key: String, value: String? /* "map" | "viewport" */)
    @nativeSetter
    operator fun set(key: String, value: Transition?)
}

external interface SkyLayout : Layout

external interface SkyPaint {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: dynamic /* String? | JsTuple<String, Any> */)
    @nativeSetter
    operator fun set(key: String, value: String? /* "gradient" | "atmosphere" */)
}

external interface ElevationQueryOptions {
    var exaggerated: Boolean
}
