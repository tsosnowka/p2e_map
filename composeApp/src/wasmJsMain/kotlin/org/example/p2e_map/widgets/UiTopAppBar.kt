package org.example.p2e_map.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.p2e_map.MyColors
import org.example.p2e_map.data.Place
import org.jetbrains.compose.resources.painterResource
import p2e_map.composeapp.generated.resources.Res
import p2e_map.composeapp.generated.resources.menu

@ExperimentalMaterial3Api
@Composable
fun UiTopAppBar(
    currentPlace: Place,
    onToggleDrawer: () -> Unit,
    onTitleClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onTitleClick() }
                        )
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                VerticalDivider(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .padding(end = 4.dp)
                )
                Box(
                    modifier = Modifier
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
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = currentPlace.description)
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onToggleDrawer()

                }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.menu),
                    contentDescription = null
                )
            }
        }
    )
}