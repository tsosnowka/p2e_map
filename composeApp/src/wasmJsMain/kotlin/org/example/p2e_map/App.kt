package org.example.p2e_map

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.example.p2e_map.date.Place
import org.example.p2e_map.widgets.*
import ovh.plrapps.mapcompose.api.*
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
        var selectedPlace by remember {
            mutableStateOf(Place.allPlaces.first())
        }
        var zoom by remember {
            mutableStateOf(mapState.scale)
        }
        mapState.setStateChangeListener {
            if (zoom!=scale) zoom = scale
        }
        val action = { place: Place ->
            mapState.removeMarker("selected-${selectedPlace.id}")
            selectedPlace = place
            mapState.addMarker(
                id = "selected-${place.id}",
                x = place.x,
                y = place.y
            ) {
                UiPinImage({ showCallout(mapState, place) }, place, true)
            }
        }

        LaunchedEffect(Unit) {
            Place.allPlaces.forEach {
                addPin(action, mapState, it)
            }
            action(selectedPlace)
        }
        DetailedDrawerExample(
            currentPlace = selectedPlace,
            zoom = zoom,
            mapState = mapState,
            onEnableStateChange = action
        ) { paddingValues ->
            UiMapContainer(modifier = Modifier.padding(paddingValues), mapState = mapState)
        }
    }
}