package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.ui.screens.main.ScaffoldScreen
import com.ravenzip.devicepicker.ui.theme.SetWindowStyle
import com.ravenzip.devicepicker.viewmodels.UserViewModel

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    startDestination: String,
    userViewModel: UserViewModel,
) {
    NavHost(
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = startDestination,
    ) {
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
