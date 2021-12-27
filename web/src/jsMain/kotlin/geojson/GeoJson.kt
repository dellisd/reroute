package geojson

import kotlin.js.Json


typealias Position = Array<Number>

external interface GeoJsonObject {
    var type: String /* "Point" | "MultiPoint" | "LineString" | "MultiLineString" | "Polygon" | "MultiPolygon" | "GeometryCollection" */
    var bbox: dynamic /* JsTuple<Number, Number, Number, Number> | JsTuple<Number, Number, Number, Number, Number, Number> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface Geometry

external interface Point : GeoJsonObject, Geometry {
    override var type: String /* "Point" */
    var coordinates: Position
}

external interface MultiPoint : GeoJsonObject, Geometry {
    override var type: String /* "MultiPoint" */
    var coordinates: Array<Position>
}

external interface LineString : GeoJsonObject, Geometry {
    override var type: String /* "LineString" */
    var coordinates: Array<Position>
}

external interface MultiLineString : GeoJsonObject, Geometry {
    override var type: String /* "MultiLineString" */
    var coordinates: Array<Array<Position>>
}

external interface Polygon : GeoJsonObject, Geometry {
    override var type: String /* "Polygon" */
    var coordinates: Array<Array<Position>>
}

external interface MultiPolygon : GeoJsonObject, Geometry {
    override var type: String /* "MultiPolygon" */
    var coordinates: Array<Array<Array<Position>>>
}

external interface GeometryCollection : GeoJsonObject, Geometry {
    override var type: String /* "GeometryCollection" */
    var geometries: Array<dynamic /* Point | MultiPoint | LineString | MultiLineString | Polygon | MultiPolygon | GeometryCollection */>
}

external interface Feature : GeoJsonObject {
    override var type: String /* "Feature" */
    var geometry: Geometry
    var id: dynamic /* String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var properties: Json?
}

external interface FeatureCollection : GeoJsonObject {
    override var type: String /* "FeatureCollection" */
    var features: Array<Feature>
}
