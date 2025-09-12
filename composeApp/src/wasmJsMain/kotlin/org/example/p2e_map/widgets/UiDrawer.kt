package org.example.p2e_map.widgets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.example.p2e_map.data.MainMapData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedDrawerExample(
    mainMapData:MainMapData,
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val onToggleDrawer: () -> Unit = {
        scope.launch {
            if (drawerState.isClosed) drawerState.open()
            else drawerState.close()
        }
    }
    val onDrawerClose: () -> Unit = {
        scope.launch { drawerState.close() }
    }
    ModalNavigationDrawer(
        drawerContent = {
            UiDrawerSheet(onDrawerClose, mainMapData.currentPlace, { mainMapData.onEnableStateChange(it) })
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = { UiTopAppBar(mainMapData.currentPlace, onToggleDrawer, { mainMapData.onTitleClick() }) },
            bottomBar = { UiBottomAppBar(mainMapData.mapState, mainMapData.zoom, { mainMapData.onSnapScrollTo(it) }) }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}