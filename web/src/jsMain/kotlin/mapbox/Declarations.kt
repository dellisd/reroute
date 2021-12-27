package mapbox


typealias quat = Array<Number>

typealias vec3 = Array<Number>

typealias TransformRequestFunction = (url: String, resourceType: String /* "Unknown" | "Style" | "Source" | "Tile" | "Glyphs" | "SpriteImage" | "SpriteJSON" | "Image" */) -> RequestParameters

typealias ExpressionSpecification = Array<Any>

typealias EventedListener = (obj: Any) -> Any
