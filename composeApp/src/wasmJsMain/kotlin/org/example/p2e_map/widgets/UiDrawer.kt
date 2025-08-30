package org.example.p2e_map.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.p2e_map.MyColors
import org.example.p2e_map.date.Place
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skiko.hostOs
import ovh.plrapps.mapcompose.api.*
import ovh.plrapps.mapcompose.ui.state.MapState
import p2e_map.composeapp.generated.resources.*
import p2e_map.composeapp.generated.resources.Res
import p2e_map.composeapp.generated.resources.arrow_back
import p2e_map.composeapp.generated.resources.menu
import p2e_map.composeapp.generated.resources.zoom_in

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedDrawerExample(
    currentPlace: Place,
    zoom: Double,
    onEnableStateChange: (Place) -> Unit,
    mapState: MapState,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val step = 1.10
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { scope.launch { drawerState.close() } }
                        )
                    }
                ) {
                    IconButton(
                        onClick = {
                            scope.launch { drawerState.close() }
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
                        UiDrawerItem(place) {
                            onEnableStateChange(place)
                            scope.launch { drawerState.close() }
                            scope.launch { mapState.centerOnMarker("${it.id}") }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            VerticalDivider(Modifier.padding(vertical = 12.dp).padding(end = 4.dp))
                            Box(
                                Modifier
                                    .size(32.dp)
                                    .background(
                                        color = MyColors.pinEnabledColor,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .align(Alignment.Center),
                                    text = "${currentPlace.id}",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(Modifier.padding(2.dp))
                            Text(currentPlace.description)
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open()
                                    else drawerState.close()
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.menu),
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                Box(modifier = Modifier.fillMaxWidth().height(48.dp), contentAlignment = Alignment.Center) {
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
                            text = zoom.toString(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Row(
                        modifier = Modifier.wrapContentSize().align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        VerticalDivider(Modifier.padding(vertical = 12.dp, horizontal = 4.dp))
                        IconButton(onClick = {
                            bottomBarScrollTo(
                                mapState = mapState,
                                scope = scope,
                                scale = mapState.scale * 1.2
                            )
                        }) {
                            Icon(painter = painterResource(Res.drawable.zoom_in), contentDescription = null)
                        }
                        IconButton(onClick = {
                            bottomBarScrollTo(
                                mapState = mapState,
                                scope = scope,
                                scale = mapState.scale * 0.8
                            )
                        }) {
                            Icon(painter = painterResource(Res.drawable.zoom_out), contentDescription = null)
                        }
                        VerticalDivider(Modifier.padding(vertical = 12.dp, horizontal = 4.dp))
                        IconButton(onClick = { bottomBarScrollTo(mapState = mapState, scope = scope, scale = 1.0) }) {
                            Icon(painter = painterResource(Res.drawable.view_real_size), contentDescription = null)
                        }
                        VerticalDivider(Modifier.padding(vertical = 12.dp, horizontal = 4.dp))
                    }
                }
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}

private fun bottomBarScrollTo(mapState: MapState, scope: CoroutineScope, scale: Double) {
    val newX = mapState.centroidX
    val newY = mapState.centroidY
    mapState.scale = scale
    scope.launch {
        mapState.snapScrollTo(
            newX,
            newY
        )
    }
}