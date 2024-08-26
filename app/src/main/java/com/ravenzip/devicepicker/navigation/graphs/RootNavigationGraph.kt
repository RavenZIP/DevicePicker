package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ravenzip.devicepicker.extensions.functions.composable
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.ui.screens.auth.SplashScreen
import com.ravenzip.devicepicker.ui.screens.main.ScaffoldScreen
import com.ravenzip.devicepicker.ui.theme.SetWindowStyle
import com.ravenzip.devicepicker.viewmodels.UserViewModel

@Composable
fun RootNavigationGraph(navController: NavHostController, userViewModel: UserViewModel) {
    NavHost(
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = RootGraph.SPLASH_SCREEN,
    ) {
        composable(route = RootGraph.SPLASH_SCREEN) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surface,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            SplashScreen(
                destroySplashScreen = { navController.popBackStack() },
                navigateToAuthentication = { navController.navigate(RootGraph.AUTHENTICATION) },
                navigateToMain = { navController.navigate(RootGraph.MAIN) },
                userViewModel = userViewModel,
            )
        }

        authNavigationGraph(navController = navController, userViewModel = userViewModel)

        composable(route = RootGraph.MAIN) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            ScaffoldScreen(
                userDataByViewModel = userViewModel.user,
                getUser = { userViewModel.getUser() },
                getUserData = { user -> userViewModel.get(user) },
                logout = { userViewModel.logout() },
            )
        }
    }
}
