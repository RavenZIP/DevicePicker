package com.ravenzip.devicepicker.navigation.auth

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ravenzip.devicepicker.navigation.root.RootGraph
import com.ravenzip.devicepicker.screens.auth.ForgotPasswordScreen
import com.ravenzip.devicepicker.screens.auth.LoginScreen
import com.ravenzip.devicepicker.screens.auth.RegistrationScreen
import com.ravenzip.devicepicker.screens.auth.WelcomeScreen
import com.ravenzip.devicepicker.ui.theme.setWindowStyle

fun NavGraphBuilder.authNavigationGraph(navController: NavHostController) {
    navigation(route = RootGraph.AUTHENTICATION, startDestination = AuthGraph.WELCOME) {
        composable(route = AuthGraph.WELCOME) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surface,
                isAppearanceLight = !isSystemInDarkTheme()
            )

            WelcomeScreen(
                navigateToRegistrationScreen = { navController.navigate(AuthGraph.REGISTRATION) },
                navigateToLoginScreen = { navController.navigate(AuthGraph.LOGIN) },
                navigateToHomeScreen = { navigateToHome(navController) }
            )
        }
        composable(route = AuthGraph.REGISTRATION) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                isAppearanceLight = !isSystemInDarkTheme()
            )

            RegistrationScreen(navigateToHomeScreen = { navigateToHome(navController) })
        }
        composable(route = AuthGraph.LOGIN) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                isAppearanceLight = !isSystemInDarkTheme()
            )

            LoginScreen(
                navigateToHomeScreen = { navigateToHome(navController) },
                navigateToForgotPassScreen = { navController.navigate(AuthGraph.FORGOT_PASS) }
            )
        }
        composable(route = AuthGraph.FORGOT_PASS) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                isAppearanceLight = !isSystemInDarkTheme()
            )

            ForgotPasswordScreen()
        }
    }
}

private fun navigateToHome(navController: NavHostController) {
    // Для того, чтобы перейти на главный экран и при этом невозможно было вернуться назад
    navController.navigate(RootGraph.MAIN) {
        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
    }
}
