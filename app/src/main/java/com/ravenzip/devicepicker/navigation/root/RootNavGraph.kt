package com.ravenzip.devicepicker.navigation.root

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ravenzip.devicepicker.navigation.auth.authNavigationGraph
import com.ravenzip.devicepicker.screens.main.ScaffoldScreen
import com.ravenzip.devicepicker.ui.theme.setWindowStyle

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = RootGraph.AUTHENTICATION
    ) {
        authNavigationGraph(navController = navController)
        composable(route = RootGraph.MAIN) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme()
            )

            ScaffoldScreen()
        }
    }
}
