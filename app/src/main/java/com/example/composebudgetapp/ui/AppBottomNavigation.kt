package com.example.composebudgetapp.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AppBottomNavigation(
    screens: List<AppScreen>,
    currentSelected: AppScreen,
    onClicked: (AppScreen) -> Unit
) {
    BottomNavigation {
        screens.forEach {
            BottomNavigationItem(selected = it == currentSelected, onClick = {
                onClicked(it)
            }, icon = {
                Icon(imageVector = it.icon, contentDescription = it.name)
            },
                selectedContentColor = MaterialTheme.colors.onSurface,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(0.6f), alwaysShowLabel = true, label = {
                    Text(text = it.name, fontSize = 9.sp)
                })
        }
    }
}

@Preview
@Composable
fun BottomNavPreview(){
    AppBottomNavigation(screens = AppScreen.values().toList(), currentSelected = AppScreen.Overview, onClicked = {})
}