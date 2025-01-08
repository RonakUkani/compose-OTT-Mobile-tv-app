package com.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.core.common.mobile.ui.navigation.Screen
import com.feature.home.tv.activity.HomeActivity
import com.feature.splash.tv.ui.screen.SplashScreen

@Composable
fun NavHostController.BaseTvNavigation() {
    NavHost(
        this,
        startDestination = Screen.Splash.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = this@BaseTvNavigation)
        }
        activity(route = Screen.Main.route) {
            activityClass = HomeActivity::class
        }
    }
}