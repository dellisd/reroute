package ca.derekellis.reroute.map.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.dellisd.reroute.map.compose.ExpressionNode
import io.github.dellisd.reroute.map.compose.ExpressionType
import org.jetbrains.compose.web.css.CSSColorValue

enum class Anchor(val value: String) {
    Map("map"),
    Viewport("viewport"),
}

enum class PositionAnchor(val value: String) {
    Center("center"),
    Left("left"),
    Right("right"),
    Top("top"),
    Bottom("bottom"),
    TopLeft("top-left"),
    BottomLeft("bottom-left"),
    BottomRight("top-left"),
}

enum class Justify(val value: String) {
    Auto("auto"),
    Left("left"),
    Center("center"),
    Right("right"),
}

enum class Visibility(val value: String) {
    Visible("visible"),
    None("none"),
}

enum class LineCap(val value: String) {
    Butt("butt"),
    Round("round"),
    Square("square"),
}

enum class LineJoin(val value: String) {
    Bevel("bevel"),
    Round("round"),
    Miter("miter"),
}

enum class Alignment(val value: String) {
    Map("map"),
    Viewport("viewport"),
    Auto("auto"),
}

enum class TextFit(val value: String) {
    None("none"),
    Width("width"),
    Height("height"),
    Both("both")
}

enum class SymbolPlacement(val value: String) {
    Point("point"),
    Line("line"),
    LineCenter("line-center"),
}

enum class SymbolZOrder(val value: String) {
    Auto("auto"),
    ViewportY("viewport-y"),
    Source("source"),
}

enum class TextTransform(val value: String) {
    None("none"),
    Uppercase("uppercase"),
    Lowercase("lowercase"),
}

enum class WritingMode(val value: String) {
    Horizontal("horizontal"),
    Vertical("vertical"),
}

abstract class LayerScope(protected val layerId: String, private val mapRef: mapbox.Map) {
    @Composable
    protected fun paint(name: String, value: Any?) {
        ComposeNode<ExpressionNode, MapNodeApplier>(
            factory = {
                ExpressionNode(name, value, ExpressionType.PAINT)
            },
            update = {
                set(value) {
                    this.expression = value
                    if (mapRef.getLayer(layerId) != null) {
                        mapRef.setPaintProperty(layerId, this.name, value)
                    }
                }
            }
        )
    }

    @Composable
    protected fun layout(name: String, value: Any?) {
        ComposeNode<ExpressionNode, MapNodeApplier>(
            factory = {
                ExpressionNode(name, value, ExpressionType.PAINT)
            },
            update = {
                set(value) {
                    this.expression = value
                    if (mapRef.getLayer(layerId) != null) {
                        mapRef.setLayoutProperty(layerId, this.name, value)
                    }
                }
            }
        )
    }

    @Composable
    fun visibility(visibility: Visibility) {
        layout("visibility", visibility.value)
    }
}

class BackgroundLayerScope(layerId: String, mapRef: mapbox.Map) : LayerScope(layerId, mapRef) {
    @Composable
    fun backgroundColor(color: String) {
        paint("background-color", color)
    }

    @Composable
    fun backgroundColor(color: CSSColorValue) {
        paint("background-color", color.toString())
    }

    @Composable
    fun backgroundColor(expression: Expression) {
        paint("background-color", expression)
    }

    @Composable
    fun backgroundOpacity(opacity: Double) {
        paint("background-opacity", opacity)
    }

    @Composable
    fun backgroundOpacity(expression: Expression) {
        paint("background-color", expression)
    }

    @Composable
    fun backgroundPattern(image: String) {
        paint("background-color", image)
    }

    @Composable
    fun backgroundPattern(expression: Expression) {
        paint("background-color", expression)
    }
}

class FillLayerScope(layerId: String, mapRef: mapbox.Map) : LayerScope(layerId, mapRef) {
    @Composable
    fun fillAntialias(value: Boolean) {
        paint("fill-antialias", value)
    }

    @Composable
    fun fillAntialias(expression: Expression) {
        paint("fill-antialias", expression)
    }

    @Composable
    fun fillColor(color: String) {
        paint("fill-color", color)
    }

    @Composable
    fun fillColor(color: CSSColorValue) {
        paint("fill-color", color.toString())
    }

    @Composable
    fun fillColor(expression: Expression) {
        paint("fill-color", expression)
    }

    @Composable
    fun fillOpacity(opacity: Double) {
        paint("fill-opacity", opacity)
    }

    @Composable
    fun fillOpacity(expression: Expression) {
        paint("fill-opacity", expression)
    }

    @Composable
    fun fillOutlineColor(color: String) {
        paint("fill-outline-color", color)
    }

    @Composable
    fun fillOutlineColor(color: CSSColorValue) {
        paint("fill-outline-color", color.toString())
    }

    @Composable
    fun fillOutlineColor(expression: Expression) {
        paint("fill-outline-color", expression)
    }

    @Composable
    fun fillPattern(image: String) {
        paint("fill-pattern", image)
    }

    @Composable
    fun fillPattern(expression: Expression) {
        paint("fill-pattern", expression)
    }

    @Composable
    fun fillSortKey(key: Double) {
        layout("fill-sort-key", key)
    }

    @Composable
    fun fillSortKey(expression: Expression) {
        layout("fill-sort-key", expression)
    }

    @Composable
    fun fillTranslate(x: Double, y: Double) {
        paint("fill-translate", arrayOf(x, y))
    }

    @Composable
    fun fillTranslate(expression: Expression) {
        paint("fill-translate", expression)
    }

    @Composable
    fun fillTranslateAnchor(anchor: Anchor) {
        paint("fill-translate-anchor", anchor.value)
    }

    @Composable
    fun fillTranslateAnchor(expression: Expression) {
        paint("fill-translate-anchor", expression)
    }
}

class LineLayerScope(layerId: String, mapRef: mapbox.Map) : LayerScope(layerId, mapRef) {
    @Composable
    fun lineBlur(blur: Double) {
        paint("line-blur", blur)
    }

    @Composable
    fun lineBlur(expression: Expression) {
        paint("line-blur", expression)
    }

    @Composable
    fun lineCap(cap: LineCap) {
        layout("line-cap", cap.value)
    }

    @Composable
    fun lineCap(expression: Expression) {
        layout("line-cap", expression)
    }

    @Composable
    fun lineColor(color: String) {
        paint("line-color", color)
    }

    @Composable
    fun lineColor(color: CSSColorValue) {
        paint("line-color", color.toString())
    }

    @Composable
    fun lineColor(expression: Expression) {
        paint("line-color", expression)
    }

    @Composable
    fun lineDasharray(vararg values: Double) {
        paint("line-dasharray", values)
    }

    @Composable
    fun lineDasharray(expression: Expression) {
        paint("line-dasharray", expression)
    }

    @Composable
    fun lineGapWidth(width: Double) {
        paint("line-gap-width", width)
    }

    @Composable
    fun lineGapWidth(expression: Expression) {
        paint("line-gap-width", expression)
    }

    @Composable
    fun lineGradient(expression: Expression) {
        paint("line-gradient", expression)
    }

    @Composable
    fun lineJoin(join: LineJoin) {
        layout("line-join", join)
    }

    @Composable
    fun lineJoin(expression: Expression) {
        layout("line-join", expression)
    }

    @Composable
    fun lineMiterLimit(limit: Double) {
        layout("line-miter-limit", limit)
    }

    @Composable
    fun lineMiterLimit(expression: Expression) {
        layout("line-miter-limit", expression)
    }

    @Composable
    fun lineOffset(offset: Double) {
        paint("line-offset", offset)
    }

    @Composable
    fun lineOffset(expression: Expression) {
        paint("line-offset", expression)
    }

    @Composable
    fun linePattern(pattern: String) {
        paint("line-pattern", pattern)
    }

    @Composable
    fun linePattern(expression: Expression) {
        paint("line-pattern", expression)
    }

    @Composable
    fun lineRoundLimit(limit: Double) {
        layout("line-round-limit", limit)
    }

    @Composable
    fun lineRoundLimit(expression: Expression) {
        layout("line-round-limit", expression)
    }

    @Composable
    fun lineSortKey(key: Double) {
        layout("line-sort-key", key)
    }

    @Composable
    fun lineSortKey(expression: Expression) {
        layout("line-sort-key", expression)
    }

    @Composable
    fun lineTranslate(expression: Expression) {
        paint("line-translate", expression)
    }

    @Composable
    fun lineTranslateAnchor(anchor: Anchor) {
        paint("line-translate-anchor", anchor)
    }

    @Composable
    fun lineTranslateAnchor(expression: Expression) {
        paint("line-translate-anchor", expression)
    }

    @Composable
    fun lineWidth(width: Double) {
        paint("line-width", width)
    }

    @Composable
    fun lineWidth(expression: Expression) {
        paint("line-width", expression)
    }
}

class SymbolLayerScope(layerId: String, mapRef: mapbox.Map) : LayerScope(layerId, mapRef) {
    @Composable
    fun iconAllowOverlap(allow: Boolean) {
        layout("icon-allow-overlap", allow)
    }

    @Composable
    fun iconAllowOverlap(expression: Expression) {
        layout("icon-allow-overlap", expression)
    }

    @Composable
    fun iconAnchor(anchor: PositionAnchor) {
        layout("icon-anchor", anchor.value)
    }

    @Composable
    fun iconAnchor(expression: Expression) {
        layout("icon-anchor", expression)
    }

    @Composable
    fun iconColor(color: String) {
        paint("icon-color", color)
    }

    @Composable
    fun iconColor(color: CSSColorValue) {
        paint("icon-color", color.toString())
    }

    @Composable
    fun iconColor(expression: Expression) {
        paint("icon-color", expression)
    }

    @Composable
    fun iconHaloBlur(blur: Double) {
        paint("icon-halo-blur", blur)
    }

    @Composable
    fun iconHaloBlur(expression: Expression) {
        paint("icon-halo-blur", expression)
    }

    @Composable
    fun iconHaloColor(color: String) {
        paint("icon-halo-color", color)
    }

    @Composable
    fun iconHaloColor(color: CSSColorValue) {
        paint("icon-halo-color", color.toString())
    }

    @Composable
    fun iconHaloColor(expression: Expression) {
        paint("icon-halo-color", expression)
    }

    @Composable
    fun iconHaloWidth(width: Double) {
        paint("icon-halo-width", width)
    }

    @Composable
    fun iconHaloWidth(expression: Expression) {
        paint("icon-halo-width", expression)
    }

    @Composable
    fun iconIgnorePlacement(ignore: Boolean) {
        layout("icon-ignore-placement", ignore)
    }

    @Composable
    fun iconIgnorePlacement(expression: Expression) {
        layout("icon-ignore-placement", expression)
    }

    @Composable
    fun iconImage(image: String) {
        layout("icon-image", image)
    }

    @Composable
    fun iconImage(expression: Expression) {
        layout("icon-image", expression)
    }

    @Composable
    fun iconKeepUpright(keep: Boolean) {
        layout("icon-keep-upright", keep)
    }

    @Composable
    fun iconKeepUpright(expression: Expression) {
        layout("icon-keep-upright", expression)
    }

    @Composable
    fun iconOffset(x: Double, y: Double) {
        layout("icon-offset", arrayOf(x, y))
    }

    @Composable
    fun iconOffset(expression: Expression) {
        layout("icon-offset", expression)
    }

    @Composable
    fun iconOpacity(opacity: Double) {
        paint("icon-opacity", opacity)
    }

    @Composable
    fun iconOpacity(expression: Expression) {
        paint("icon-opacity", expression)
    }

    @Composable
    fun iconOptional(optional: Boolean) {
        layout("icon-optional", optional)
    }

    @Composable
    fun iconOptional(expression: Expression) {
        layout("icon-optional", expression)
    }

    @Composable
    fun iconPadding(padding: Double) {
        layout("icon-padding", padding)
    }

    @Composable
    fun iconPadding(expression: Expression) {
        layout("icon-padding", expression)
    }

    @Composable
    fun iconPitchAlignment(alignment: Alignment) {
        layout("icon-pitch-alignment", alignment.value)
    }

    @Composable
    fun iconPitchAlignment(expression: Expression) {
        layout("icon-pitch-alignment", expression)
    }

    @Composable
    fun iconRotate(rotation: Double) {
        layout("icon-rotation", rotation)
    }

    @Composable
    fun iconRotate(expression: Expression) {
        layout("icon-rotation", expression)
    }

    @Composable
    fun iconRotationAlignment(alignment: Alignment) {
        layout("icon-rotation-alignment", alignment.value)
    }

    @Composable
    fun iconRotationAlignment(expression: Expression) {
        layout("icon-rotation-alignment", expression)
    }

    @Composable
    fun iconSize(size: Double) {
        layout("icon-size", size)
    }

    @Composable
    fun iconSize(expression: Expression) {
        layout("icon-size", expression)
    }

    @Composable
    fun iconTextFit(fit: TextFit) {
        layout("icon-text-fit", fit.value)
    }

    @Composable
    fun iconTextFit(expression: Expression) {
        layout("icon-text-fit", expression)
    }

    @Composable
    fun iconTextFitPadding(top: Double = 0.0, right: Double = 0.0, bottom: Double = 0.0, left: Double = 0.0) {
        layout("icon-text-fit-padding", arrayOf(top, right, bottom, left))
    }

    @Composable
    fun iconTextFitPadding(expression: Expression) {
        layout("icon-text-fit-padding", expression)
    }

    @Composable
    fun iconTranslate(x: Double, y: Double) {
        paint("icon-translate", arrayOf(x, y))
    }

    @Composable
    fun iconTranslate(expression: Expression) {
        paint("icon-translate", expression)
    }

    @Composable
    fun iconTranslateAnchor(anchor: Anchor) {
        paint("icon-translate-anchor", anchor.value)
    }

    @Composable
    fun iconTranslateAnchor(expression: Expression) {
        paint("icon-translate-anchor", expression)
    }

    @Composable
    fun symbolAvoidEdges(avoid: Boolean) {
        layout("symbol-avoid-edges", avoid)
    }

    @Composable
    fun symbolAvoidEdges(expression: Expression) {
        layout("symbol-avoid-edges", expression)
    }

    @Composable
    fun symbolPlacement(placement: SymbolPlacement) {
        layout("symbol-placement", placement.value)
    }

    @Composable
    fun symbolPlacement(expression: Expression) {
        layout("symbol-placement", expression)
    }

    @Composable
    fun symbolSortKey(key: Double) {
        layout("symbol-sort-key", key)
    }

    @Composable
    fun symbolSortKey(expression: Expression) {
        layout("symbol-sort-key", expression)
    }

    @Composable
    fun symbolSpacing(spacing: Double) {
        layout("symbol-spacing", spacing)
    }

    @Composable
    fun symbolSpacing(expression: Expression) {
        layout("symbol-spacing", expression)
    }

    @Composable
    fun symbolZOrder(order: SymbolZOrder) {
        layout("symbol-z-order", order.value)
    }

    @Composable
    fun symbolZOrder(expression: Expression) {
        layout("symbol-z-order", expression)
    }

    @Composable
    fun textAllowOverlap(overlap: Boolean) {
        layout("text-allow-overlap", overlap)
    }

    @Composable
    fun textAllowOverlap(expression: Expression) {
        layout("text-allow-overlap", expression)
    }

    @Composable
    fun textAnchor(anchor: PositionAnchor) {
        layout("text-anchor", anchor.value)
    }

    @Composable
    fun textAnchor(expression: Expression) {
        layout("text-anchor", expression)
    }

    @Composable
    fun textColor(color: String) {
        paint("text-color", color)
    }

    @Composable
    fun textColor(color: CSSColorValue) {
        paint("text-color", color.toString())
    }

    @Composable
    fun textColor(expression: Expression) {
        paint("text-color", expression)
    }

    @Composable
    fun textField(field: String) {
        layout("text-field", field)
    }

    @Composable
    fun textField(expression: Expression) {
        layout("text-field", expression)
    }

    @Composable
    fun textFont(vararg stack: String) {
        layout("text-font", stack)
    }

    @Composable
    fun textFont(expression: Expression) {
        layout("text-font", expression)
    }

    @Composable
    fun textHaloBlur(blur: Double) {
        paint("text-halo-blur", blur)
    }

    @Composable
    fun textHaloBlur(expression: Expression) {
        paint("text-halo-blur", expression)
    }

    @Composable
    fun textHaloColor(color: String) {
        paint("text-halo-color", color)
    }

    @Composable
    fun textHaloColor(color: CSSColorValue) {
        paint("text-halo-color", color.toString())
    }

    @Composable
    fun textHaloColor(expression: Expression) {
        paint("text-halo-color", expression)
    }

    @Composable
    fun textHaloWidth(width: Double) {
        paint("text-halo-width", width)
    }

    @Composable
    fun textHaloWidth(expression: Expression) {
        paint("text-halo-width", expression)
    }

    @Composable
    fun textIgnorePlacement(ignore: Boolean) {
        layout("text-ignore-placement", ignore)
    }

    @Composable
    fun textIgnorePlacement(expression: Expression) {
        layout("text-ignore-placement", expression)
    }

    @Composable
    fun textJustify(justify: Justify) {
        layout("text-justify", justify.value)
    }

    @Composable
    fun textJustify(expression: Expression) {
        layout("text-justify", expression)
    }

    @Composable
    fun textKeepUpright(keep: Boolean) {
        layout("text-keep-upright", keep)
    }

    @Composable
    fun textKeepUpright(expression: Expression) {
        layout("text-keep-upright", expression)
    }

    @Composable
    fun textLetterSpacing(spacing: Double) {
        layout("text-letter-spacing", spacing)
    }

    @Composable
    fun textLetterSpacing(expression: Expression) {
        layout("text-letter-spacing", expression)
    }

    @Composable
    fun textLineHeight(height: Double) {
        layout("text-line-height", height)
    }

    @Composable
    fun textLineHeight(expression: Expression) {
        layout("text-line-height", expression)
    }

    @Composable
    fun textMaxAngle(max: Double) {
        layout("text-max-angle", max)
    }

    @Composable
    fun textMaxAngle(expression: Expression) {
        layout("text-max-angle", expression)
    }

    @Composable
    fun textMaxWidth(max: Double) {
        layout("text-max-width", max)
    }

    @Composable
    fun textMaxWidth(expression: Expression) {
        layout("text-max-width", expression)
    }

    @Composable
    fun textOffset(x: Double, y: Double) {
        layout("text-offset", arrayOf(x, y))
    }

    @Composable
    fun textOffset(expression: Expression) {
        layout("text-offset", expression)
    }

    @Composable
    fun textOpacity(opacity: Double) {
        paint("text-opacity", opacity)
    }

    @Composable
    fun textOpacity(expression: Expression) {
        paint("text-opacity", expression)
    }

    @Composable
    fun textOptional(optional: Boolean) {
        layout("text-optional", optional)
    }

    @Composable
    fun textOptional(expression: Expression) {
        layout("text-optional", expression)
    }

    @Composable
    fun textPadding(padding: Double) {
        layout("text-padding", padding)
    }

    @Composable
    fun textPadding(expression: Expression) {
        layout("text-padding", expression)
    }

    @Composable
    fun textPitchAlignment(alignment: Alignment) {
        layout("text-pitch-alignment", alignment.value)
    }

    @Composable
    fun textPitchAlignment(expression: Expression) {
        layout("text-pitch-alignment", expression)
    }

    @Composable
    fun textRadialOffset(offset: Double) {
        layout("text-radial-offset", offset)
    }

    @Composable
    fun textRadialOffset(expression: Expression) {
        layout("text-radial-offset", expression)
    }

    @Composable
    fun textRotate(degrees: Double) {
        layout("text-rotate", degrees)
    }

    @Composable
    fun textRotate(expression: Expression) {
        layout("text-rotate", expression)
    }

    @Composable
    fun textRotationAlignment(alignment: Alignment) {
        layout("text-rotation-alignment", alignment.value)
    }

    @Composable
    fun textRotationAlignment(expression: Expression) {
        layout("text-rotation-alignment", expression)
    }

    @Composable
    fun textSize(size: Double) {
        layout("text-size", size)
    }

    @Composable
    fun textSize(expression: Expression) {
        layout("text-size", expression)
    }

    @Composable
    fun textTransform(transform: TextTransform) {
        layout("text-transform", transform.value)
    }

    @Composable
    fun textTransform(expression: Expression) {
        layout("text-transform", expression)
    }

    @Composable
    fun textTranslate(x: Double, y: Double) {
        paint("text-translate", arrayOf(x, y))
    }

    @Composable
    fun textTranslate(expression: Expression) {
        paint("text-translate", expression)
    }

    @Composable
    fun textTranslateAnchor(anchor: Anchor) {
        paint("text-translate-anchor", anchor.value)
    }

    @Composable
    fun textTranslateAnchor(expression: Expression) {
        paint("text-translate-anchor", expression)
    }

    @Composable
    fun textVariableAnchor(anchor: PositionAnchor) {
        layout("text-variable-anchor", anchor.value)
    }

    @Composable
    fun textVariableAnchor(expression: Expression) {
        layout("text-variable-anchor", expression)
    }

    @Composable
    fun textWritingMode(mode: WritingMode) {
        layout("text-writing-mode", mode.value)
    }

    @Composable
    fun textWritingMode(expression: Expression) {
        layout("text-writing-mode", expression)
    }
}

class CircleLayerScope(layerId: String, mapRef: mapbox.Map) : LayerScope(layerId, mapRef) {
    @Composable
    fun circleBlur(blur: Number) {
        paint("circle-blur", blur)
    }

    @Composable
    fun circleBlur(expression: Expression) {
        paint("circle-blur", expression)
    }

    @Composable
    fun circleColor(color: String) {
        paint("circle-color", color)
    }

    @Composable
    fun circleColor(color: CSSColorValue) {
        paint("circle-color", color.toString())
    }

    @Composable
    fun circleColor(expression: Expression) {
        paint("circle-color", expression)
    }

    @Composable
    fun circleOpacity(opacity: Number) {
        paint("circle-opacity", opacity)
    }

    @Composable
    fun circleOpacity(expression: Expression) {
        paint("circle-opacity", expression)
    }

    @Composable
    fun circlePitchAlignment(alignment: Anchor) {
        paint("circle-pitch-alignment", alignment.value)
    }

    @Composable
    fun circlePitchAlignment(expression: Expression) {
        paint("circle-pitch-alignment", expression)
    }

    @Composable
    fun circlePitchScale(scale: Anchor) {
        paint("circle-pitch-scale", scale.value)
    }

    @Composable
    fun circlePitchScale(expression: Expression) {
        paint("circle-pitch-scale", expression)
    }

    @Composable
    fun circleRadius(radius: Number) {
        paint("circle-radius", radius)
    }

    @Composable
    fun circleRadius(expression: Expression) {
        paint("circle-radius", expression)
    }

    @Composable
    fun circleSortKey(key: Number) {
        layout("circle-sort-key", key)
    }

    @Composable
    fun circleSortKey(expression: Expression) {
        layout("circle-sort-key", expression)
    }

    @Composable
    fun circleStrokeColor(color: String) {
        paint("circle-stroke-color", color)
    }

    @Composable
    fun circleStrokeColor(color: CSSColorValue) {
        paint("circle-stroke-color", color.toString())
    }

    @Composable
    fun circleStrokeColor(expression: Expression) {
        paint("circle-stroke-color", expression)
    }

    @Composable
    fun circleStrokeOpacity(opacity: Number) {
        paint("circle-stroke-opacity", opacity)
    }

    @Composable
    fun circleStrokeOpacity(expression: Expression) {
        paint("circle-stroke-opacity", expression)
    }

    @Composable
    fun circleStrokeWidth(width: Number) {
        paint("circle-stroke-width", width)
    }

    @Composable
    fun circleStrokeWidth(expression: Expression) {
        paint("circle-stroke-width", expression)
    }

    @Composable
    fun circleTranslate(x: Number, y: Number) {
        paint("circle-translate", arrayOf(x, y))
    }

    @Composable
    fun circleTranslate(expression: Expression) {
        paint("circle-translate", expression)
    }

    @Composable
    fun circleTranslateAnchor(anchor: Anchor) {
        paint("circle-translate-anchor", anchor.value)
    }

    @Composable
    fun circleTranslateAnchor(expression: Expression) {
        paint("circle-translate-anchor", expression)
    }
}
