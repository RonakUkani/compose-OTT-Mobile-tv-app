package com.core.common.mobile.ui.navigation

sealed class Screen(
    val route: String,
) {
    data object Main : Screen("Main")
    data object Home : Screen("Home")
    data object Videos : Screen("Videos")
    data object Search : Screen("Search")
    data object Account : Screen("Account")
    data object VideoPlayer : Screen("VideoPlayer")
    data object Splash : Screen("Splash")
}
