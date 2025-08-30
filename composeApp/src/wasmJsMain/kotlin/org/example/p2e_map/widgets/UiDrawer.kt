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
import kotlinx.coroutines.launch
import org.example.p2e_map.MyColors
import org.example.p2e_map.date.Place
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skiko.hostOs
import ovh.plrapps.mapcompose.api.centerOnMarker
import ovh.plrapps.mapcompose.ui.state.MapState
import p2e_map.composeapp.generated.resources.Res
import p2e_map.composeapp.generated.resources.arrow_back
import p2e_map.composeapp.generated.resources.menu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedDrawerExample(
    currentPlace: Place,
    onEnableStateChange: (Place) -> Unit,
    mapState: MapState,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                    ){
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
                            VerticalDivider(Modifier.padding(12.dp))
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
                            Spacer(Modifier.padding(4.dp))
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
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}