package org.example.p2e_map

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.example.p2e_map.data.MainMapData
import org.example.p2e_map.data.Place
import org.example.p2e_map.data.ResourcesTileProvider
import org.example.p2e_map.widgets.*
import ovh.plrapps.mapcompose.api.*
import ovh.plrapps.mapcompose.ui.state.MapState

@Composable
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        val mainMapData = remember {
            MainMapData(scope).apply {
                initPins()
            }
        }
        DetailedDrawerExample(
            mainMapData = mainMapData
        ) { paddingValues ->
            UiMapContainer(
                modifier = Modifier.padding(paddingValues),
                mapState = mainMapData.mapState
            )
        }
    }
}