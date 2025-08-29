package org.example.p2e_map.widgets

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.p2e_map.date.Place
import org.jetbrains.compose.resources.painterResource
import ovh.plrapps.mapcompose.api.centerOnMarker
import ovh.plrapps.mapcompose.ui.state.MapState
import p2e_map.composeapp.generated.resources.Res
import p2e_map.composeapp.generated.resources.menu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedDrawerExample(
    mapState: MapState,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Places:",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    HorizontalDivider()
                    Place.getAll().forEach {
                        NavigationDrawerItem(
                            label = { Text(it.description) },
                            selected = false,
                            icon = {
                                Box(
                                    Modifier
                                        .size(32.dp)
                                        .background(
                                            color = Color.Red,
                                            shape = CircleShape
                                        )
                                        .align(Alignment.CenterHorizontally)
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .align(Alignment.Center),
                                        text = "${it.id}",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            },
                            onClick = { scope.launch{ mapState.centerOnMarker("${it.id}") } },
                        )
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
                    title = { Text("Map: Oklyon") },
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