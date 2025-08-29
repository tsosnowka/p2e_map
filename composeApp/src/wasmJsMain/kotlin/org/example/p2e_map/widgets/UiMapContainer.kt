package org.example.p2e_map.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.io.Buffer
import kotlinx.io.RawSource
import org.example.p2e_map.date.Place
import org.jetbrains.compose.resources.painterResource
import ovh.plrapps.mapcompose.api.*
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.MapUI
import ovh.plrapps.mapcompose.ui.state.MapState
import p2e_map.composeapp.generated.resources.Res
import p2e_map.composeapp.generated.resources.pin_png

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UiMapContainer(
    modifier: Modifier = Modifier,
    mapState: MapState
) {
    val step = 1.2
    mapState.disableZooming()
    mapState.disableFlingZoom()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        Place.getAll().forEach {
            addPin(mapState, it)
        }
    }

    Box(
        modifier
            .onPointerEvent(PointerEventType.Scroll, pass = PointerEventPass.Initial) { ev ->
                val firstChange = ev.changes.firstOrNull() ?: return@onPointerEvent
                val dy = firstChange.scrollDelta.y
                val factor = if (dy < 0f) step else (1.0 / step)
                val newScale = (mapState.scale * factor).coerceIn(mapState.minScale, mapState.maxScale)
                ev.changes.forEach { it.consume() }
                scope.launch {
                    mapState.scrollTo(
                        mapState.centroidX,
                        mapState.centroidY,
                        destScale = mapState.scale
                    )
                }
                mapState.scale = newScale
            }
            .fillMaxSize()
    ) {
        MapUI(
            modifier = Modifier.fillMaxSize(),
            state = mapState
        )
    }
}

private fun addPin(
    mapState: MapState,
    place: Place
) {
    mapState.addMarker(
        id = "${place.id}",
        x = place.x,
        y = place.y
    ) {
        val painter = painterResource(Res.drawable.pin_png)
        Box(modifier = Modifier.size(48.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { showCallout(mapState, place) }
                )
            }
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Pin",
                painter = painter
            )
            Text(
                text = "${place.id}",
                modifier = Modifier
                    .wrapContentSize()
                    .padding(bottom = 8.dp)
                    .align(Alignment.Center),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        }

    }
}

private fun showCallout(
    mapState: MapState,
    place: Place
) {
    val calloutId = "callout-${place.id}"

    mapState.removeCallout(calloutId)

    mapState.addCallout(
        id = calloutId,
        x = place.x,
        y = place.y,
        autoDismiss = true
    ) {
        Surface(
            shadowElevation = 8.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(Modifier.padding(10.dp)) {
                Text("${place.id} | ${place.description}", style = MaterialTheme.typography.titleMedium)
                TextButton(
                    onClick = { mapState.removeCallout(calloutId) }
                ) { Text("Zamknij") }
            }
        }
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