package com.core.common.mobile.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.core.common.mobile.ui.navigation.Screen
import com.core.common.mobile.ui.theme.OTTSampleTheme
import com.core.common.mobile.ui.util.BottomNavItem

@Composable
fun MainScreen(
    navController: NavHostController,
    callback: @Composable (innerPadding: PaddingValues) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    OTTSampleTheme {
        Surface(color = White, modifier = Modifier.navigationBarsPadding()) {
            Scaffold(
                bottomBar = {
                    if (shouldShowBottomNavigation(currentRoute)) {
                        BottomNavigationBar(navController, currentRoute)
                    }
                }
            ) { innerPadding ->
                callback.invoke(innerPadding)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    val screens = listOf(BottomNavItem.Home, BottomNavItem.Videos, BottomNavItem.Search, BottomNavItem.Account)

    BottomNavigation(
        backgroundColor = White,
        modifier = Modifier
            .background(color = White)
            .shadow(
                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
                elevation = 12.dp
            )
            .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
    ) {
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    if (currentRoute == screen.route) {
                        Icon(screen.iconFilled, contentDescription = screen.label)
                    } else {
                        Icon(screen.iconUnfilled, contentDescription = screen.label)
                    }
                },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun shouldShowBottomNavigation(route: String?): Boolean {
    return route in listOf(
        Screen.Main.route,
        Screen.Home.route,
        Screen.Search.route,
        Screen.Videos.route,
        Screen.Account.route
    )
}