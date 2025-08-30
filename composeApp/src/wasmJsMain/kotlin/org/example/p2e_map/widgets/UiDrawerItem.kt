package org.example.p2e_map.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.p2e_map.MyColors
import org.example.p2e_map.date.Place

@Composable
fun UiDrawerItem(
    place: Place,
    onClick: (Place) -> Unit
) {
    NavigationDrawerItem(
        label = { Text(place.description) },
        selected = false,
        icon = {
            Box(
                Modifier
                    .size(32.dp)
                    .background(
                        color = MyColors.pinDisabledColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    text = "${place.id}",
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        },
        onClick = {
            onClick(place)
        },
    )
}

