package com.synerise.integration.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.synerise.integration.app.graphs.RootNavigationGraph
import com.synerise.integration.app.ui.theme.SyneriseAndroidIntegrationAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    private val userState by viewModels<UserStateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SyneriseAndroidIntegrationAppTheme {
                CompositionLocalProvider(LocalUserState provides userState) {
                    RootNavigationGraph(navController = rememberNavController())
                }
            }
        }
    }
}