package com.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.core.common.mobile.ui.navigation.Screen
import com.core.common.mobile.ui.screen.MainScreen
import com.feature.account.mobile.ui.screen.AccountScreen
import com.feature.home.mobile.ui.screen.HomeScreen
import com.feature.search.mobile.ui.screen.SearchScreen
import com.feature.splash.mobile.ui.screen.SplashScreen
import com.feature.videoplayer.mobile.ui.activity.VideoPlayerActivity
import com.feature.videos.mobile.ui.screen.VideosScreen

@Composable
fun NavHostController.BaseNavigation() {
    NavHost(
        this,
        startDestination = Screen.Splash.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = this@BaseNavigation)
        }
        composable(route = Screen.Main.route) {
            val navController: NavHostController = rememberNavController()
            MainScreen(navController) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                    modifier = Modifier.padding(it)
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(navController)
                    }
                    composable(Screen.Videos.route) {
                        VideosScreen()
                    }
                    composable(Screen.Search.route) {
                        SearchScreen(navController)
                    }
                    composable(Screen.Account.route) {
                        AccountScreen(navController)
                    }
                    activity(route = "${Screen.VideoPlayer.route}/{videoUrl}/{isLiveUrl}/{title}") {
                        activityClass = VideoPlayerActivity::class
                    }
                }
            }
        }
    }
}