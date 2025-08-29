package org.example.p2e_map.widgets

import p2e_map.composeapp.generated.resources.Res
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import p2e_map.composeapp.generated.resources.pin_png

@Composable
fun UiZoomableImage(
    modifier: Modifier = Modifier,
    res: DrawableResource = Res.drawable.pin_png
) {
    val painter = painterResource(res)

    var scale by remember { mutableStateOf(1f) }
    var pan by remember { mutableStateOf(Offset.Zero) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    // parametry
    val minScale = 0.25f
    val maxScale = 8f
    val step = 1.10f

    Box(
        modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7F9))
            .onGloballyPositioned { size = it.size }

            // 1) Double-click reset
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scale = 1f
                        pan = Offset.Zero
                    }
                )
            }

            // 2) Wheel zoom względem kursora przy stałym pivot=środek
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.type == PointerEventType.Scroll && size.width > 0 && size.height > 0) {
                            val change = event.changes.firstOrNull() ?: continue
                            val cursor = change.position

                            val old = scale
                            val dy = event.changes.sumOf { it.scrollDelta.y.toDouble() }.toFloat()
                            val r = if (dy > 0f) 1f / step else step
                            val new = (old * r).coerceIn(minScale, maxScale)

                            if (new != old) {
                                val ratio = new / old
                                val pivotPx = Offset(size.width * 0.5f, size.height * 0.5f)
//                                val newPan = pan * ratio + (cursor - pivotPx) * (1f - ratio)
                                val newPan = Offset(
                                    x = pan.x * ratio + (cursor.x - pivotPx.x) * (1f - ratio),
                                    y = pan.y * ratio + (cursor.y - pivotPx.y) * (1f - ratio)
                                )
                                scale = new
                                pan = clampPanAllowWhenZoomedOut(newPan, scale, size)
                            }

                            event.changes.forEach { it.consume() }
                        }
                    }
                }
            }

            // 3) Panning tylko podczas przeciągania LPM
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, drag ->
                        pan = clampPanAllowWhenZoomedOut(pan + drag, scale, size)
                        change.consume()
                    }
                )
            }
    ) {
        Image(
            painter = painter,
            contentDescription = "Zoomable image",
            contentScale = ContentScale.Fit, // zmień na Crop jeśli chcesz bez „belkowania”
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Pivot zawsze w środku:
                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                    translationX = pan.x
                    translationY = pan.y
                    scaleX = scale
                    scaleY = scale
                }
        )
    }
}

private fun clampPanAllowWhenZoomedOut(p: Offset, s: Float, sz: IntSize): Offset {
    val halfW = sz.width * 0.5f
    val halfH = sz.height * 0.5f
    val extentX = kotlin.math.abs(s - 1f) * halfW
    val extentY = kotlin.math.abs(s - 1f) * halfH
    return Offset(
        p.x.coerceIn(-extentX, extentX),
        p.y.coerceIn(-extentY, extentY)
    )
}