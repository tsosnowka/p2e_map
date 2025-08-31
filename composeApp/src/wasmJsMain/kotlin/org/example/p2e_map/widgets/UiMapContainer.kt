package org.example.p2e_map.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import kotlinx.coroutines.launch
import kotlinx.io.Buffer
import kotlinx.io.RawSource
import org.example.p2e_map.date.Place
import org.jetbrains.skiko.hostOs
import ovh.plrapps.mapcompose.api.*
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.MapUI
import ovh.plrapps.mapcompose.ui.state.MapState
import p2e_map.composeapp.generated.resources.Res

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UiMapContainer(
    mapState: MapState,
    modifier: Modifier = Modifier
) {
    mapState.disableZooming()
    mapState.disableFlingZoom()
    var newModifier: Modifier = modifier
    if (hostOs.isLinux || hostOs.isWindows || hostOs.isMacOS) {
        val step = 1.2
        val scope = rememberCoroutineScope()
        newModifier = newModifier
            .onPointerEvent(
                eventType = PointerEventType.Scroll,
                pass = PointerEventPass.Initial
            ) { ev ->
                val firstChange = ev.changes.firstOrNull() ?: return@onPointerEvent
                val dy = firstChange.scrollDelta.y
                val factor = if (dy < 0f) step else (1.0 / step)
                val oldScale = mapState.scale
                val newScale = (oldScale * factor)
                ev.changes.forEach { it.consume() }
                val newX = mapState.centroidX
                val newY = mapState.centroidY
                mapState.scale = newScale
                scope.launch {
                    mapState.snapScrollTo(
                        newX,
                        newY
                    )
                }
            }
    }
    Box(
        newModifier.fillMaxSize()
    ) {
        MapUI(
            modifier = Modifier.fillMaxSize(),
            state = mapState
        )
    }
}

fun addPin(
    onClick: (Place) -> Unit,
    mapState: MapState,
    place: Place
) {
    mapState.addMarker(
        id = "${place.id}",
        x = place.x,
        y = place.y
    ) {
        UiPinImage({
            onClick(place)
            showCallout(mapState, place) }, place,false)
    }
}

fun showCallout(
    mapState: MapState,
    place: Place,
    enableTooltips:Boolean = false
) {
    if (enableTooltips){
        val calloutId = "${place.id}"
        mapState.removeCallout(calloutId)
        mapState.addCallout(
            id = calloutId,
            x = place.x,
            y = place.y,
            autoDismiss = true,
            c = { UiMapTooltip(calloutId, mapState, place) }
        )
    }
}

class ResourcesTileProvider(
    private val basePath: String = "drawable/tiles"
) : TileStreamProvider {

    override suspend fun getTileStream(
        row: Int,
        col: Int,
        zoomLvl: Int
    ): RawSource? {
        val path = "$basePath/$zoomLvl/$row/$col.png"
        return kotlin.runCatching {
            val bytes = Res.readBytes(path)
            Buffer().apply { write(bytes) }
        }.getOrNull()
    }
}