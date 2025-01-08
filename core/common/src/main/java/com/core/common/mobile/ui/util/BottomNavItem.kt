package com.core.common.mobile.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.sharp.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val iconFilled: ImageVector,
    val iconUnfilled: ImageVector,
    val label: String
) {
    data object Home : BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, "Home")
    data object Videos :
        BottomNavItem(
            "Videos",
            Icons.Filled.PlayArrow,
            Icons.Outlined.PlayArrow,
            "Videos"
        )
    data object Search :
        BottomNavItem("Search", Icons.Filled.Search, Icons.Sharp.Search, "Search")

    data object Account :
        BottomNavItem(
            "Account",
            Icons.Filled.AccountCircle,
            Icons.Outlined.AccountCircle,
            "Account"
        )
}
