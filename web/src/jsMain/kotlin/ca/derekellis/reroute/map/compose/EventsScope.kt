package ca.derekellis.reroute.map.compose

class EventsScope(private val mapRef: mapbox.Map) {
    private inline fun <E> registerEvent(event: String, layers: List<String>? = null, callback: (E) -> Unit) {
        if (layers == null) {
            mapRef.on(event, callback.unsafeCast<(Any) -> Unit>())
        } else {
            mapRef.on(event, layers.toTypedArray(), callback.unsafeCast<(Any) -> Unit>())
        }
    }

    fun onMouseDown(layers: List<String>? = null, callback: (mapbox.MapMouseEvent) -> Unit) {
        registerEvent("mousedown", layers, callback)
    }

    fun onMouseUp(layers: List<String>? = null, callback: (mapbox.MapMouseEvent) -> Unit) {
        registerEvent("mouseup", layers, callback)
    }

    fun onMouseOver(layers: List<String>? = null, callback: (mapbox.MapMouseEvent) -> Unit) {
        registerEvent("mouseover", layers, callback)
    }

    fun onMouseMove(layers: List<String>? = null, callback: (mapbox.MapMouseEvent) -> Unit) {
        registerEvent("mousemove", layers, callback)
    }

    fun onPreClick(callback: (mapbox.MapMouseEvent) -> Unit) {
        registerEvent("preclick", null, callback)
    }

    fun onClick(layers: List<String>? = null, callback: (mapbox.MapMouseEvent) -> Unit) {
        registerEvent("click", layers, callback)
    }

    fun onDblClick(layers: List<String>? = null, callback: (mapbox.MapMouseEvent) -> Unit) {
        registerEvent("dblclick", layers, callback)
    }

    fun onMouseEnter(layers: List<String>, callback: (mapbox.MapMouseEvent) -> Unit) {
        registerEvent("mouseenter", layers, callback)
    }

    fun onMouseLeave(layers: List<String>, callback: (mapbox.MapMouseEvent) -> Unit) {
        registerEvent("mouseleave", layers, callback)
    }

    fun onMouseOut(layers: List<String>? = null, callback: (mapbox.MapMouseEvent) -> Unit) {
        registerEvent("mouseout", layers, callback)
    }

    fun onContextMenu(layers: List<String>? = null, callback: (mapbox.MapMouseEvent) -> Unit) {
        registerEvent("contextmenu", layers, callback)
    }

    fun onWheel(callback: (mapbox.MapWheelEvent) -> Unit) {
        registerEvent("wheel", null, callback)
    }

    fun onTouchStart(layers: List<String>? = null, callback: (mapbox.MapTouchEvent) -> Unit) {
        registerEvent("touchstart", layers, callback)
    }

    fun onTouchEnd(layers: List<String>? = null, callback: (mapbox.MapTouchEvent) -> Unit) {
        registerEvent("touchend", layers, callback)
    }

    fun onTouchMove(callback: (mapbox.MapTouchEvent) -> Unit) {
        registerEvent("touchmove", null, callback)
    }

    fun onTouchCancel(layers: List<String>? = null, callback: (mapbox.MapTouchEvent) -> Unit) {
        registerEvent("touchcancel", layers, callback)
    }
}