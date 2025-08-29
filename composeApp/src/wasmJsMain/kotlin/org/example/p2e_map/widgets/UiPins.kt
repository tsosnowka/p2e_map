package org.example.p2e_map.widgets

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.launch
import ovh.plrapps.mapcompose.api.addCallout
import ovh.plrapps.mapcompose.api.addMarker
import ovh.plrapps.mapcompose.api.scrollTo
import ovh.plrapps.mapcompose.ui.MapUI
import ovh.plrapps.mapcompose.ui.state.MapState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ClickToAddPinsMap(
    mapState: MapState,
    // Wymiary pełnego obrazu (takie, jak podałeś do MapState(fullWidth, fullHeight))
    fullWidth: Int,
    fullHeight: Int,
    modifier: Modifier = Modifier,
    minScale: Double = 1.0,
    maxScale: Double = 8.0
) {
    val scope = rememberCoroutineScope()

    // To samo sterowanie kamerą co wcześniej: środek i skala
    var center by remember { mutableStateOf(Offset(0.5f, 0.5f)) } // znormalizowany środek widoku
    var scale by remember { mutableStateOf(1.0) }

    // Rozmiar widoku mapy (potrzebny do przeliczeń ekran -> mapa)
    var viewport by remember { mutableStateOf(IntSize.Zero) }

    // Proste liczniki/id dla markerów
    var nextId by remember { mutableIntStateOf(1) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { viewport = it }
            // Scroll myszy -> "programowy" zoom wokół środka widoku (jak wcześniej)
            .onPointerEvent(PointerEventType.Scroll, pass = PointerEventPass.Initial) { ev ->
                val dy = ev.changes.firstOrNull()?.scrollDelta?.y ?: 0f
                if (dy != 0f) {
                    val step = 1.2
                    val factor = if (dy < 0f) step else 1.0 / step
                    val newScale = (scale * factor).coerceIn(minScale, maxScale)
                    if (newScale != scale) {
                        scale = newScale
                        scope.launch {
                            mapState.scrollTo(
                                x =center.x.toDouble(),
                                y = center.y.toDouble(),
                                destScale = newScale
                            )
                        }
                    }
                    ev.changes.forEach { it.consume() }
                }
            }
            // Klik w mapę -> dodaj marker na współrzędnych mapy pod kursorem
            .pointerInput(mapState, viewport, scale, center) {
                detectTapGestures { pos ->
                    val (nx, ny) = screenToNorm(
                        screen = pos,
                        viewport = viewport,
                        center = center,
                        scale = scale,
                        fullWidth = fullWidth,
                        fullHeight = fullHeight
                    )
                    val id = "pin-${nextId++}"

                    // Dodaj marker zakotwiczony do punktu mapy (domyślny anchor = lewy-górny rogu kontentu markera).
                    // Jeśli chcesz typową „pinezkę” z ostrzem do dołu, łatwo podbić offset wewnątrz composable markera.
                    mapState.addMarker(id, x = nx, y = ny) {
                        // MARKER UI + klik (otwieramy callout)
                        ElevatedButton(
                            onClick = {
                                scope.launch {
                                    mapState.addCallout("callout-$id", x = nx, y = ny) {
                                        Surface(tonalElevation = 2.dp) {
                                            Text("(${nx}, ${ny})")
                                        }
                                    }
                                }
                            },
                            contentPadding = PaddingValues(6.dp)
                        ) {
                            Text("📍")
                        }
                    }
                }
            }
    ) {
        MapUI(state = mapState, modifier = Modifier.fillMaxSize())
    }
}

/**
 * Przeliczenie punktu ekranu (piksele w obrębie MapUI) na współrzędne znormalizowane mapy (0..1).
 * Założenia:
 * - [center] to znormalizowany punkt mapy znajdujący się w środku ekranu (tak ustawiasz przez scrollTo).
 * - [scale] to bieżąca skala (1.0 = 1 piksel mapy -> 1 piksel ekranu).
 */
private fun screenToNorm(
    screen: Offset,
    viewport: IntSize,
    center: Offset,
    scale: Double,
    fullWidth: Int,
    fullHeight: Int
): Pair<Double, Double> {
    if (viewport.width == 0 || viewport.height == 0) {
        return center.x.toDouble() to center.y.toDouble()
    }

    // Środek w pikselach mapy
    val cx = center.x * fullWidth
    val cy = center.y * fullHeight

    // Odchylenie kliknięcia od środka ekranu (w pikselach ekranu)
    val dxScreen = screen.x - viewport.width / 2f
    val dyScreen = screen.y - viewport.height / 2f

    // Ekran -> mapa: przy skali S piksel mapy zajmuje S pikseli ekranu
    val dxMap = dxScreen / scale
    val dyMap = dyScreen / scale

    // Piksele mapy -> współrzędne znormalizowane
    val mx = cx + dxMap
    val my = cy + dyMap
    val nx = (mx / fullWidth).coerceIn(0.0, 1.0)
    val ny = (my / fullHeight).coerceIn(0.0, 1.0)
    return nx to ny
}
