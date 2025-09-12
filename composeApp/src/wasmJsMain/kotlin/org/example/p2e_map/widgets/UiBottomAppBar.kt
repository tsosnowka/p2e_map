package org.example.p2e_map.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.p2e_map.utils.formatted
import org.jetbrains.compose.resources.painterResource
import ovh.plrapps.mapcompose.api.scale
import ovh.plrapps.mapcompose.ui.state.MapState
import p2e_map.composeapp.generated.resources.Res
import p2e_map.composeapp.generated.resources.view_real_size
import p2e_map.composeapp.generated.resources.zoom_in
import p2e_map.composeapp.generated.resources.zoom_out

@Composable
fun UiBottomAppBar(
    mapState: MapState,
    zoom: Double,
    onScapScrollTo: (newScroll: Double) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.wrapContentSize().align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            VerticalDivider(modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp))
            Text(
                text = "Zoom",
                style = MaterialTheme.typography.titleLarge
            )
            VerticalDivider(modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp))
            Text(
                text = zoom.formatted(),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Row(
            modifier = Modifier.wrapContentSize().align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            VerticalDivider(Modifier.padding(vertical = 12.dp, horizontal = 4.dp))
            IconButton(onClick = { onScapScrollTo(mapState.scale * 1.2) }) {
                Icon(painter = painterResource(Res.drawable.zoom_in), contentDescription = null)
            }
            IconButton(onClick = { onScapScrollTo(mapState.scale * 0.8) }) {
                Icon(painter = painterResource(Res.drawable.zoom_out), contentDescription = null)
            }
            VerticalDivider(Modifier.padding(vertical = 12.dp, horizontal = 4.dp))
            IconButton(onClick = { onScapScrollTo(1.0) }) {
                Icon(painter = painterResource(Res.drawable.view_real_size), contentDescription = null)
            }
            VerticalDivider(Modifier.padding(vertical = 12.dp, horizontal = 4.dp))
        }
    }
}