package org.example.p2e_map.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.p2e_map.date.Place
import org.jetbrains.compose.resources.painterResource
import p2e_map.composeapp.generated.resources.Res
import p2e_map.composeapp.generated.resources.pin_disabled
import p2e_map.composeapp.generated.resources.pin_enabled

@Composable
fun UiPinImage(
    onTapAction: () -> Unit,
    place: Place,
    enabled: Boolean
) {
    val painter = painterResource(
        if (enabled) Res.drawable.pin_enabled
        else Res.drawable.pin_disabled
    )
    Box(modifier = Modifier.size(48.dp)
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { onTapAction() }
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
            fontSize = 20.sp
        )
    }
}

