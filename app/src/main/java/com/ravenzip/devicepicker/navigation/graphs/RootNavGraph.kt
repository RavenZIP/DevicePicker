package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.screens.main.ScaffoldScreen
import com.ravenzip.devicepicker.services.firebase.UserService
import com.ravenzip.devicepicker.ui.theme.SetWindowStyle

@Composable
fun RootNavigationGraph(navController: NavHostController, startDestination: String) {
    val userService = hiltViewModel<UserService>()

    NavHost(
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = startDestination
    ) {
        authNavigationGraph(navController = navController, userService = userService)
        composable(route = RootGraph.MAIN) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme()
            )

            ScaffoldScreen(userService = userService)
        }
    }
}
