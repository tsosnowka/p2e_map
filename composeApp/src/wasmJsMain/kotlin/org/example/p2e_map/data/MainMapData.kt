package org.example.p2e_map.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.p2e_map.widgets.UiPinImage
import org.example.p2e_map.widgets.addPin
import org.example.p2e_map.widgets.showTooltip
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.addMarker
import ovh.plrapps.mapcompose.api.centerOnMarker
import ovh.plrapps.mapcompose.api.centroidX
import ovh.plrapps.mapcompose.api.centroidY
import ovh.plrapps.mapcompose.api.removeMarker
import ovh.plrapps.mapcompose.api.scale
import ovh.plrapps.mapcompose.api.setStateChangeListener
import ovh.plrapps.mapcompose.api.snapScrollTo
import ovh.plrapps.mapcompose.ui.state.MapState

class MainMapData(
    val scope: CoroutineScope
) {

    val mapState = MapState(
        levelCount = 1,
        fullWidth = 3840,
        fullHeight = 2160,
        tileSize = 3840
    ).apply {
        addLayer(ResourcesTileProvider("drawable/tiles"))
        setStateChangeListener {
            if (zoom != scale) zoom = scale
        }
    }

    var zoom: Double by mutableStateOf(1.0)

    var currentPlace: Place by mutableStateOf(Place.allPlaces.first())

    fun onEnableStateChange(place: Place) {
        mapState.removeMarker("selected")
        currentPlace = place
        mapState.addMarker(
            id = "selected",
            x = place.x,
            y = place.y
        ) {
            UiPinImage(
                onTapAction = {
                    showTooltip(
                        mapState = mapState,
                        place = place
                    )
                },
                place = place,
                enabled = true
            )
        }
        onTitleClick()
    }

    fun onTitleClick() {
        scope.launch {
            mapState.centerOnMarker("selected")
        }
    }

    fun onSnapScrollTo(newScale: Double) {
        val newX = mapState.centroidX
        val newY = mapState.centroidY
        mapState.scale = newScale
        scope.launch {
            mapState.snapScrollTo(
                newX,
                newY
            )
        }
    }

    fun initPins() {
        scope.launch {
            Place.allPlaces.forEach { place ->
                addPin(
                    onClick = { clickedPlace -> onEnableStateChange(clickedPlace) },
                    mapState = mapState,
                    place = place
                )
            }
            onEnableStateChange(currentPlace)
        }
    }
}