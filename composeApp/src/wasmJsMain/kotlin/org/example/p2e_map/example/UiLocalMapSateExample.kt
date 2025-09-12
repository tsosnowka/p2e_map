package org.example.p2e_map.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import org.example.p2e_map.data.ResourcesTileProvider
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.ui.state.MapState

val LocalMapStateExample = compositionLocalOf<MapState> {
    error("No MapState provided")
}

@Composable
fun UiLocalMapSateExample() {
    val mapState = remember {
        MapState(
            levelCount = 1,
            fullWidth = 3840,
            fullHeight = 2160,
            tileSize = 3840
        ).apply {
            addLayer(ResourcesTileProvider("drawable/tiles"))
        }
    }
    CompositionLocalProvider(LocalMapStateExample provides mapState) {
        UiLocalMapSateExampleNested()
    }
}

@Composable
fun UiLocalMapSateExampleNested() {
    LocalMapStateExample.current.let {
        // do something with it
    }
}