package com.app.ottsample.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.navigation.compose.rememberNavController
import com.app.ottsample.BuildConfig
import com.core.common.mobile.ui.theme.OTTSampleTheme
import com.navigation.BaseTvNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()
        setContent {
            OTTSampleTheme {
                Surface(color = White, modifier = Modifier.fillMaxSize()) {
                    rememberNavController().BaseTvNavigation()
                }
            }
        }
    }
}