package org.example.p2e_map.widgets

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.example.p2e_map.date.Place
import ovh.plrapps.mapcompose.api.removeCallout
import ovh.plrapps.mapcompose.ui.state.MapState

@Composable
fun UiMapTooltip(
    calloutId: String,
    mapState: MapState,
    place: Place
) {
    Surface(
        shadowElevation = 8.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { mapState.removeCallout(calloutId) }
                    )
                },
                text = "${place.id} | ${place.description}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

