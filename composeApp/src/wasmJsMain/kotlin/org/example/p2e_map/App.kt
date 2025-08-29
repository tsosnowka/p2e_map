package org.example.p2e_map

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.example.p2e_map.widgets.DetailedDrawerExample
import org.example.p2e_map.widgets.ResourcesTileProvider
import org.example.p2e_map.widgets.UiMapContainer
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.ui.state.MapState

@Composable
fun App() {
    MaterialTheme {
        val mapState = remember {
            MapState(
                levelCount = 1,
                fullWidth = 3840,
                fullHeight = 2160,
                tileSize = 1080
            ).apply {
                addLayer(ResourcesTileProvider("drawable/tiles"))
            }
        }
        DetailedDrawerExample(mapState) { paddingValues ->
            UiMapContainer(modifier = Modifier.padding(paddingValues),mapState=mapState)
//            val imageWidthPx: Int = 3840
//            val imageHeightPx: Int = 2160
//            val tilesBasePath: String = "drawable/tiles"
//            val mapState = remember {
//                MapState(
//                    levelCount = 1,
//                    fullWidth = imageWidthPx,
//                    fullHeight = imageHeightPx,
//                    tileSize = 1080
//                ).apply {
//                    addLayer(ResourcesTileProvider(tilesBasePath))
//                }
//            }
//            mapState.disableZooming()
//            mapState.disableFlingZoom()
//            ClickToAddPinsMap(modifier = Modifier.padding(paddingValues), mapState = mapState, fullWidth = imageWidthPx, fullHeight = imageHeightPx)
        }
    }
}