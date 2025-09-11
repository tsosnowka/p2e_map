package org.example.p2e_map.widgets

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.example.p2e_map.data.Place
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skiko.hostOs
import p2e_map.composeapp.generated.resources.Res
import p2e_map.composeapp.generated.resources.arrow_back

@Composable
fun UiDrawerSheet(
    onDrawerClose: () -> Unit,
    currentPlace: Place,
    onEnableStateChange: (Place) -> Unit
) {
    ModalDrawerSheet {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onDrawerClose() }
                    )
                }
        ) {
            IconButton(
                onClick = {
                    onDrawerClose()
                }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back),
                    contentDescription = null
                )
            }
            VerticalDivider(Modifier.height(48.dp).padding(4.dp))
            Text(
                text = "Oklyon:",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )

        }
        HorizontalDivider()
        Column(
            modifier = Modifier.padding(horizontal = 4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "OS:$hostOs",
                style = MaterialTheme.typography.labelSmall
            )
            HorizontalDivider()
            Place.allPlaces.forEach { place ->
                UiDrawerItem(place, place.id == currentPlace.id) {
                    onDrawerClose()
                    onEnableStateChange(place)
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}