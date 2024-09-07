package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ravenzip.devicepicker.extensions.functions.navigateWithFadeAnimation
import com.ravenzip.devicepicker.extensions.functions.navigateWithSlideAnimation
import com.ravenzip.devicepicker.extensions.functions.navigateWithoutPreviousRoute
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.ui.screens.auth.SplashScreen
import com.ravenzip.devicepicker.ui.screens.main.MainScaffold
import com.ravenzip.devicepicker.ui.theme.SetWindowStyle

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = RootGraph.SPLASH_SCREEN,
    ) {
        navigateWithSlideAnimation(route = RootGraph.SPLASH_SCREEN) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surface,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            SplashScreen(
                navigateToAuthentication = {
                    navController.navigateWithoutPreviousRoute(
                        startDestination = RootGraph.ROOT,
                        targetDestination = RootGraph.AUTHENTICATION,
                    )
                },
                navigateToMain = {
                    navController.navigateWithoutPreviousRoute(
                        startDestination = RootGraph.ROOT,
                        targetDestination = RootGraph.MAIN,
                    )
                },
            )
        }

        authNavigationGraph(navController = navController)

        navigateWithFadeAnimation(route = RootGraph.MAIN) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            MainScaffold(
                navigateToSplashScreen = {
                    navController.navigateWithoutPreviousRoute(
                        startDestination = RootGraph.ROOT,
                        targetDestination = RootGraph.SPLASH_SCREEN,
                    )
                }
            )
        }
    }
}
