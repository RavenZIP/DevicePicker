package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ravenzip.devicepicker.extensions.functions.navigateWithFadeAnimation
import com.ravenzip.devicepicker.extensions.functions.navigateWithSlideInAnimation
import com.ravenzip.devicepicker.extensions.functions.navigateWithoutPreviousRoute
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.ui.screens.auth.SplashScreen
import com.ravenzip.devicepicker.ui.screens.main.ScaffoldScreen
import com.ravenzip.devicepicker.ui.theme.SetWindowStyle
import com.ravenzip.devicepicker.viewmodels.SplashScreenViewModel
import com.ravenzip.devicepicker.viewmodels.UserViewModel

@Composable
fun RootNavigationGraph(navController: NavHostController, userViewModel: UserViewModel) {
    NavHost(
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = RootGraph.SPLASH_SCREEN,
    ) {
        navigateWithSlideInAnimation(route = RootGraph.SPLASH_SCREEN) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surface,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            val splashScreenViewModel = hiltViewModel<SplashScreenViewModel>()

            SplashScreen(
                navigateToAuthentication = {
                    navController.navigateWithoutPreviousRoute(RootGraph.AUTHENTICATION)
                },
                navigateToMain = { navController.navigateWithoutPreviousRoute(RootGraph.MAIN) },
                firebaseUser = userViewModel.firebaseUser,
                splashScreenViewModel = splashScreenViewModel,
            )
        }

        authNavigationGraph(navController = navController, userViewModel = userViewModel)

        navigateWithFadeAnimation(route = RootGraph.MAIN) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            ScaffoldScreen(
                userDataByViewModel = userViewModel.user,
                updateDeviceHistory = { deviceHistory ->
                    userViewModel.updateDeviceHistory(deviceHistory)
                },
                firebaseUser = userViewModel.firebaseUser,
                getUserData = { userViewModel.getUserData() },
                logout = { userViewModel.logout() },
            )
        }
    }
}
